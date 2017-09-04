package cn.yyx.research.program.ir.parse.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class VariableResolveCheckVisitor extends ASTVisitor {
	
	int variable_amount = 0;
	int current_variable_amount = 0;
	
	public VariableResolveCheckVisitor(int variable_amount) {
		this.variable_amount = variable_amount;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		IBinding nbinding = node.resolveBinding();
		// System.err.println("SimpleName:" + node + ";Binding:" + nbinding);
		if (nbinding != null) {
			// IJavaElement ije = nbinding.getJavaElement();
			// if (ije != null) {
			//	System.err.println("NullIJE SimpleName:" + node.toString() + ";Element:" + ije);
			current_variable_amount++;
			// }
		}
		return super.visit(node);
	}
	
	public boolean IsVariableAmountConsistent() {
		return current_variable_amount == variable_amount;
	}
	
	public int GetCurrentVariableAmount() {
		return current_variable_amount;
	}
	
}
