package cykparser;

public class Rule {
	LHS lhs;
	RHS rhs;
	
	public Rule(LHS lhs, RHS rhs){
		this.lhs = lhs;
		this.rhs = rhs;
	}
	
	public String getType(){
		String ret = this.isTerminal() ?  "Terminal" :  "Variable";
		return ret;
	}
	
	public boolean isTerminal() {
		return this.rhs.variables == null;
	}

	public boolean isVariable() {
		return this.rhs.terminal == null;
	}
	
	public String getVariable(){
		return this.lhs.variable + "";
	}

	@Override 
	public String toString() {
		String lhs = this.lhs.variable + "";
		String rhs = "";
		if (this.isVariable()){
			rhs = this.rhs.variables + "("+this.getType()+")";
		} else {
			rhs=  this.rhs.terminal + "("+this.getType()+")";
		}
		return " " + lhs + " -> " + rhs;
	}
}
