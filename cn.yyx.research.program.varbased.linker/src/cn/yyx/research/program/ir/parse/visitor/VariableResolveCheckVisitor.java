package cn.yyx.research.program.ir.parse.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;

public class VariableResolveCheckVisitor extends ASTVisitor {
	
	int variable_amount = 0;
	boolean conflict = false;
	
	public VariableResolveCheckVisitor(int variable_amount) {
		this.variable_amount = variable_amount;
	}
	
	public boolean IsConflict() {
		return conflict;
	}
	
}
