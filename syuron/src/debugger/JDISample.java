// VM起動メモ
// Java Test -agentlib:jdwp=transport=localhost,server=y
// とりあえずこれ 
// Java -agentlib:jdwp=transport=dt_socket,suspend=y,server=y,address=localhost:8000 Test
// Java Test -Xdebug -Xrunjdwp:transport=localhost,address=8000,server=y,suspend=y

package debugger;

import java.io.*;
import java.util.*;

/*以下のライブラリは/lib/toos.jarに存在*/
import com.sun.jdi.*;
import com.sun.jdi.connect.*;
import com.sun.jdi.connect.Connector.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

public class JDISample {

	private static String[] excludes = { "java.*", "javax.*", "sun.*",
			"com.sun.*" };

	private VirtualMachine vm;
	private PrintWriter writer;

	public static void main(String[] args) {
		JDISample jdisample = new JDISample();
		// jdisample.run();
	}

	public JDISample() {
		writer = new PrintWriter(System.out);

		VirtualMachineManager vmm = Bootstrap.virtualMachineManager();

		// 起動型
		// // LaunchConnector取得
		// List<Connector> connectors = vmm.allConnectors();
		// LaunchingConnector lc = null;
		// for (Connector c : connectors) {
		// if (c.name().equals("com.sun.jdi.CommandLineLaunch")) {
		// lc = (LaunchingConnector) c;
		// }
		// }
		// // 引数設定
		// Map<String, Connector.Argument> arguments = null;
		// if (lc != null) {
		// // 起動クラス指定
		// arguments = lc.defaultArguments();
		//
		// Connector.Argument mainArg = (Connector.Argument) arguments
		// .get("main");
		// mainArg.setValue("Test");
		// // オプション指定
		// Connector.Argument optionArg = (Connector.Argument) arguments
		// .get("options");
		// optionArg
		// .setValue("-classpath D:\\SourceTree\\syuron\\syuuron\\bin\\sample;");
		//
		// for (String s : arguments.keySet()) {
		// System.out.println(s + ":" + arguments.get(s));
		// }
		// } else {
		// throw new Error("Not find launching connector");
		// }
		//
		// try {
		// vm = lc.launch(arguments);
		// } catch (IOException e) {
		// e.printStackTrace();
		// } catch (IllegalConnectorArgumentsException e) {
		// e.printStackTrace();
		// } catch (VMStartException e) {
		// e.printStackTrace();
		// }

		// 接続型
		List<AttachingConnector> connectors = vmm.attachingConnectors();
		AttachingConnector connector = connectors.get(0);
		Map<String, ? extends Argument> arguments = connector
				.defaultArguments();

		IntegerArgument portNumber = (IntegerArgument) arguments.get("port");
		portNumber.setValue("8000");
		StringArgument hostname = (StringArgument) arguments.get("hostname");
		hostname.setValue("localhost");
		try {
			vm = connector.attach(arguments);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalConnectorArgumentsException e) {
			e.printStackTrace();
		}

		if (vm != null) {
			System.out.println("接続完了");
		}

		createBP();

		vm.setDebugTraceMode(VirtualMachine.TRACE_NONE);
		// excludes = new String[0];
		EventThread eventThread = new EventThread(vm, excludes, writer);
		eventThread.setEventRequests(true);
		eventThread.start();
		vm.resume();

		System.out.println("終了");

	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}

	public void createBP() {
		List<ReferenceType> RefTyps = vm.allClasses();
		for (ReferenceType reftyp : RefTyps) {
			if (reftyp instanceof ClassType) {
				if (reftyp.name().equals("Test")) {
					System.out.println(reftyp.name());
				}
			}
		}
	}

}
