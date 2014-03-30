package cykparser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CYKParser {

	protected List<String> lines;
	protected String memTable[][];
	protected String start;
	protected HashMap<String, Rule> grammerMap = new HashMap<String, Rule>();
	protected ArrayList<Rule> ruleList = new ArrayList<Rule>();

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
		ruleList.add(new Rule(l, r));
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
						String newVariable = this.memTable[k][col] + ","
								+ this.memTable[row - k - 1][col + k + 1];
						insetIntoTable(newVariable, row, col, false);

					}
				}
			}
		}
		System.out.println(ruleList.get(0).getVariable());
		return (this.memTable[inputSize - 1][0] != null && this.memTable[inputSize - 1][0]
				.contains(ruleList.get(0).getVariable()));
	}

	private boolean doFirstRow(String[] splited) {
		for (int col = 0; col < splited.length; col++) {
			insetIntoTable(splited[col] + " ", 0, col, true);
		}
		return false;
	}

	HashSet<String> makeCartensianProduct(String token) {
		if (token != null) {
			HashSet<String> returnSet = new HashSet<String>();
			if (token.contains(",")) {
				returnSet = new HashSet<String>();
				String[] tokens = token.split(",");
				char[] one = tokens[0].toCharArray();
				char[] two = tokens[1].toCharArray();
				for (int i = 0; i < one.length; i++) {
					for (int j = 0; j < two.length; j++) {
						returnSet.add("" + one[i] + two[j] + "");
					}
				}
				return returnSet;
			}
			returnSet = new HashSet<String>();
			returnSet.add(token);
			return returnSet;

		}

		return null;
	}

	private void insertOrUpdate(int row, int col, Rule r) {
		if (this.memTable[row][col] != null) {
			HashSet<Character> dupeRemover = new HashSet<Character>();
			char[] dupestring = (this.memTable[row][col] + r.getVariable())
					.toCharArray();
			for (char c : dupestring) {
				dupeRemover.add(c);
			}
			String dupefree = "";
			for (char c : dupeRemover) {
				dupefree += c;
			}

			this.memTable[row][col] = dupefree;
		} else {
			this.memTable[row][col] = r.getVariable();
		}
	}

	private boolean insetIntoTable(String rhs, int row, int col,
			boolean firstrow) {
		if (firstrow) {
			for (Rule r : ruleList) {
				if (r.isTerminal()) {
					if (r.rhs.terminal.equals(rhs)) {
						insertOrUpdate(row, col, r);
					}
				}
			}
		} else {

			HashSet<String> variables = makeCartensianProduct(rhs);

			for (Rule r : ruleList) {
				if (r.isVariable()) {
					for (String c : variables) {
						if (r.rhs.variables.equals(c)) {
							insertOrUpdate(row, col, r);
						}
					}
				}
			}

		}

		return firstrow;

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
		CYKParser parser = new CYKParser("ab.txt");
		// System.out.println(parser.inGrammer("ate ").getVariable());
		boolean x = parser.parse("b a a b a ");
		System.out.println(x);
		printArray(parser.memTable);
	}

}
