package cn.yyx.research.program.ir.storage.node.info;

public class IRStatementInfo {
	
	int variable_index = 0;
	String content = null;
	
	public IRStatementInfo(int variable_index, String content) {
		this.variable_index = variable_index;
		this.content = content;
	}
	
	public int GetAmountOfVariables() {
		return variable_index;
	}
	
	public String GetContent() {
		return content;
	}
	
}
