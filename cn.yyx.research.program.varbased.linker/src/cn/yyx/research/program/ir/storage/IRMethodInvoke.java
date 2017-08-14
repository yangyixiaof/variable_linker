package cn.yyx.research.program.ir.storage;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IMember;

public class IRMethodInvoke {
	
	List<IMember> invokes = new LinkedList<IMember>();
	
	public IRMethodInvoke(List<IMember> invokes) {
		this.invokes.addAll(invokes);
	}
	
	public List<IMember> GetMethodInvokes() {
		return invokes;
	}
	
}
