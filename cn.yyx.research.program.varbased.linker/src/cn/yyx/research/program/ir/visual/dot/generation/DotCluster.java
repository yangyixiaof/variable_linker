package cn.yyx.research.program.ir.visual.dot.generation;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.node.IIRNode;

public class DotCluster {
	
	private Set<IIRNode> ivns = new HashSet<IIRNode>();
	private Set<IIRConnection> ivn_conns = new HashSet<IIRConnection>();
	
	public DotCluster(IIRNode ivn) {
		GetIvns().add(ivn);
	}
	
	public void AddIVNode(IIRNode ivn) {
		GetIvns().add(ivn);
	}
	
	public void AddIVConnection(IIRConnection ivn_conn) {
		GetIvnConns().add(ivn_conn);
	}
	
	public void Merge(DotCluster cluster) {
		GetIvns().addAll(cluster.GetIvns());
		GetIvnConns().addAll(cluster.GetIvnConns());
	}

	public Set<IIRNode> GetIvns() {
		return ivns;
	}

	public Set<IIRConnection> GetIvnConns() {
		return ivn_conns;
	}
	
	@Override
	public String toString() {
		return "nodes:" + ivns + ";conns:" + ivn_conns;
	}
	
}
