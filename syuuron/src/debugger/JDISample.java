package debugger;

import java.io.*;
import java.util.*;

/*以下のライブラリは/lib/toos.jarに存在*/
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.Connector.*;

public class JDISample {

	private VirtualMachine vm;

	public static void main(String[] args) {
		JDISample jdisample = new JDISample();
		jdisample.run();
	}

	public JDISample() {
		VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

		/*
		LaunchingConnector lc = vmm.defaultConnector();
		Map<String, ? extends Argument> arg = lc.defaultArguments();

		Set<String> t = arg.keySet();
		for (String key : t) {
			System.out.println(arg.get(key));
		}

		StringArgument option = (StringArgument) arg.get("options");
		option.setValue("-Xdebug");
		
		//IntegerArgument portNumber = (IntegerArgument) arg.get("port");
		//portNumber.setValue("4321");
		//StringArgument hostname = (StringArgument) arg.get("hostname");
		//hostname.setValue("localhost");

		for (String key : t) {
			System.out.println(arg.get(key));
		}

		try {
			vm = lc.launch(arg);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalConnectorArgumentsException e) {
			e.printStackTrace();
		} catch (VMStartException e) {
			e.printStackTrace();
		}
		 */
		List<AttachingConnector> connectors = vmm.attachingConnectors();
		AttachingConnector connector = connectors.get(0);
		for(AttachingConnector ac : connectors) {
			System.out.println(ac);
		}
		Map<String, ? extends Argument> arguments = connector.defaultArguments();
		Set<String> t = arguments.keySet();
		for (String key : t) {
			System.out.println(arguments.get(key));
		}
		IntegerArgument portNumber = (IntegerArgument) arguments.get("port");
		portNumber.setValue("8000");
		StringArgument hostname = (StringArgument) arguments.get("hostname");
		hostname.setValue("localhost");
		for (String key : t) {
			System.out.println(arguments.get(key));
		}
		try {
			vm = connector.attach(arguments);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalConnectorArgumentsException e) {
			e.printStackTrace();
		}
		
		if(vm != null) {
			System.out.println("接続完了");
		}
		
		//VM起動メモ
		//Java Test -agentlib:jdwp=transport=localhost,server=y
		//とりあえずこれ   Java -agentlib:jdwp=transport=dt_socket,suspend=y,server=y,address=localhost:8000 Test
		//Java Test -Xdebug -Xrunjdwp:transport=localhost,address=8000,server=y,suspend=y
	}

	public void run() {
		List<ThreadReference> list = vm.allThreads();
		for (ThreadReference tr : list) {
			try {
				tr.suspend();
				Thread.sleep(1000);
				System.out.println("test");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (tr.isSuspended()) {
					tr.resume();
				}
			}
		}
	}

}
