/* Copyright (c) 1998, 2011, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. */

/* This source code is provided to illustrate the usage of a given feature
 * or technique and has been deliberately simplified. Additional steps
 * required for a production-quality application, such as security checks,
 * input validation and proper error handling, might not be present in
 * this sample code. */

package debugger.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import com.sun.jdi.VirtualMachine;
import debugger.bdi.ExecutionManager;

public class GUI extends JPanel {

	private static final long serialVersionUID = 3292463234530679091L;

	private JFrame frame;
	private CommandTool cmdTool;
	private ApplicationTool appTool;
	// ###HACK##
	// ### There is currently dirty code in Environment that
	// ### accesses this directly.
	// private SourceTool srcTool;
	public static SourceTool srcTool;

	private SourceTreeTool sourceTreeTool;
	private ClassTreeTool classTreeTool;
	private ThreadTreeTool threadTreeTool;
	private StackTraceTool stackTool;
	// private MonitorTool monitorTool;
	// private VariableTool variableTool;
	private VariableTool variableTreeTable;
	private JPanel supportTool;// 未実装 とりあえずパネル配置
	private JDBMenuBar menuBar;

	private final Environment env = new Environment();

	public static final String progname = "javadt";
	public static final String version = "1.0Beta"; // ### FIX ME.
	public static final String windowBanner = "Java(tm) platform Debug Tool";

	private Font fixedFont = new Font("consolas", Font.PLAIN, 11);

	public final int WINDOW_WIDTH;
	public final int WINDOW_HEIGHT;

