package cn.yyx.research.program.ir.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.eclipse.jdt.core.dom.SimpleName;
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

import cn.yyx.research.program.ir.ast.ASTSearch;
import cn.yyx.research.program.ir.generation.structure.ASTNodeHandledInfo;
import cn.yyx.research.program.ir.generation.structure.StatementBranchInfo;
import cn.yyx.research.program.ir.generation.traversal.task.IRASTNodeTask;
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElement;
import cn.yyx.research.program.ir.storage.node.IRNoneSucceedNode;

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
	
	protected Map<String, ASTNode> label_scope = new TreeMap<String, ASTNode>();
	protected Map<ASTNode, Set<IRNoneSucceedNode>> break_continue_wait_handle = new HashMap<ASTNode, Set<IRNoneSucceedNode>>();

	@Override
	public boolean visit(LabeledStatement node) {
		// minor but close relation.
		SimpleName label = node.getLabel();
		if (label != null) {
			label_scope.put(label.toString(), node.getBody());
		}
		return super.visit(node);
	}

	@Override
	public void endVisit(LabeledStatement node) {
		SimpleName label = node.getLabel();
		if (label != null) {
			label_scope.remove(label.toString());
		}
		super.endVisit(node);
	}
	
	@Override
	public boolean visit(BreakStatement node) {
		HandleBreakContinueStatement(node, node.getLabel(), "break");
		return super.visit(node);
	}
	
	@Override
	public boolean visit(ContinueStatement node) {
		HandleBreakContinueStatement(node, node.getLabel(), "continue");
		return super.visit(node);
	}
	
	protected void HandleBreakContinueStatement(ASTNode node, SimpleName label, String code) {
		ASTNode break_scope = null;
		if (label != null) {
			break_scope = label_scope.get(label.toString());
		} else {
			break_scope = ASTSearch.FindMostCloseLoopNode(node);
		}
		if (break_scope != null) {
			Set<IRNoneSucceedNode> none_succeed_nodes = break_continue_wait_handle.get(break_scope);
			if (none_succeed_nodes == null) {
				none_succeed_nodes = new HashSet<IRNoneSucceedNode>();
				break_continue_wait_handle.put(break_scope, none_succeed_nodes);
			}
			IRNoneSucceedNode iirn = new IRNoneSucceedNode(code);
			graph.GoForwardAStep(iirn);
			none_succeed_nodes.add(iirn);
		}
	}
	
	@Override
	public boolean visit(ReturnStatement node) {
		// TODO Auto-generated method stub
		return super.visit(node);
	}
	
	protected Map<ASTNode, StatementBranchInfo> statement_branch_map = new HashMap<ASTNode, StatementBranchInfo>();
	
	protected void PostHandleMustTwoBranches(ASTNode node) {
		// TODO handle situation that there are no branches. in which branch_root should directly connect to block_over node.
		
	}
	
	protected void PostHandleMultiBranches(ASTNode node) {
		// TODO handle situation that there are no branches. in which branch_root should directly connect to block_over node.
		
	}
	
	protected Map<ASTNode, IIRNode> semantic_block_control = new HashMap<ASTNode, IIRNode>();
	
	@Override
	public boolean visit(DoStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		String hdoc = info.GetNodeHandledDoc();
		IIRNode branch_root = new IIRNode(hdoc);
		semantic_block_control.put(node, branch_root);
		graph.GoForwardAStep(branch_root);
		StatementBranchInfo sbi = new StatementBranchInfo(branch_root);
		statement_branch_map.put(node, sbi);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(DoStatement node) {
		IIRNode branch_root = semantic_block_control.remove(node);
		IIRNode active = graph.getActive();
		if (!active.equals(branch_root)) {
			statement_branch_map.get(node).AddBranch(active);
		}
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
	
	// nothing need to be done.
	
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
