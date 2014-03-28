package cykparser;

public class RHS {
	String terminal;
	String variables;

	public RHS(String terminal, String variables) {
		this.terminal = terminal;
		this.variables = variables;
	}
	
	public boolean isTerminal() {
		return this.variables == null;
	}

	public boolean isVariable() {
		return this.terminal == null;
	}
	
	public String getKey(){
		if (this.isVariable()){
			return this.variables;
		} else {
			return this.terminal;
		}
	}
	

}
