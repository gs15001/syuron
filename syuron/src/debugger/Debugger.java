package debugger;

import debugger.gui.GUI;

public class Debugger {
	
	public static final String NAME = "Debugger";
	public static final String VERSION = "1.0.0";
	public static final String WINDOWTITLE = "Debugger";

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.run(args);
	}

}
