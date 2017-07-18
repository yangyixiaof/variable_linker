package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.node.IIRNode;

public class ASTNodeHandledInfo {
	
	int element_idx = -1;
	String hdoc = null;
	
	public ASTNodeHandledInfo(int element_idx, String hdoc) {
		this.element_idx = element_idx;
		this.hdoc = hdoc;
	}
	
	public int GetElementIndex() {
		return element_idx;
	}
	
	public String GetNodeHandledDoc() {
		return hdoc;
	}
	
}
