package editor;

import java.awt.*;

import javax.swing.*;

public class EditorControler extends JFrame{

	private JTextArea ta;
	private EditorMenu em;
	
	public EditorControler(JTextArea ta,EditorMenu em) {
		super("test");
		
		this.ta = ta;
		this.em = em;
		
		Container c = getContentPane();
		c.setLayout(new BorderLayout());
		
		c.add(em,BorderLayout.NORTH);
		JScrollPane sp = new JScrollPane(ta);
		c.add(sp,BorderLayout.CENTER);
		
		setSize(800,600);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
}