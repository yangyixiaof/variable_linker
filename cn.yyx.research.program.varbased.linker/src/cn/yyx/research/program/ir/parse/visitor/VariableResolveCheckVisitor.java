package cn.yyx.research.program.ir.parse.visitor;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class VariableResolveCheckVisitor extends ASTVisitor {
	
	int variable_amount = 0;
	boolean conflict = false;
	
	public VariableResolveCheckVisitor(int variable_amount) {
		this.variable_amount = variable_amount;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		System.err.println("SimpleName:" + node);
		IBinding nbinding = node.resolveBinding();
		if (nbinding != null) {
			IJavaElement ije = nbinding.getJavaElement();
			// if (ije != null) {
				System.err.println("NullIJE SimpleName:" + node.toString() + ";Element:" + ije);
			// }
		}
		return super.visit(node);
	}
	
	public boolean IsConflict() {
		return conflict;
	}
	
}
