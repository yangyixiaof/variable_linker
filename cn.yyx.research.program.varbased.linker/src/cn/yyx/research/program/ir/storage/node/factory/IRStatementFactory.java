package cn.yyx.research.program.ir.storage.node.factory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;

import cn.yyx.research.program.ir.meta.IRStatementMeta;
import cn.yyx.research.program.ir.storage.node.IIRBlockOverNode;
import cn.yyx.research.program.ir.storage.node.IIRBranchOverNode;
import cn.yyx.research.program.ir.storage.node.IRNoneSucceedNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class IRStatementFactory {
	
	List<IRStatementNode> statements = new LinkedList<IRStatementNode>();
	
	public IRStatementFactory() {
	}
	
	public IRSourceMethodStatementNode CreateIRSourceMethodStatementNode(Collection<IMethod> methods) {
		// int variable_index, 
		IRSourceMethodStatementNode result = new IRSourceMethodStatementNode(methods); // variable_index, 
		statements.add(result);
		return result;
	}
	
	public IIRBlockOverNode CreateIIRBlockOverNode() {
		IIRBlockOverNode result = new IIRBlockOverNode(IRStatementMeta.VirtualBlockOver);
		statements.add(result);
		return result;
	}
	
	public IIRBranchOverNode CreateIIRBranchOverNode() {
		IIRBranchOverNode result = new IIRBranchOverNode(IRStatementMeta.VirtualBranchOver);
		statements.add(result);
		return result;
	}
	
	public IRNoneSucceedNode CreateIRNoneSucceedNode(String code) {
		IRNoneSucceedNode result = new IRNoneSucceedNode(code + ";");
		statements.add(result);
		return result;
	}
	
	public IRStatementNode CreateIRStatementNode(int variable_index) {
		IRStatementNode result = new IRStatementNode(variable_index);
		statements.add(result);
		return result;
	}
	
	public Collection<IRStatementNode> GetStatements() {
		return statements;
	}
	
	public Collection<IRStatementNode> GetAllIRStatementNodes() {
		Collection<IRStatementNode> result = new LinkedList<IRStatementNode>();
		result.addAll(statements);
		return result;
	}
	
}
