package cn.yyx.research.program.ir.storage.node.creation;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class IRStatementFactory {
	
	List<IRStatementNode> statements = new LinkedList<IRStatementNode>();
	
	public IRStatementFactory() {
	}
	
	public IRStatementNode UniversalStatement(IRStatementNode irsn) {
		// IRJavaElement irje
		// String content = irje.getElement().toString();
		statements.add(irsn);
		return irsn;
	}
	
}
