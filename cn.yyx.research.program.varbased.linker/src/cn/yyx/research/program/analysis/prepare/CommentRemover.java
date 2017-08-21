package cn.yyx.research.program.analysis.prepare;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

public class CommentRemover extends ASTVisitor {
	
	ASTRewrite rewrite = null;
	
	public CommentRemover(ASTRewrite rewrite) {
		this.rewrite = rewrite;
	}
	
	@Override
	public void endVisit(Javadoc node) {
		rewrite.remove(node, null);
		super.endVisit(node);
	}
	
	@Override
	public void endVisit(BlockComment node) {
		rewrite.remove(node, null);
		super.endVisit(node);
	}
	
	@Override
	public void endVisit(LineComment node) {
		rewrite.remove(node, null);
		super.endVisit(node);
	}
	
}
