package cn.yyx.research.program.ir.storage.connection;

import cn.yyx.research.program.ir.storage.node.IIRNode;

public class IIRConnection {
	
	private IIRNode source = null;
	private IIRNode target = null;
	
	public IIRConnection(IIRNode source, IIRNode target) {
		this.setSource(source);
		this.setTarget(target);
	}

	public IIRNode getSource() {
		return source;
	}

	public void setSource(IIRNode source) {
		this.source = source;
	}

	public IIRNode getTarget() {
		return target;
	}

	public void setTarget(IIRNode target) {
		this.target = target;
	}
	
}
