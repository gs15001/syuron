package org.jeditor.app;

import java.io.*;

public class OutputStreamThread extends Thread {

	private BufferedReader br;
	private ConsolePane console = null;

	public OutputStreamThread(InputStream is) {
		br = new BufferedReader(new InputStreamReader(is));
	}

	public OutputStreamThread(InputStream is, String charset) {
		try {
			br = new BufferedReader(new InputStreamReader(is, charset));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public OutputStreamThread(InputStream is, String charset, ConsolePane console) {
		this(is, charset);
		this.console = console;
	}

	@Override
	public void run() {
		String line;
		try {
			while ((line = br.readLine()) != null) {
				if(console != null) {
					console.outputLine(line);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
