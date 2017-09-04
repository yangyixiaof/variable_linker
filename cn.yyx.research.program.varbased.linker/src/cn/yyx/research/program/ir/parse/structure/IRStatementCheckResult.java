package cn.yyx.research.program.ir.parse.structure;

public class IRStatementCheckResult {
	
	private boolean statement_is_valid = false;
	private int expected_amount_of_variable = 0;
	private int actual_amount_of_variable = 0;
	
	public IRStatementCheckResult(boolean statement_is_valid, int expected_amount_of_variable, int actual_amount_of_variable) {
		this.statement_is_valid = statement_is_valid;
		this.expected_amount_of_variable = expected_amount_of_variable;
		this.actual_amount_of_variable = actual_amount_of_variable;
	}

	public boolean IsStatementValid() {
		return statement_is_valid;
	}
	
	public int GetExpectedAmountOfVariable() {
		return expected_amount_of_variable;
	}

	public int GetActualAmountOfVariable() {
		return actual_amount_of_variable;
	}
	
	@Override
	public String toString() {
		return "statement_is_valid:" + statement_is_valid + ";expected_amount_of_variable:" + expected_amount_of_variable + ";actual_amount_of_variable:" + actual_amount_of_variable;
	}
	
}
