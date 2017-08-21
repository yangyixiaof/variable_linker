package cn.yyx.research.program.analysis.prepare;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;

public class CommentRemover extends ASTVisitor {
	
	public CommentRemover() {
	}
	
	@Override
	public boolean visit(Javadoc node) {
		ASTNode alt_root = node.getAlternateRoot();
		if (alt_root != null) {
			alt_root.delete();
		}
		node.delete();
		return super.visit(node) && false;
	}
	
	@Override
	public boolean visit(BlockComment node) {
		ASTNode alt_root = node.getAlternateRoot();
		if (alt_root != null) {
			alt_root.delete();
		}
		node.delete();
		return super.visit(node) && false;
	}
	
	@Override
	public boolean visit(LineComment node) {
		ASTNode alt_root = node.getAlternateRoot();
		if (alt_root != null) {
			alt_root.delete();
		}
		node.delete();
		return super.visit(node) && false;
	}
	
}
