package cn.yyx.research.program.ir.storage.node;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;

import cn.yyx.research.program.ir.meta.IRStatementMeta;

public class IRSourceMethodStatementNode extends IRStatementNode {

	protected Collection<IMethod> methods = null;
	protected List<IRStatementNode> argument_stmts = new LinkedList<IRStatementNode>();
	
	public IRSourceMethodStatementNode(int variable_index, Collection<IMethod> methods) {
		super(variable_index);
		this.SetContent(IRStatementMeta.SourceMethodInvoke);
		this.methods = methods;
	}
	
	public Collection<IMethod> GetMethods() {
		return methods;
	}
	
	public void AddArgumentStatement(IRStatementNode irsn) {
		argument_stmts.add(irsn);
	}
	
	public List<IRStatementNode> GetArgumentStatements() {
		return argument_stmts;
	}
	
}
