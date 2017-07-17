package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.node.IIRNode;

public class ASTNodeHandledInfo {
	
	int element_idx = -1;
	IIRNode iirn = null;
	
	public ASTNodeHandledInfo(int element_idx, IIRNode iirn) {
		this.element_idx = element_idx;
		this.iirn = iirn;
	}
	
	public int GetElementIndex() {
		return element_idx;
	}
	
	public IIRNode GetIIRNode() {
		return iirn;
	}
	
}
