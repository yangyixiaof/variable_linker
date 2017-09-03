package cn.yyx.research.program.ir.storage.node.factory;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IMethod;

import cn.yyx.research.program.ir.meta.IRStatementMeta;
import cn.yyx.research.program.ir.parse.IRStatementParser;
import cn.yyx.research.program.ir.parse.structure.IRStatementCheckResult;
import cn.yyx.research.program.ir.storage.node.IIRBlockOverNode;
import cn.yyx.research.program.ir.storage.node.IIRBranchOverNode;
import cn.yyx.research.program.ir.storage.node.IRNoneSucceedNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

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

	public void RefineSelf() {
		List<IRStatementNode> statements_copy = new LinkedList<IRStatementNode>();
		Iterator<IRStatementNode> nue_itr = statements.iterator();
		while (nue_itr.hasNext()) {
			IRStatementNode irjen = nue_itr.next();
			if (irjen.IsIsolate()) {
			} else {
				statements_copy.add(irjen);
			}
		}
		statements.clear();
		statements.addAll(statements_copy);
		statements_copy.clear();
	}
	
	public void CheckEveryStatement() {
		Iterator<IRStatementNode> sitr = statements.iterator();
		while (sitr.hasNext()) {
			IRStatementNode irsn = sitr.next();
			IRStatementCheckResult check_result = IRStatementParser.CheckTheStatementContainsRightAmountOfVariables(new IRStatementInfo(irsn.GetVariableIndex(), irsn.GetContent()));
			if (!check_result.IsStatementValid()) {
				System.err.println("Wrong content is: " + irsn.GetContent() + "#expected amount:" + irsn.GetVariableIndex() + "#actual amount:" + check_result.GetActualAmountOfVariable());
				System.exit(1);
			}
		}
	}
	
}
