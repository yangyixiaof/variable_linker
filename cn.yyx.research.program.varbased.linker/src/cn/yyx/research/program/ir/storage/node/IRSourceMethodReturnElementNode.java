package cn.yyx.research.program.ir.storage.node;

public class IRSourceMethodReturnElementNode extends IRJavaElementNode {
	
	private IRSourceMethodStatementNode irsmsn = null;
	
	public IRSourceMethodReturnElementNode(IRSourceMethodStatementNode irsmsn) {
		super(null);
		this.SetIRSourceMethodStatementNode(irsmsn);
	}

	public IRSourceMethodStatementNode GetIRSourceMethodStatementNode() {
		return irsmsn;
	}

	private void SetIRSourceMethodStatementNode(IRSourceMethodStatementNode irsmsn) {
		this.irsmsn = irsmsn;
	}
	
	@Override
	public String toString() {
		return "SMReturnNode:" + super.toString() + ";SM:" + irsmsn.toString();
	}
	
}
