package cn.yyx.research.program.ir.storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;

public class IRGraphManager {
	
	Map<IMember, Set<IRMethodInvoke>> method_invokes = new HashMap<IMember, Set<IRMethodInvoke>>();
	Map<IType, Set<IMethod>> member_relations = new HashMap<IType, Set<IMethod>>();
	Map<IMember, IRGraph> resource_graphs = new HashMap<IMember, IRGraph>();
	
	public void AddIRGraph(IMember imember, IRGraph graph) {
		resource_graphs.put(imember, graph);
	}
	
	public Collection<IRGraph> GetAllGraphs() {
		return resource_graphs.values();
	}
	
	public IRGraph GetGraphByIMember(IMember imember) {
		return resource_graphs.get(imember);
	}
	
	public void AddMemberRelation(IType it, IMethod im) {
		Set<IMethod> relations = member_relations.get(it);
		if (relations == null) {
			relations = new HashSet<IMethod>();
			member_relations.put(it, relations);
		}
		relations.add(im);
	}
	
	public Set<IMethod> GetMemberRelation(IType it) {
		return member_relations.get(it);
	}
	
	public void AddMemberMethodInvoke(IMember imember, IRMethodInvoke irmi) {
		Set<IRMethodInvoke> invokes = method_invokes.get(imember);
		if (invokes == null) {
			invokes = new HashSet<IRMethodInvoke>();
			method_invokes.put(imember, invokes);
		}
		invokes.add(irmi);
	}
	
	public Set<IRMethodInvoke> GetMemberMethodInvoke(IMember imember) {
		return method_invokes.get(imember);
	}
	
}
