/*
 * Copyright (c) 1999, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

/*
 * This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code.
 */

package debugger.bdi;

import com.sun.jdi.*;
import com.sun.jdi.request.*;

import java.util.*;

class EventRequestSpecList {

	// all specs
	private List<EventRequestSpec> eventRequestSpecs = Collections
			.synchronizedList(new ArrayList<EventRequestSpec>());

	final ExecutionManager runtime;

	EventRequestSpecList(ExecutionManager runtime) {
		this.runtime = runtime;
	}

	/**
	 * Resolve all deferred eventRequests waiting for 'refType'.
	 */
	void resolve(ReferenceType refType) {
		synchronized (eventRequestSpecs) {
			for (EventRequestSpec spec : eventRequestSpecs) {
				spec.attemptResolve(refType);
			}
		}
	}

	void install(EventRequestSpec ers, VirtualMachine vm) {
		synchronized (eventRequestSpecs) {
			eventRequestSpecs.add(ers);
		}
		if (vm != null) {
			ers.attemptImmediateResolve(vm);
		}
	}

	BreakpointSpec createClassLineBreakpoint(String classPattern, int line) {
		ReferenceTypeSpec refSpec = new PatternReferenceTypeSpec(classPattern);
		return new LineBreakpointSpec(this, refSpec, line);
	}

	BreakpointSpec createSourceLineBreakpoint(String sourceName, int line) {
		ReferenceTypeSpec refSpec = new SourceNameReferenceTypeSpec(sourceName,
				line);
		return new LineBreakpointSpec(this, refSpec, line);
	}

	BreakpointSpec createMethodBreakpoint(String classPattern, String methodId,
			List<String> methodArgs) {
		ReferenceTypeSpec refSpec = new PatternReferenceTypeSpec(classPattern);
		return new MethodBreakpointSpec(this, refSpec, methodId, methodArgs);
	}

	ExceptionSpec createExceptionIntercept(String classPattern,
			boolean notifyCaught, boolean notifyUncaught) {
		ReferenceTypeSpec refSpec = new PatternReferenceTypeSpec(classPattern);
		return new ExceptionSpec(this, refSpec, notifyCaught, notifyUncaught);
	}

	AccessWatchpointSpec createAccessWatchpoint(String classPattern,
			String fieldId) {
		ReferenceTypeSpec refSpec = new PatternReferenceTypeSpec(classPattern);
		return new AccessWatchpointSpec(this, refSpec, fieldId);
	}

	ModificationWatchpointSpec createModificationWatchpoint(
			String classPattern, String fieldId) {
		ReferenceTypeSpec refSpec = new PatternReferenceTypeSpec(classPattern);
		return new ModificationWatchpointSpec(this, refSpec, fieldId);
	}

	void delete(EventRequestSpec ers) {
		EventRequest request = ers.getEventRequest();
		synchronized (eventRequestSpecs) {
			eventRequestSpecs.remove(ers);
		}
		if (request != null && runtime.vm() != null) {
			request.virtualMachine().eventRequestManager()
					.deleteEventRequest(request);
		}
		notifyDeleted(ers);
		// ### notify delete - here?
	}

	List<EventRequestSpec> eventRequestSpecs() {
		// We need to make a copy to avoid synchronization problems
		synchronized (eventRequestSpecs) {
			return new ArrayList<EventRequestSpec>(eventRequestSpecs);
		}
	}

	// -------- notify routines --------------------

	@SuppressWarnings("unchecked")
	private ArrayList<SpecListener> specListeners() {
		return (ArrayList<SpecListener>) runtime.specListeners.clone();
	}

	void notifySet(EventRequestSpec spec) {
		ArrayList<SpecListener> l = specListeners();
		SpecEvent evt = new SpecEvent(spec);
		for (int i = 0; i < l.size(); i++) {
			spec.notifySet(l.get(i), evt);
		}
	}

	void notifyDeferred(EventRequestSpec spec) {
		ArrayList<SpecListener> l = specListeners();
		SpecEvent evt = new SpecEvent(spec);
		for (int i = 0; i < l.size(); i++) {
			spec.notifyDeferred(l.get(i), evt);
		}
	}

	void notifyDeleted(EventRequestSpec spec) {
		ArrayList<SpecListener> l = specListeners();
		SpecEvent evt = new SpecEvent(spec);
		for (int i = 0; i < l.size(); i++) {
			spec.notifyDeleted(l.get(i), evt);
		}
	}

	void notifyResolved(EventRequestSpec spec) {
		ArrayList<SpecListener> l = specListeners();
		SpecEvent evt = new SpecEvent(spec);
		for (int i = 0; i < l.size(); i++) {
			spec.notifyResolved(l.get(i), evt);
		}
	}

	void notifyError(EventRequestSpec spec, Exception exc) {
		ArrayList<SpecListener> l = specListeners();
		SpecErrorEvent evt = new SpecErrorEvent(spec, exc);
		for (int i = 0; i < l.size(); i++) {
			spec.notifyError(l.get(i), evt);
		}
	}
}
