package cn.yyx.research.program.ir.storage;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.FlowConnect;
import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodInvocation;

public class IRGraph {
	
	private IIRNode root = null;
	private IIRNode active = null;
	Set<IRSourceMethodInvocation> source_method_invokes = new HashSet<IRSourceMethodInvocation>();
	
	public IRGraph() {
	}

	public IIRNode getActive() {
		return active;
	}
	
	public IIRNode getRoot() {
		return root;
	}

	public void RegistConnection(IIRNode source, IIRNode target, Connect connect) {
		IIRConnection irc = null;
		if (!target.HasInConnection(source) && !source.HasOutConnection(target)) {
			irc = new IIRConnection(source, target);
			irc.AddConnect(connect);
			target.AddInConnection(irc);
			source.AddOutConnection(irc);
		} else {
			if (!(target.HasInConnection(source) && source.HasOutConnection(target))) {
				System.err.println("inconsistent node connection!");
				System.exit(1);
			}
			irc = target.GetInConnection(source);
			irc.AddConnect(connect);
		}
	}

	public void GoForwardAStep(IIRNode iirn) {
		RegistConnection(active, iirn, new FlowConnect());
		active = iirn;
	}
	
}
