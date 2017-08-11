package cn.yyx.research.test;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;

public class CompilableTestVisitor extends ASTVisitor {
	
	@Override
	public void preVisit(ASTNode node) {
		System.out.println(node.getClass() + ";" + node);
		super.preVisit(node);
	}
	
}
