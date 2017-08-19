package cn.yyx.research.program.ir.storage.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodParamElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodReturnElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class IRGraph {

	private IRStatementNode root = null;
	private IRStatementNode active = null;
	
	private Set<IRSourceMethodStatementNode> source_method_statements = new HashSet<IRSourceMethodStatementNode>();
	private Set<IRSourceMethodReturnElementNode> source_method_returns = new HashSet<IRSourceMethodReturnElementNode>();
	private Set<IRSourceMethodParamElementNode> source_method_params = new HashSet<IRSourceMethodParamElementNode>();
	// Solved. control_out_nodes is not set.
	private Set<IRStatementNode> control_out_nodes = new HashSet<IRStatementNode>();
	private Set<IRJavaElementNode> variable_nodes = new HashSet<IRJavaElementNode>();

	public IRGraph() {
	}
	
	public Set<IIRNode> GetControlOutNodes() {
		HashSet<IIRNode> result = new HashSet<IIRNode>();
		if (active != null) {
			// add last statement.
			result.add(active);
		}
		result.addAll(control_out_nodes);
		return result;
	}

	public Set<IRSourceMethodStatementNode> GetSourceMethodStatements() {
		return source_method_statements;
	}

	public Set<IRSourceMethodReturnElementNode> GetSourceMethodReturns() {
		return source_method_returns;
	}

	public Set<IRSourceMethodParamElementNode> GetSourceMethodParams() {
		return source_method_params;
	}

	public Set<IRJavaElementNode> GetVariableNodes() {
		return variable_nodes;
	}

	public void AddNonVirtualVariableNode(IRJavaElementNode var) {
		variable_nodes.add(var);
	}

	public void setActive(IRStatementNode active) {
		this.active = active;
	}

	public void AddControlOutNodes(IRStatementNode iirn) {
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

	public IRStatementNode getActive() {
		return active;
	}

	public IRStatementNode getRoot() {
		return root;
	}
	
	public void GoForwardAStep(IRStatementNode iirn) {
		if (active == null) {
			root = iirn;
		} else {
			RegistConnection(active, iirn, new Connect());
		}
		active = iirn;
	}

	public static void RegistConnection(IIRNode source, IIRNode target, Connect connect) {
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

	public static void MergeNodesToOne(Set<IRStatementNode> wait_merge_nodes, IIRNode new_node) {
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

	public static void TransferConnectionFromNodeToNode(IIRNode from, IIRNode to) {
		{
			Collection<IIRConnection> out_conns = from.GetAllOutConnections();
			Iterator<IIRConnection> out_itr = out_conns.iterator();
			while (out_itr.hasNext()) {
				IIRConnection irc = out_itr.next();
				IIRConnection new_irc = new IIRConnection(irc);
				new_irc.setSource(to);
				new_irc.getSource().AddOutConnection(new_irc);
				new_irc.getTarget().AddInConnection(new_irc);
			}
		}
		{
			Collection<IIRConnection> in_conns = from.GetAllInConnections();
			Iterator<IIRConnection> in_itr = in_conns.iterator();
			while (in_itr.hasNext()) {
				IIRConnection irc = in_itr.next();
				IIRConnection new_irc = new IIRConnection(irc);
				new_irc.setTarget(to);
				new_irc.getSource().AddOutConnection(new_irc);
				new_irc.getTarget().AddInConnection(new_irc);
			}
		}
	}

	public static void RemoveConnectionsOfNode(IIRNode to_be_deleted) {
		{
			Collection<IIRConnection> out_conns = to_be_deleted.GetAllOutConnections();
			Iterator<IIRConnection> oitr = out_conns.iterator();
			while (oitr.hasNext()) {
				IIRConnection oirc = oitr.next();
				oirc.getSource().RemoveOutConnection(oirc);
				oirc.getTarget().RemoveInConnection(oirc);
			}
		}
		{
			Collection<IIRConnection> in_conns = to_be_deleted.GetAllInConnections();
			Iterator<IIRConnection> iitr = in_conns.iterator();
			while (iitr.hasNext()) {
				IIRConnection iirc = iitr.next();
				iirc.getSource().RemoveOutConnection(iirc);
				iirc.getTarget().RemoveInConnection(iirc);
			}
		}
	}

}
