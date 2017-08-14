package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class ASTNodeHandledInfo {
	
	int element_idx = -1;
	IRStatementNode irsn = null;
	boolean could_continue = false;
//	String hdoc = null;
	
	public ASTNodeHandledInfo(int element_idx, IRStatementNode irsn, boolean could_continue) {
//		String hdoc
		this.element_idx = element_idx;
		this.irsn = irsn;
		this.could_continue = could_continue;
//		this.hdoc = hdoc;
	}
	
	public int GetElementIndex() {
		return element_idx;
	}
	
	public IRStatementNode GetIRStatementNode() {
		return irsn;
	}
	
	public boolean CouldContinue() {
		return could_continue;
	}
	
//	public String GetNodeHandledDoc() {
//		return hdoc;
//	}
	
}
