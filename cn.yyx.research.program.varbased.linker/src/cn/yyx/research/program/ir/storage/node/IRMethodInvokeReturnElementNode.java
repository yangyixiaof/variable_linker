package cn.yyx.research.program.ir.storage.node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;

public class IRMethodInvokeReturnElementNode extends IRJavaElementNode {
	
	// unlinked statically.
	protected List<IIRNode> argument_stmts = new LinkedList<IIRNode>();
	protected Set<IMethod> implementation_methods = new HashSet<IMethod>();
	
	public IRMethodInvokeReturnElementNode(String content, IJavaElement ele) {
		super(content, ele);
	}
	
	public void AddIMethods(Set<IMethod> implementation_methods) {
		this.implementation_methods.addAll(implementation_methods);
	}
	
	public void AddIRNode(IIRNode argument) {
		argument_stmts.add(argument);
	}
	
	public List<IIRNode> GetArguments() {
		return argument_stmts;
	}
	
}
