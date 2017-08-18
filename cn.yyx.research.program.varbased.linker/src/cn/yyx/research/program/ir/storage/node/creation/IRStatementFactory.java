package cn.yyx.research.program.ir.storage.node.creation;

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
	
	public IRSourceMethodStatementNode CreateIRSourceMethodStatementNode(int variable_index, Collection<IMethod> methods) {
		IRSourceMethodStatementNode result = new IRSourceMethodStatementNode(variable_index, methods);
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
	
	public IRNoneSucceedNode CreateIRNoneSucceedNode() {
		IRNoneSucceedNode result = new IRNoneSucceedNode(IRStatementMeta.NoneSucceed);
		statements.add(result);
		return result;
	}
	
	public IRStatementNode CreateIRStatementNode(int variable_index) {
		IRStatementNode result = new IRStatementNode(variable_index);
		statements.add(result);
		return result;
	}
	
	public void CreateStatementNode() {
		
	}
	
//	public IRStatementNode UniversalStatement(IRStatementNode irsn) {
		// IRJavaElement irje
		// String content = irje.getElement().toString();
//		statements.add(irsn);
//		return irsn;
//	}
	
}
