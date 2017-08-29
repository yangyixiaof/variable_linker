package cn.yyx.research.program.ir.storage.node;

public class IRStatementNode extends IIRNode {
	
	int variable_index = 0; // also length for number of variables in a statement.
	
	public IRStatementNode(int variable_index) {
		super("");
		this.variable_index = variable_index;
	}
	
	public int GetVariableIndex() {
		return variable_index;
	}
	
	public int IncreaseAndGetVariableIndex() {
		return ++variable_index;
	}
	
	@Override
	public String toString() {
		return super.toString() + ";VIndex:" + variable_index;
	}
	
}
