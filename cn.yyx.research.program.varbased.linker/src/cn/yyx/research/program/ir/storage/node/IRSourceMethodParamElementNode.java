package cn.yyx.research.program.ir.storage.node;

public class IRSourceMethodParamElementNode extends IRJavaElementNode {
	
	protected IRSourceMethodStatementNode irsmsn = null;
	protected int param_index = 0;
	
	public IRSourceMethodParamElementNode(IRSourceMethodStatementNode irsmsn, int param_index) {
		// , String content, IJavaElement ele
		super(null, null);
		this.SetIRSourceMethodStatementNode(irsmsn);
		this.SetParamIndex(param_index);
	}

	public IRSourceMethodStatementNode GetIRSourceMethodStatementNode() {
		return irsmsn;
	}

	private void SetIRSourceMethodStatementNode(IRSourceMethodStatementNode irsmsn) {
		this.irsmsn = irsmsn;
	}

	public int GetParamIndex() {
		return param_index;
	}

	private void SetParamIndex(int param_index) {
		this.param_index = param_index;
	}
	
}
