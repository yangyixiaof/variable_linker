package cn.yyx.research.program.ir.storage.node;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;

public class IRMethodInvokeReturnElementNode extends IRJavaElementNode {
	
	// unlinked statically.
	protected List<IIRNode> argument_stmts = new LinkedList<IIRNode>();
	
	public IRMethodInvokeReturnElementNode(String content, IJavaElement ele) {
		super(content, ele);
	}
	
	public void AddIRNode(IIRNode argument) {
		argument_stmts.add(argument);
	}
	
	public List<IIRNode> GetArguments() {
		return argument_stmts;
	}
	
}
