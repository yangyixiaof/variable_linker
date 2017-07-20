package cn.yyx.research.program.ir.generation.structure;

import java.util.LinkedList;

import org.eclipse.jdt.core.dom.Statement;

public class SwitchCaseBlock {
	
	private LinkedList<Statement> stmt_list = new LinkedList<Statement>();
	
	public SwitchCaseBlock() {
	}
	
	public void AddStatement(Statement one_stmt) {
		stmt_list.add(one_stmt);
	}

	public LinkedList<Statement> GetStatementList() {
		return stmt_list;
	}
	
}
