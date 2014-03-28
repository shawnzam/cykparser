package cykparser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import java.util.List;

public class CYKParser {

	protected List<String> lines;
	protected String memTable[][];
	protected String start;
	protected HashMap<String, Rule> grammerMap = new HashMap<String, Rule>();

	public CYKParser(String fileName) {
		try {
			this.lines = Files.readAllLines(Paths.get(fileName),
					Charset.defaultCharset());
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (!checkInputFile()) {
			System.out
					.println("The formating of this line is incorrect. "
							+ "\nPlease make sure that there is a space before and after -> "
							+ "\nand a space following any variable defined. ");
		}
		makeGrammer();

		System.out.println(grammerMap);
	}

	boolean checkInputFile() {
		for (String line : this.lines) {
			if (!line.matches("^[A-Z] -> ([A-Z]{2}|[a-z]+ )")) {
				System.out.println(line);
				return false;
			}
		}
		return true;
	}

	void makeGrammer() {
		start = this.lines.get(0);
		for (String line : this.lines) {
			splitLineToMap(line);
		}
	}

	private void splitLineToMap(String s) {
		LHS l = new LHS(s.charAt(0));
		// check if we have a terminal or variable on RHS
		RHS r;
		if (s.charAt(s.length() - 1) == ' ') {
			r = new RHS(s.substring(5), null);
		} else {
			r = new RHS(null, s.substring(5));
		}
		grammerMap.put(r.getKey(), new Rule(l, r));
	}

	private Rule inGrammer(String terminal) {
		for (String key : grammerMap.keySet()) {
			if (terminal.equals(key)) {
				return grammerMap.get(key);
			}

		}
		return null;

	}

	public String parse(String in) {
		String[] splited = in.split(" ");
		this.memTable = new String[splited.length][splited.length];
		for (int i = 0; i < splited.length; i++) {
			// for (int k = splited.length - i - 1; k >= 0; k--) {
			for (int k = 0; k < i + 1; k++) {
				this.memTable[i][k] = "----";
			}
		}
		return "";
	}

	// taken from stackoverflow
	static void printMatrix(String[][] grid) {
		for (int r = 0; r < grid.length; r++) {
			for (int c = 0; c < grid[r].length; c++)
				System.out.print(" | " + grid[r][c]);

			System.out.println(" |");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CYKParser parser = new CYKParser("english.txt");
		System.out.println(parser.inGrammer("amy ").getVariable());
		parser.parse("f o o");

		printMatrix(parser.memTable);
	}

}
