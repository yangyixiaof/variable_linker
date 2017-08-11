package cn.yyx.research.program.ir.storage.node;

import org.eclipse.jdt.core.IJavaElement;

public class IRSourceMethodReturnElementNode extends IRJavaElementNode {
	
	private IRSourceMethodStatementNode irsmsn = null;
	
	public IRSourceMethodReturnElementNode(IRSourceMethodStatementNode irsmsn, String content, IJavaElement ele) {
		super(content, ele);
		this.SetIRSourceMethodStatementNode(irsmsn);
	}

	public IRSourceMethodStatementNode GetIRSourceMethodStatementNode() {
		return irsmsn;
	}

	private void SetIRSourceMethodStatementNode(IRSourceMethodStatementNode irsmsn) {
		this.irsmsn = irsmsn;
	}
	
}
