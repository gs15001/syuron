package debugger;

import java.util.*;

/*以下のライブラリは/lib/toos.jarに存在*/
import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

class Debugger extends Thread {
	private VirtualMachine vm;
	private EventQueue queue;

	private boolean exitRequested = false;

	public Debugger(VirtualMachine vm) {
		this.vm = vm;

		EventRequestManager manager = vm.eventRequestManager();

		// refType = all, notifyCaught = false, notifyUncaught = true
		ExceptionRequest req = manager
				.createExceptionRequest(null, false, true);

		req.setSuspendPolicy(EventRequest.SUSPEND_EVENT_THREAD);
		req.enable();
	}

	public void showThreadInfo(ThreadReference thread)
			throws IncompatibleThreadStateException {
		List<StackFrame> frames = thread.frames();
		Iterator<StackFrame> it = frames.iterator();
		while (it.hasNext()) {
			StackFrame frame = it.next();
			Location loc = frame.location();
			Method method = loc.method();
			long codeIndex = loc.codeIndex();

			System.err.println("StackFarme location:");
			try {
				System.err.println(" sourceName = " + loc.sourceName());
			} catch (AbsentInformationException ex) {
				System.err.println(" sourceName = (Unknown)");
			}
			System.err.println(" lineNumber = " + loc.lineNumber());
			System.err.println(" method     = " + method);
			try {
				byte[] bytecodes = method.bytecodes();
				System.err.println(" codeIndex  = " + codeIndex + "/"
						+ bytecodes.length);
			} catch (UnsupportedOperationException ex) {
				System.err.println(" codeIndex  = " + codeIndex);
			}
		}
	}

	public void processExceptionEvent(ExceptionEvent ee) {
		ThreadReference thread = ee.thread();
		try {
			// showThreadInfo(thread);

			thread.suspend();
			List<StackFrame> frames = thread.frames();
			if (!frames.isEmpty()) {
				StackFrame frame = frames.get(0);
				thread.popFrames(frame);
			}
			thread.resume();
		} catch (IncompatibleThreadStateException ex) {
			System.err.println("ThreadReference.frame() => " + ex);
		}
	}

	public void run() {
		queue = vm.eventQueue();
		while (!exitRequested) {
			try {
				EventSet events = queue.remove(1000);
				if (events == null)
					continue;

				EventIterator it = events.eventIterator();
				while (it.hasNext()) {
					Event event = it.nextEvent();
					if (event instanceof ExceptionEvent) {
						processExceptionEvent((ExceptionEvent) event);
					}
				}
				events.resume();
			} catch (InterruptedException ex) {
				// System.err.println("Debugger: interrupted");
				break;
			}
		}
		System.err.println("Debugger: exited.");
	}

	public void exit() {
		exitRequested = true;
		interrupt();
	}
}