package cn.yyx.research.program.ir.parse.structure;

public class IRStatementCheckResult {
	
	private boolean statement_is_valid = false;
	private int actual_amount_of_variable = 0;
	
	public IRStatementCheckResult(boolean statement_is_valid, int actual_amount_of_variable) {
		this.actual_amount_of_variable = actual_amount_of_variable;
		this.statement_is_valid = statement_is_valid;
	}

	public boolean IsStatementValid() {
		return statement_is_valid;
	}

	public int GetActualAmountOfVariable() {
		return actual_amount_of_variable;
	}
	
}
