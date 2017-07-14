package cn.yyx.research.program.ir.storage.connection;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.node.IIRNode;

public class IIRConnection {
	
	private IIRNode source = null;
	private IIRNode target = null;
	
	Set<Connect> conns = new HashSet<Connect>();
	
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
	
	public void AddConnect(Connect vc) {
		conns.add(vc);
	}
	
	public Set<Connect> GetAllConnects() {
		return conns;
	}
	
}
