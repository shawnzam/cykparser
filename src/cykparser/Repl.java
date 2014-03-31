package cykparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Repl {
	boolean validFile;
	CYKParser parser;

	public Repl() {
		this.validFile = false;
		this.run();
	}

	public void run() {
		String input = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (!validFile) {
			askForFile(br);
		}
		while (true) {
			System.out.print("Enter a sentance to test(or '.exit' to exit): ");
			try {
				input = br.readLine();

				if (input.equals(".exit")) {
					System.out.println("Exiting...");
					break;
				}
				System.out.println(this.parser.parse((input)));
			} catch (IOException ioe) {
				System.out.println("IO error trying to read");
			}
		}
	}

	private boolean askForFile(BufferedReader br) {
		String filename = "";
		System.out.print("Enter the path to your grammar: ");
		try {
			filename = br.readLine();
			System.out.println("Trying to open " + filename);
			this.parser = new CYKParser(filename);
			if (this.parser.tryToOpenFile()) {
				this.validFile = true;
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;

		}

	}
}
