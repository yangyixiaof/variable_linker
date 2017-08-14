package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class ASTNodeHandledInfo {
	
	int element_idx = -1;
	IRStatementNode irsn = null;
//	String hdoc = null;
	
	public ASTNodeHandledInfo(int element_idx, IRStatementNode irsn) {
//		String hdoc
		this.element_idx = element_idx;
		this.irsn = irsn;
//		this.hdoc = hdoc;
	}
	
	public int GetElementIndex() {
		return element_idx;
	}
	
//	public String GetNodeHandledDoc() {
//		return hdoc;
//	}
	
}
