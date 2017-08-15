package cn.yyx.research.program.ir.storage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.FlowConnect;
import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodParamElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodReturnElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class IRGraph {
	
	private IIRNode root = null;
	private IIRNode active = null;
	
	private Set<IRSourceMethodStatementNode> source_method_statements = new HashSet<IRSourceMethodStatementNode>();
	private Set<IRSourceMethodReturnElementNode> source_method_returns = new HashSet<IRSourceMethodReturnElementNode>();
	private Set<IRSourceMethodParamElementNode> source_method_params = new HashSet<IRSourceMethodParamElementNode>();
	// TODO control_out_nodes is not set.
	private Set<IIRNode> control_out_nodes = new HashSet<IIRNode>();
	private Set<IRJavaElementNode> variable_nodes = new HashSet<IRJavaElementNode>();
	
	public IRGraph() {
	}
	
	public Set<IRJavaElementNode> GetAddVariableNodes() {
		return variable_nodes;
	}
	
	public void AddNonVirtualVariableNode(IRJavaElementNode var) {
		variable_nodes.add(var);
	}
	
	public void setActive(IIRNode active) {
		this.active = active;
	}
	
	public void AddControlOutNodes(IIRNode iirn) {
		control_out_nodes.add(iirn);
	}
	
	public void AddSourceMethodStatement(IRSourceMethodStatementNode irsmren) {
		source_method_statements.add(irsmren);
	}
	
	public void AddSourceMethodReturn(IRSourceMethodReturnElementNode irsmren) {
		source_method_returns.add(irsmren);
	}
	
	public void AddSourceMethodParam(IRSourceMethodParamElementNode iesmpe) {
		source_method_params.add(iesmpe);
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

	public void MergeNodesToOne(Set<IRStatementNode> wait_merge_nodes, IIRNode new_node) {
		Iterator<IRStatementNode> witr = wait_merge_nodes.iterator();
		while (witr.hasNext()) {
			IRStatementNode irsn = witr.next();
			Collection<IIRConnection> in_conns = irsn.GetAllInConnections();
			Iterator<IIRConnection> iitr = in_conns.iterator();
			while (iitr.hasNext()) {
				IIRConnection iir_conn = iitr.next();
				iir_conn.getSource().RemoveOutConnection(iir_conn);
				iir_conn.getTarget().RemoveInConnection(iir_conn);
				IIRConnection new_iir_conn = new IIRConnection(iir_conn);
				new_iir_conn.setTarget(new_node);
				new_iir_conn.getSource().AddOutConnection(new_iir_conn);
				new_iir_conn.getTarget().AddInConnection(new_iir_conn);
			}
		}
	}
	
}
