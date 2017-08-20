package cn.yyx.research.program.ir.storage.node;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.visual.node.IVNode;

public class IIRNode implements IVNode {
	
	private String content = null;
	private Map<IIRNode, IIRConnection> ins = new HashMap<IIRNode, IIRConnection>();
	private Map<IIRNode, IIRConnection> outs = new HashMap<IIRNode, IIRConnection>();
	
	public IIRNode(String content) {
		this.SetContent(content);
	}
	
	public void AddInConnection(IIRConnection in_conn) {
//		if (!in_conn.getTarget().equals(this)) {
//			System.err.println("Strange! target of in_conn is not this.");
//		}
		ins.put(in_conn.getSource(), in_conn);
	}
	
	public void RemoveInConnection(IIRConnection in_conn) {
		ins.remove(in_conn.getSource());
	}
	
	public Collection<IIRConnection> GetAllInConnections() {
		List<IIRConnection> ir_conn_list = new LinkedList<IIRConnection>(ins.values());
		return ir_conn_list;
	}
	
	public IIRConnection GetInConnection(IIRNode source) {
		return ins.get(source);
	}
	
	public boolean HasInConnection(IIRNode source) {
		if (ins.containsKey(source)) {
			return true;
		}
		return false;
	}
	
	public void AddOutConnection(IIRConnection out_conn) {
//		if (!out_conn.getSource().equals(this)) {
//			System.err.println("Strange! target of in_conn is not this.");
//		}
		outs.put(out_conn.getTarget(), out_conn);
	}
	
	public void RemoveOutConnection(IIRConnection out_conn) {
		outs.remove(out_conn.getTarget());
	}
	
	public Collection<IIRConnection> GetAllOutConnections() {
		List<IIRConnection> ir_conn_list = new LinkedList<IIRConnection>(outs.values());
		return ir_conn_list;
	}
	
	public IIRConnection GetOutConnection(IIRNode target) {
		return outs.get(target);
	}
	
	public boolean HasOutConnection(IIRNode target) {
		if (outs.containsKey(target)) {
			return true;
		}
		return false;
	}

	public String GetContent() {
		return content;
	}

	public void SetContent(String content) {
		this.content = content;
	}

	@Override
	public String ToVisual() {
		return content;
	}
	
	@Override
	public String toString() {
		return "Content:" + ToVisual() + "@" + hashCode();
	}
	
}
