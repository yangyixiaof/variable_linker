package cn.yyx.research.program.ir.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

import cn.yyx.research.program.ir.generation.structure.ASTNodeHandledInfo;
import cn.yyx.research.program.ir.generation.traversal.task.IRASTNodeTask;
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElement;

public class IRGeneratorForStatements extends ASTVisitor {
	
	protected IRGraphManager graph_manager = null;
	protected IRElementPool pool = null;
	protected IRJavaElement super_class_element = null;
	protected IRASTNodeTask post_visit_task = new IRASTNodeTask();
	protected IRASTNodeTask pre_visit_task = new IRASTNodeTask();
	protected IRGraph graph = new IRGraph();
	protected List<ASTNode> forbid_visit = new LinkedList<ASTNode>();
	
	public IRGeneratorForStatements(IRGraphManager graph_manager, IRElementPool pool, IRJavaElement super_class_element) {
		this.graph_manager = graph_manager;
		this.pool = pool;
		this.super_class_element = super_class_element;
		this.graph_manager.AddIRGraph(graph);
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		if (forbid_visit.contains(node)) {
			return false;
		}
		return super.preVisit2(node);
	}
	
	@Override
	public void preVisit(ASTNode node) {
		super.preVisit(node);
		pre_visit_task.ProcessAndRemoveTask(node);
	}
	
	@Override
	public void postVisit(ASTNode node) {
		if (forbid_visit.contains(node)) {
			return;
		}
		post_visit_task.ProcessAndRemoveTask(node);
		super.postVisit(node);
	}
	
	protected ASTNodeHandledInfo PreHandleOneASTNode(ASTNode node, int element_idx) {
		Document doc = new Document(node.toString());
		ASTRewrite rewrite = ASTRewrite.create(node.getAST());
		IRGeneratorForOneExpression irfoe = new IRGeneratorForOneExpression(graph_manager, node, rewrite, pool, graph, super_class_element, element_idx);
		forbid_visit.add(node);
		node.accept(irfoe);
		// TextEdit edits = ;
		rewrite.rewriteAST(doc, null);
		return new ASTNodeHandledInfo(irfoe.GetElementIndex(), doc.toString());
	}
	
	protected void PostHandleOneASTNode(ASTNode node) {
		forbid_visit.remove(node);
	}
	
	@Override
	public boolean visit(AssertStatement node) {
		IIRNode iirn = new IIRNode("");
		graph.GoForwardAStep(iirn);
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		iirn.SetContent(info.GetNodeHandledDoc());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(AssertStatement node) {
		PostHandleOneASTNode(node);
		super.endVisit(node);
	}

	@Override
	public boolean visit(Block node) {
		// do nothing.
		return super.visit(node);
	}
	
	@Override
	public boolean visit(VariableDeclarationStatement node) {
		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> frags = node.fragments();
		Iterator<VariableDeclarationFragment> fitr = frags.iterator();
		int element_idx = 0;
		while (fitr.hasNext()) {
			IIRNode iirn = new IIRNode("");
			graph.GoForwardAStep(iirn);
			VariableDeclarationFragment vd = fitr.next();
			ASTNodeHandledInfo info = PreHandleOneASTNode(vd, element_idx);
			element_idx = info.GetElementIndex();
			iirn.SetContent(info.GetNodeHandledDoc());
		}
		return super.visit(node);
	}
	
	@Override
	public void endVisit(VariableDeclarationStatement node) {
		@SuppressWarnings("unchecked")
		List<VariableDeclarationFragment> frags = node.fragments();
		Iterator<VariableDeclarationFragment> fitr = frags.iterator();
		while (fitr.hasNext()) {
			VariableDeclarationFragment vd = fitr.next();
			PostHandleOneASTNode(vd);
		}
	}
	
	@Override
	public boolean visit(ExpressionStatement node) {
		IIRNode iirn = new IIRNode("");
		graph.GoForwardAStep(iirn);
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		iirn.SetContent(info.GetNodeHandledDoc());
		return super.visit(node);
	}
	
	@Override
	public void endVisit(ExpressionStatement node) {
		PostHandleOneASTNode(node);
	}
	
	@Override
	public boolean visit(LabeledStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(BreakStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	private void HandleMustTwoBranches(IIRNode branch_root, List<IIRNode> branches) {
		
	}
	
	private void HandleMultiBranches(IIRNode branch_root, List<IIRNode> branches) {
		
	}
	
	@Override
	public boolean visit(DoStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		IIRNode iirn = info.GetIIRNode();
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EnhancedForStatement node) {
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
	public boolean visit(WhileStatement node) {
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
		// do nothing.
		return super.visit(node);
	}
	
	@Override
	public boolean visit(EmptyStatement node) {
		// do nothing.
		return super.visit(node);
	}
	
}
