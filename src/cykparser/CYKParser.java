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

	public boolean parse(String in) {
		String[] splited = in.split(" ");
		int inputSize = splited.length;
		this.memTable = new String[splited.length][splited.length];
		this.doFirstRow(splited);
		for (int row = 1; row < inputSize; row++) {
			for (int col = 0; col < inputSize - row; col++) {
				for (int k = 0; k < row; k++) {
					if (this.memTable[k][col] != null
							&& this.memTable[row - k - 1][col + k + 1] != null) {
						String newVariable = this.memTable[k][col] + ""
								+ this.memTable[row - k - 1][col + k + 1];
						insetIntoTable(newVariable, row, col);
					}
				}
			}
		}

		return (this.memTable[inputSize-1][0] != null && this.memTable[4][0].contains("S"));
	}

	private boolean doFirstRow(String[] splited) {
		for (int col = 0; col < splited.length; col++) {
			insetIntoTable(splited[col] + " ", 0, col);
		}
		return false;
	}

	private boolean insetIntoTable(String token, int row, int col) {
		if (this.grammerMap.containsKey(token)) {
			Rule rule = this.grammerMap.get(token);
			this.memTable[row][col] = rule.getVariable();
			return true;
		}
		return false;
	}

	// taken from stackoverflow
	static void printArray(String matrix[][]) {
		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix[row].length; column++) {
				System.out.print("  |  " + matrix[row][column]);
			}
			System.out.println(" | ");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CYKParser parser = new CYKParser("english.txt");
		// System.out.println(parser.inGrammer("ate ").getVariable());
		boolean x = parser.parse("amy ate fish for dinner for dinner ");
		System.out.println(x);
		printArray(parser.memTable);
	}

}
