package debugger;

import java.io.*;
import java.util.*;

/*以下のライブラリは/lib/toos.jarに存在*/
import com.sun.jdi.*;
import com.sun.jdi.connect.*;

public class JDITest {
	private static Connector findConnector(String name) {
		VirtualMachineManager vmManager = Bootstrap.virtualMachineManager();
		List<Connector> connectors = vmManager.allConnectors();
		Iterator<Connector> it = connectors.iterator();
		while (it.hasNext()) {
			Connector connector = it.next();
			if (connector.name().equals(name))
				return connector;
		}
		return null;
	}

	public static void main(String[] args) {
		AttachingConnector connector = (AttachingConnector) findConnector("com.sun.jdi.SocketAttach");
		if (connector == null) {
			throw new RuntimeException("No connector");
		}
		Map<String, Connector.Argument> arguments = connector
				.defaultArguments();
		arguments.get("hostname").setValue("localhost");
		arguments.get("port").setValue("4321");

		VirtualMachine vm = null;
		try {
			vm = connector.attach(arguments);
		} catch (IllegalConnectorArgumentsException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		Debugger debugger = new Debugger(vm);
		debugger.start();

		Parent parent = new Parent();

		debugger.exit();
	}
}