	public GUI() {
		int height = Toolkit.getDefaultToolkit().getScreenSize().height;
		int width = Toolkit.getDefaultToolkit().getScreenSize().width;

		// 推奨スペック(解像度)1366*768以上
		if(width > 1366) {
			WINDOW_WIDTH = 1366;
			WINDOW_HEIGHT = 768;
		} else {
			WINDOW_WIDTH = width;
			WINDOW_HEIGHT = height;
		}

		setLayout(new BorderLayout());

		setBorder(new EmptyBorder(5, 5, 5, 5));

		add(new JDBToolBar(env), BorderLayout.NORTH);
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// ソースビュー
		srcTool = new SourceTool(env);
		srcTool.setPreferredSize(new java.awt.Dimension((int) (WINDOW_WIDTH * 0.5), (int) (WINDOW_HEIGHT * 0.7)));
		srcTool.setTextFont(fixedFont);

		// ソースツリービュー
		sourceTreeTool = new SourceTreeTool(env);
		sourceTreeTool.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.1), (int) (WINDOW_HEIGHT * 0.7)));

		JSplitPane left = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sourceTreeTool, srcTool);

		// デバッグ情報ビュー
		// monitorTool = new MonitorTool(env);
		// monitorTool.setPreferredSize(new java.awt.Dimension(500, 150));
		// variableTool = new VariableTool(env);
		// variableTool.setPreferredSize(new java.awt.Dimension(500, 150));

		variableTreeTable = new VariableTool(env);
		variableTreeTable.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.35), (int) (WINDOW_HEIGHT * 0.3)));

		stackTool = new StackTraceTool(env);
		stackTool.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.35), (int) (WINDOW_HEIGHT * 0.3)));

		// デバッグ情報ビューをタブ化
		JTabbedPane infoPane = new JTabbedPane(SwingConstants.TOP);
		infoPane.setFont(new Font("メイリオ", Font.PLAIN, 14));
		infoPane.addTab("変数", variableTreeTable);
		infoPane.addTab("呼び出し階層", stackTool);

		// 支援情報ビュー
		supportTool = new SupportTool(this);
		supportTool.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.35), (int) (WINDOW_HEIGHT * 0.3)));

		JSplitPane right = new JSplitPane(JSplitPane.VERTICAL_SPLIT, infoPane, supportTool);

		// Treeは使わないので非表示に 一応オブジェクトは生成しておく
		classTreeTool = new ClassTreeTool(env);
		threadTreeTool = new ThreadTreeTool(env);

		// classTreeTool.setPreferredSize(new java.awt.Dimension(200, 450));
		// threadTreeTool.setPreferredSize(new java.awt.Dimension(200, 450));

		// JTabbedPane treePane = new JTabbedPane(SwingConstants.BOTTOM);
		// treePane.addTab("Source", null, sourceTreeTool);
		// treePane.addTab("Classes", null, classTreeTool);
		// treePane.addTab("Threads", null, threadTreeTool);

		JSplitPane centerTop = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left, right);

		// cmdToolは使わないので非表示に 一応オブジェクトは生成しておく
		cmdTool = new CommandTool(env);
		// cmdTool.setPreferredSize(new java.awt.Dimension(700, 150));

		appTool = new ApplicationTool(env);
		appTool.setPreferredSize(new Dimension((int) (WINDOW_WIDTH * 0.6), (int) (WINDOW_HEIGHT * 0.1)));

		// cmdTool削除によりcenterBottomのスプリット消失
		// JSplitPane centerBottom = new
		// JSplitPane(JSplitPane.VERTICAL_SPLIT,cmdTool, appTool);

		// 初期から
		// centerBottom.setPreferredSize(new java.awt.Dimension(700, 350));

		JSplitPane center = new JSplitPane(JSplitPane.VERTICAL_SPLIT, centerTop, appTool);

		add(center, BorderLayout.CENTER);

	}

	private static void usage() {
		String separator = File.pathSeparator;
		System.out.println("Usage: " + progname + " <options> <class> <arguments>");
		System.out.println();
		System.out.println("where options include:");
		System.out.println("    -help             print out this message and exit");
		System.out.println("    -sourcepath <directories separated by \"" + separator + "\">");
		System.out.println("                      list directories in which to look for source files");
		System.out.println("    -remote <hostname>:<port-number>");
		System.out.println("                      host machine and port number of interpreter to attach to");
		System.out.println("    -dbgtrace [flags] print info for debugging " + progname);
		System.out.println();
		System.out.println("options forwarded to debuggee process:");
		System.out.println("    -v -verbose[:class|gc|jni]");
		System.out.println("                      turn on verbose mode");
		System.out.println("    -D<name>=<value>  set a system property");
		System.out.println("    -classpath <directories separated by \"" + separator + "\">");
		System.out.println("                      list directories in which to look for classes");
		System.out.println("    -X<option>        non-standard debuggee VM option");
		System.out.println();
		System.out.println("<class> is the name of the class to begin debugging");
		System.out.println("<arguments> are the arguments passed to the main() method of <class>");
		System.out.println();
		System.out.println("For command help type 'help' at " + progname + " prompt");
	}

	public void run(String argv[]) {
		String clsName = "";
		String progArgs = "";
		String javaArgs = "";

		JPanel mainPanel = this;

		ContextManager context = env.getContextManager();
		ExecutionManager runtime = env.getExecutionManager();

		for (int i = 0; i < argv.length; i++) {
			String token = argv[i];
			if(token.equals("-dbgtrace")) {
				if((i == argv.length - 1) || !Character.isDigit(argv[i + 1].charAt(0))) {
					runtime.setTraceMode(VirtualMachine.TRACE_ALL);
				} else {
					String flagStr = argv[++i];
					runtime.setTraceMode(Integer.decode(flagStr).intValue());
				}
			} else if(token.equals("-X")) {
				System.out.println("Use 'java -X' to see the available non-standard options");
				System.out.println();
				usage();
				System.exit(1);
			} else if(
			// Standard VM options passed on
			token.equals("-v")
					|| token.startsWith("-v:")
					|| // -v[:...]
					token.startsWith("-verbose")
					|| // -verbose[:...]
					token.startsWith("-D")
					||
					// NonStandard options passed on
					token.startsWith("-X")
					||
					// Old-style options
					// (These should remain in place as long as the standard VM
					// accepts them)
					token.equals("-noasyncgc") || token.equals("-prof") || token.equals("-verify")
					|| token.equals("-noverify") || token.equals("-verifyremote") || token.equals("-verbosegc")
					|| token.startsWith("-ms") || token.startsWith("-mx") || token.startsWith("-ss")
					|| token.startsWith("-oss")) {
				javaArgs += token + " ";
			} else if(token.equals("-sourcepath")) {
				if(i == (argv.length - 1)) {
					System.out.println("No sourcepath specified.");
					usage();
					System.exit(1);
				}
				env.getSourceManager().setSourcePath(new SearchPath(argv[++i]));
			} else if(token.equals("-classpath")) {
				if(i == (argv.length - 1)) {
					System.out.println("No classpath specified.");
					usage();
					System.exit(1);
				}
				env.getClassManager().setClassPath(new SearchPath(argv[++i]));
			} else if(token.equals("-remote")) {
				if(i == (argv.length - 1)) {
					System.out.println("No remote specified.");
					usage();
					System.exit(1);
				}
				env.getContextManager().setRemotePort(argv[++i]);
			} else if(token.equals("-help")) {
				usage();
				System.exit(0);
			} else if(token.equals("-version")) {
				System.out.println(progname + " version " + version);
				System.exit(0);
			} else if(token.startsWith("-")) {
				System.out.println("invalid option: " + token);
				usage();
				System.exit(1);
			} else {
				// Everything from here is part of the command line
				clsName = token;
				for (i++; i < argv.length; i++) {
					progArgs += argv[i] + " ";
				}
				break;
			}
		}

		context.setMainClassName(clsName);
		context.setProgramArguments(progArgs);
		context.setVmArguments(javaArgs);

		// Force Cross Platform L&F
		// try {
		// UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		// If you want the System L&F instead, comment out the above line
		// and
		// uncomment the following:
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		// } catch (Exception exc) {
		// System.err.println("Error loading L&F: " + exc);
		// }

		frame = new JFrame();
		frame.setPreferredSize(this.getSize());
		frame.setBackground(Color.lightGray);
		frame.setTitle(windowBanner);
		menuBar = new JDBMenuBar(env);
		frame.setJMenuBar(menuBar);
		frame.setContentPane(mainPanel);

		// frame.addWindowListener(new WindowAdapter() {
		//
		// @Override
		// public void windowClosing(WindowEvent e) {
		// env.terminate();
		// }
		// });
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		frame.pack();
		frame.setVisible(true);

		// try {
		// UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		// SwingUtilities.updateComponentTreeUI(frame);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	public void openFile(File f) {
		menuBar.openFile(f);
	}
}
