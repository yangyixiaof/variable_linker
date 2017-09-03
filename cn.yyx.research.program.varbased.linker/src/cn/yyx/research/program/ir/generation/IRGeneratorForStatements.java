package cn.yyx.research.program.ir.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import cn.yyx.research.program.eclipse.jdtutil.ASTRewriteHelper;
import cn.yyx.research.program.ir.ast.ASTSearch;
import cn.yyx.research.program.ir.element.VirtualMethodReturnElement;
import cn.yyx.research.program.ir.generation.structure.ASTNodeHandledInfo;
import cn.yyx.research.program.ir.generation.structure.StatementBranchInfo;
import cn.yyx.research.program.ir.generation.structure.SwitchCaseBlock;
import cn.yyx.research.program.ir.generation.structure.SwitchCaseBlockList;
import cn.yyx.research.program.ir.generation.traversal.task.IRASTNodeTask;
import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.VariableConnect;
import cn.yyx.research.program.ir.storage.graph.IRGraph;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRNoneSucceedNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorForStatements extends ASTVisitor {
	
	protected IJavaProject java_project = null;
	protected IRGraphManager graph_manager = null;
	protected IRElementFactory ele_factory = null;
	protected IRStatementFactory stmt_factory = null;
	protected IRJavaElementNode super_class_element = null;
	protected IRASTNodeTask post_visit_task = new IRASTNodeTask();
	protected IRASTNodeTask pre_visit_task = new IRASTNodeTask();
	protected IRGraph graph = null;
	protected List<ASTNode> forbid_visit = new LinkedList<ASTNode>();
	protected IType it = null;
	protected IMethod im = null;
	protected ICompilationUnit type_declare_resource = null;
	protected CompilationUnit type_declare = null;
	
	public IRGeneratorForStatements(IJavaProject java_project, IRGraph graph, IRGraphManager graph_manager, IRElementFactory ele_factory, IRStatementFactory stmt_factory,
			IRJavaElementNode super_class_element, IType it, IMethod im, ICompilationUnit type_declare_resource, CompilationUnit type_declare) {
		this.java_project = java_project;
		this.graph = graph;
		this.graph_manager = graph_manager;
		this.ele_factory = ele_factory;
		this.stmt_factory = stmt_factory;
		this.super_class_element = super_class_element;
		this.it = it;
		this.im = im;
		this.type_declare_resource = type_declare_resource;
		this.type_declare = type_declare;
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

	protected ASTNodeHandledInfo PreHandleOneASTNode(ASTNode node, int element_index) {
		// Document doc = new Document(node.toString());
		ASTRewrite rewrite = ASTRewrite.create(node.getAST());
		IRStatementNode irsn = stmt_factory.CreateIRStatementNode(element_index);
		IRGeneratorForOneExpression irfoe = new IRGeneratorForOneExpression(java_project, graph_manager, node, rewrite, ele_factory, stmt_factory, graph, irsn, super_class_element, it, im, type_declare_resource, type_declare);
		node.accept(irfoe);
		forbid_visit.add(node);
		
		// testing. debugging.
		// System.err.println("rewrite_error_node:" + node.toString() + ";rewrite:" + rewrite.toString());
		String doc_new = ASTRewriteHelper.GetRewriteContent(node, rewrite, type_declare_resource, type_declare);
		// System.err.println("doc_new:" + doc_new);
//		try {
//			TextEdit edits = rewrite.rewriteAST();
//			edits.apply(doc);
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
		// rewrite.rewriteAST(doc, null);
		irsn.SetContent(doc_new);
		return new ASTNodeHandledInfo(irsn, false);// doc.toString()
	}

	protected void PostHandleOneASTNode(ASTNode node) {
		forbid_visit.remove(node);
	}
	
	// Solved. all mechanisms are wrong.
	// Solved. remember to merge expressions.
	@Override
	public boolean visit(AssertStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		graph.GoForwardAStep(info.GetIRStatementNode());
		return false;
	}

	@Override
	public void endVisit(AssertStatement node) {
		PostHandleOneASTNode(node);
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
			VariableDeclarationFragment vd = fitr.next();
			ASTNodeHandledInfo info = PreHandleOneASTNode(vd, element_idx);
			IRStatementNode irsn = info.GetIRStatementNode();
			element_idx = irsn.GetVariableIndex();
			graph.GoForwardAStep(irsn);
		}
		return false;
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
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		HandleExpressionAsStatement(info);
		return false;
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
	
	protected void HandleBreakContinueInLoopOver(ASTNode node, IIRNode over) {
		Set<IRNoneSucceedNode> none_succeed_nodes = break_continue_wait_handle.remove(node);
		if (none_succeed_nodes != null && none_succeed_nodes.size() > 0) {
			Iterator<IRNoneSucceedNode> nitr = none_succeed_nodes.iterator();
			while (nitr.hasNext()) {
				IRNoneSucceedNode irnsn = nitr.next();
				IRGraph.RegistConnection(irnsn, over, new Connect());
			}
		}
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
			IRNoneSucceedNode iirn = stmt_factory.CreateIRNoneSucceedNode();
			graph.GoForwardAStep(iirn);
			none_succeed_nodes.add(iirn);
		}
	}

	@Override
	public boolean visit(ReturnStatement node) {
		Expression expr = node.getExpression();
		if (expr != null && im != null) {
			IJavaElement ije = new VirtualMethodReturnElement(im.getKey());
			IRJavaElementNode f_return = ele_factory.UniversalElement(ije); // im.getKey(), 
			graph.AddNonVirtualVariableNode(f_return);
			ASTNodeHandledInfo info = PreHandleOneASTNode(expr, 1);
			IRStatementNode iirn = info.GetIRStatementNode();
			iirn.SetContent("V=" + iirn.GetContent() + ";");
			graph.GoForwardAStep(iirn);
			IRGraph.RegistConnection(f_return, iirn, new VariableConnect(1));
		}
		graph.AddControlOutNodes(graph.getActive());
		return false;
	}

	@Override
	public void endVisit(ReturnStatement node) {
		Expression expr = node.getExpression();
		if (expr != null) {
			PostHandleOneASTNode(node);
		}
	}

	protected Map<ASTNode, StatementBranchInfo> statement_branch_map = new HashMap<ASTNode, StatementBranchInfo>();

	protected IIRNode PostHandleMustTwoBranches(ASTNode node) {
		// Solved. handle situation that there are no branches. in which branch_root
		// should directly connect to block_over node.
		IRStatementNode over = stmt_factory.CreateIIRBranchOverNode();
		StatementBranchInfo sbi = statement_branch_map.remove(node);
		List<IIRNode> branches = sbi.GetBranches();
		Iterator<IIRNode> bitr = branches.iterator();
		while (bitr.hasNext()) {
			IIRNode iirn = bitr.next();
			IRGraph.RegistConnection(iirn, over, new Connect());
		}
		if (branches.size() < 2) {
			IIRNode branch_root = sbi.GetBranchRoot();
			IRGraph.RegistConnection(branch_root, over, new Connect());
		}
		graph.setActive(over);
		return over;
	}

	protected IIRNode PostHandleMultiBranches(ASTNode node) {
		// Solved. handle situation that there are no branches. in which branch_root should
		// directly connect to block_over node.
		IRStatementNode over = stmt_factory.CreateIIRBranchOverNode();
		StatementBranchInfo sbi = statement_branch_map.remove(node);
		List<IIRNode> branches = sbi.GetBranches();
		Iterator<IIRNode> bitr = branches.iterator();
		while (bitr.hasNext()) {
			IIRNode iirn = bitr.next();
			IRGraph.RegistConnection(iirn, over, new Connect());
		}
		if (branches.size() == 0) {
			IIRNode branch_root = sbi.GetBranchRoot();
			IRGraph.RegistConnection(branch_root, over, new Connect());
		}
		graph.setActive(over);
		return over;
	}

	protected Map<ASTNode, IIRNode> semantic_block_control = new HashMap<ASTNode, IIRNode>();

	@Override
	public boolean visit(DoStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		IRStatementNode branch_root = info.GetIRStatementNode();
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
		IIRNode over = PostHandleMustTwoBranches(node);
		PostHandleOneASTNode(node.getExpression());
		HandleBreakContinueInLoopOver(node, over);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		ASTNodeHandledInfo info = null;
		StringBuilder enh_for = new StringBuilder("for (");
		Set<IRStatementNode> wait_merge_nodes = new HashSet<IRStatementNode>();
		SingleVariableDeclaration param = node.getParameter();
		Type t = param.getType();
		info = PreHandleOneASTNode(t, 0);
		wait_merge_nodes.add(info.GetIRStatementNode());
		enh_for.append(info.GetIRStatementNode().GetContent());
		SimpleName name = param.getName();
		info = PreHandleOneASTNode(name, info.GetIRStatementNode().GetVariableIndex());
		wait_merge_nodes.add(info.GetIRStatementNode());
		enh_for.append(" " + info.GetIRStatementNode().GetContent() + ":");
		Expression expr = node.getExpression();
		info = PreHandleOneASTNode(expr, info.GetIRStatementNode().GetVariableIndex());
		wait_merge_nodes.add(info.GetIRStatementNode());
		enh_for.append(info.GetIRStatementNode().GetContent() + ") {}");
		
		IRStatementNode branch_root = stmt_factory.CreateIRStatementNode(info.GetIRStatementNode().GetVariableIndex());
		branch_root.SetContent(enh_for.toString());
		semantic_block_control.put(node, branch_root);
		IRGraph.MergeNodesToOne(wait_merge_nodes, branch_root);
		graph.GoForwardAStep(branch_root);
		StatementBranchInfo sbi = new StatementBranchInfo(branch_root);
		statement_branch_map.put(node, sbi);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(EnhancedForStatement node) {
		IIRNode branch_root = semantic_block_control.remove(node);
		IIRNode active = graph.getActive();
		if (!active.equals(branch_root)) {
			statement_branch_map.get(node).AddBranch(active);
		}
		IIRNode over = PostHandleMustTwoBranches(node);
		SingleVariableDeclaration param = node.getParameter();
		Type t = param.getType();
		PostHandleOneASTNode(t);
		SimpleName name = param.getName();
		PostHandleOneASTNode(name);
		Expression expr = node.getExpression();
		PostHandleOneASTNode(expr);
		HandleBreakContinueInLoopOver(node, over);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(ForStatement node) {
		StringBuilder for_builder = new StringBuilder("for (");
		int element_index = 0;
		Set<IRStatementNode> wait_merge_nodes = new HashSet<IRStatementNode>();
		List<Expression> inis = node.initializers();
		if (inis != null && inis.size() > 0) {
			Iterator<Expression> expr_itr = inis.iterator();
			while (expr_itr.hasNext()) {
				Expression expr = expr_itr.next();
				ASTNodeHandledInfo info = PreHandleOneASTNode(expr, element_index);
				wait_merge_nodes.add(info.GetIRStatementNode());
				for_builder.append(info.GetIRStatementNode().GetContent());
				if (expr_itr.hasNext()) {
					for_builder.append(",");
				}
				element_index = info.GetIRStatementNode().GetVariableIndex();
			}
		}
		for_builder.append(";");
		
		Expression judge_expr = node.getExpression();
		if (judge_expr != null) {
			ASTNodeHandledInfo info = PreHandleOneASTNode(judge_expr, element_index);
			wait_merge_nodes.add(info.GetIRStatementNode());
			for_builder.append(info.GetIRStatementNode().GetContent());
			element_index = info.GetIRStatementNode().GetVariableIndex();
		}
		for_builder.append(";");
		
		List<Expression> upds = node.updaters();
		if (upds != null && upds.size() > 0) {
			Iterator<Expression> expr_itr = upds.iterator();
			while (expr_itr.hasNext()) {
				Expression expr = expr_itr.next();
				ASTNodeHandledInfo info = PreHandleOneASTNode(expr, element_index);
				wait_merge_nodes.add(info.GetIRStatementNode());
				for_builder.append(info.GetIRStatementNode().GetContent());
				if (expr_itr.hasNext()) {
					for_builder.append(",");
				}
				element_index = info.GetIRStatementNode().GetVariableIndex();
			}
		}
		for_builder.append(") {}");
		
		IRStatementNode branch_root = stmt_factory.CreateIRStatementNode(element_index);
		branch_root.SetContent(for_builder.toString());
		semantic_block_control.put(node, branch_root);
		IRGraph.MergeNodesToOne(wait_merge_nodes, branch_root);
		graph.GoForwardAStep(branch_root);
		StatementBranchInfo sbi = new StatementBranchInfo(branch_root);
		statement_branch_map.put(node, sbi);
		
		return false;
	}
	
	@Override
	public void endVisit(ForStatement node) {
		IIRNode branch_root = semantic_block_control.remove(node);
		IIRNode active = graph.getActive();
		if (!active.equals(branch_root)) {
			statement_branch_map.get(node).AddBranch(active);
		}
		IIRNode over = PostHandleMustTwoBranches(node);
		PostHandleOneASTNode(node.getExpression());
		HandleBreakContinueInLoopOver(node, over);
	}

	@Override
	public boolean visit(WhileStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		// String hdoc = info.GetNodeHandledDoc();
		// IIRNode branch_root = new IIRNode("while (" + hdoc + ") {}");
		IRStatementNode iirn = info.GetIRStatementNode();
		iirn.SetContent("while (" + iirn.GetContent() + ") {}");
		semantic_block_control.put(node, iirn);
		graph.GoForwardAStep(iirn);
		StatementBranchInfo sbi = new StatementBranchInfo(iirn);
		statement_branch_map.put(node, sbi);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(WhileStatement node) {
		IIRNode branch_root = semantic_block_control.remove(node);
		IIRNode active = graph.getActive();
		if (!active.equals(branch_root)) {
			statement_branch_map.get(node).AddBranch(active);
		}
		IIRNode over = PostHandleMustTwoBranches(node);
		PostHandleOneASTNode(node.getExpression());
		HandleBreakContinueInLoopOver(node, over);
	}

	@Override
	public boolean visit(IfStatement node) {
//		if (node.toString().startsWith("if (x")) {
//			System.err.println("heihei");
//		}
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		// String hdoc = info.GetNodeHandledDoc();
		// IIRNode branch_root = new IIRNode("if (" + hdoc + ") {}");
		IRStatementNode branch_root = info.GetIRStatementNode();
		// System.err.println("If_Node:" + node + ";branch_root:" + branch_root.GetContent());
		branch_root.SetContent("if (" + branch_root.GetContent() + ") {}");
		semantic_block_control.put(node, branch_root);
		graph.GoForwardAStep(branch_root);
		StatementBranchInfo sbi = new StatementBranchInfo(branch_root);
		statement_branch_map.put(node, sbi);
		
		graph.setActive(branch_root);
		Statement then_stmt = node.getThenStatement();
		if (then_stmt != null) {
			then_stmt.accept(this);
			forbid_visit.add(then_stmt);
			IIRNode active = graph.getActive();
			if (!branch_root.equals(active)) {
				sbi.AddBranch(active);
			}
		}
		
		graph.setActive(branch_root);
		Statement else_stmt = node.getElseStatement();
		if (else_stmt != null) {
			else_stmt.accept(this);
			forbid_visit.add(else_stmt);
			IIRNode active = graph.getActive();
			if (!branch_root.equals(active)) {
				sbi.AddBranch(active);
			}
		}
		return super.visit(node);
	}
	
	@Override
	public void endVisit(IfStatement node) {
		PostHandleMustTwoBranches(node);
		Statement then_stmt = node.getThenStatement();
		if (then_stmt != null) {
			forbid_visit.add(then_stmt);
		}
		Statement else_stmt = node.getElseStatement();
		if (else_stmt != null) {
			forbid_visit.add(else_stmt);
		}
	}

	// @Override
	// public boolean visit(SwitchCase node) {
	// do nothing.
	// return super.visit(node);
	// }

	protected SwitchCaseBlockList SearchForBranchBlocks(SwitchStatement node) {
		SwitchCaseBlockList scbl = new SwitchCaseBlockList();
		@SuppressWarnings("unchecked")
		List<Statement> stmts = node.statements();
		Iterator<Statement> sitr = stmts.iterator();
		Statement previous = null;
		SwitchCaseBlock scb = null;
		while (sitr.hasNext()) {
			Statement stmt = sitr.next();
			if (stmt instanceof SwitchCase) {
				if (previous != null) {
					scb.AddStatement(previous);
				}
				scb = new SwitchCaseBlock();
				scbl.AddSwitchBlock(scb);
			}
			scb.AddStatement(stmt);
			previous = stmt;
		}
		if (previous != null) {
			scb.AddStatement(previous);
		}
		return scbl;
	}

	@Override
	public boolean visit(SwitchStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		// String hdoc = info.GetNodeHandledDoc();
		// IIRNode branch_root = new IIRNode("switch(" + hdoc + ")" + "{}");
		IRStatementNode branch_root = info.GetIRStatementNode();
		branch_root.SetContent("switch(" + branch_root.GetContent() + ")" + "{}");
		semantic_block_control.put(node, branch_root);
		graph.GoForwardAStep(branch_root);
		StatementBranchInfo sbi = new StatementBranchInfo(branch_root);
		statement_branch_map.put(node, sbi);
		
		SwitchCaseBlockList scbl = SearchForBranchBlocks(node);
		List<SwitchCaseBlock> sbs = scbl.GetSwitchBlocks();
		{
			Iterator<SwitchCaseBlock> sbitr = sbs.iterator();
			while (sbitr.hasNext()) {
				SwitchCaseBlock scb = sbitr.next();
				LinkedList<Statement> stmts = scb.GetStatementList();
				if (stmts.size() > 0) {
					graph.setActive(branch_root);
					Iterator<Statement> stmt_itr = stmts.iterator();
					while (stmt_itr.hasNext()) {
						Statement stmt = stmt_itr.next();
						stmt.accept(this);
						forbid_visit.add(stmt);
					}
					IIRNode active = graph.getActive();
					if (!branch_root.equals(active)) {
						sbi.AddBranch(active);
					}
				}
			}
		}
		return super.visit(node);
	}
	
	@Override
	public void endVisit(SwitchStatement node) {
		SwitchCaseBlockList scbl = SearchForBranchBlocks(node);
		List<SwitchCaseBlock> sbs = scbl.GetSwitchBlocks();
		{
			Iterator<SwitchCaseBlock> sbitr = sbs.iterator();
			while (sbitr.hasNext()) {
				SwitchCaseBlock scb = sbitr.next();
				LinkedList<Statement> stmts = scb.GetStatementList();
				Iterator<Statement> stmt_itr = stmts.iterator();
				while (stmt_itr.hasNext()) {
					Statement stmt = stmt_itr.next();
					forbid_visit.remove(stmt);
				}
			}
		}
		PostHandleOneASTNode(node);
		PostHandleMultiBranches(node);
	}

	@Override
	public boolean visit(SynchronizedStatement node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node.getExpression(), 0);
		// String hdoc = info.GetNodeHandledDoc();
		// IIRNode branch_root = new IIRNode("synchronized(" + hdoc + ")" + "{}");
		IRStatementNode iirn = info.GetIRStatementNode();
		semantic_block_control.put(node, iirn);
		graph.GoForwardAStep(iirn);
		return super.visit(node);
	}
	
	@Override
	public void endVisit(SynchronizedStatement node) {
		IRStatementNode over = stmt_factory.CreateIIRBlockOverNode();
		graph.GoForwardAStep(over);
	}
	
	@Override
	public boolean visit(EmptyStatement node) {
		// do nothing.
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
	
	public boolean visit(ConstructorInvocation node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		HandleExpressionAsStatement(info);
		return super.visit(node) && false;
	}
	
	@Override
	public void endVisit(ConstructorInvocation node) {
		PostHandleOneASTNode(node);
		super.endVisit(node);
	}

	@Override
	public boolean visit(SuperConstructorInvocation node) {
		ASTNodeHandledInfo info = PreHandleOneASTNode(node, 0);
		HandleExpressionAsStatement(info);
		return super.visit(node) && false;
	}
	
	@Override
	public void endVisit(SuperConstructorInvocation node) {
		PostHandleOneASTNode(node);
	}
	
	protected void HandleExpressionAsStatement(ASTNodeHandledInfo info) {
		if (IRGeneratorHelper.IRStatementContentIsValid(info.GetIRStatementNode().GetContent())) {
			graph.GoForwardAStep(info.GetIRStatementNode());
		} else {
			IRGraph.RemoveConnectionsOfNode(info.GetIRStatementNode());
		}
	}

}
