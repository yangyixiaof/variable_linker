package cn.yyx.research.program.ir.storage.node;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.connection.IIRConnection;

public class IIRNode {
	
	private String content = null;
	private Set<IIRConnection> ins = new HashSet<IIRConnection>();
	private Set<IIRConnection> outs = new HashSet<IIRConnection>();
	
	public IIRNode(String content) {
		this.setContent(content);
	}
	
	public void AddInConnection(IIRConnection in_conn) {
		if (!in_conn.getTarget().equals(this)) {
			System.err.println("Strange! target of in_conn is not this.");
		}
		ins.add(in_conn);
	}
	
	public void AddOutConnection(IIRConnection out_conn) {
		if (!out_conn.getSource().equals(this)) {
			System.err.println("Strange! target of in_conn is not this.");
		}
		outs.add(out_conn);
	}

	public String GetContent() {
		return content;
	}

	private void setContent(String content) {
		this.content = content;
	}
	
}
