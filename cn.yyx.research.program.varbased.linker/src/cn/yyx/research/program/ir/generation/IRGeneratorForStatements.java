package cn.yyx.research.program.ir.generation;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

import cn.yyx.research.program.analysis.prepare.ParameterizedTypeEliminator;
import cn.yyx.research.program.ir.generation.traversal.task.IRASTNodeTask;
import cn.yyx.research.program.ir.storage.IRGraph;

public class IRGeneratorForStatements extends ASTVisitor {
	
	protected IRASTNodeTask post_visit_task = new IRASTNodeTask();
	protected IRASTNodeTask pre_visit_task = new IRASTNodeTask();
	protected IRGraph graph = new IRGraph();
	
	public IRGeneratorForStatements() {
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		pre_visit_task.ProcessAndRemoveTask(node);
	}
	
	@Override
	public void postVisit(ASTNode node) {
		post_visit_task.ProcessAndRemoveTask(node);
		super.postVisit(node);
	}
	
	@Override
	public boolean visit(AssertStatement node) {
		Document doc = new Document(node.toString());
		final ASTRewrite rewrite = ASTRewrite.create(node.getAST());
		node.accept(new ParameterizedTypeEliminator(rewrite));
		// TextEdit edits = ;
		rewrite.rewriteAST(doc, null);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(Block node) {
		// do nothing.
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BreakStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ConstructorInvocation node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(DoStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EmptyStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ExpressionStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ForStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(IfStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(LabeledStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SwitchCase node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SwitchStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SynchronizedStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ThrowStatement node) {
		// do nothing.
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TryStatement node) {
		// do nothing.
		return super.visit(node);
	}
	
	@Override
	public boolean visit(TypeDeclarationStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(WhileStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
}