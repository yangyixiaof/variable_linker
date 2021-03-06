package cn.yyx.research.program.ir.generation;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.CreationReference;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionMethodReference;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IntersectionType;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NameQualifiedType;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SuperMethodReference;
import org.eclipse.jdt.core.dom.TypeMethodReference;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import cn.yyx.research.program.eclipse.jdtutil.ASTRewriteHelper;
import cn.yyx.research.program.eclipse.searchutil.EclipseSearchForIMember;
import cn.yyx.research.program.ir.bind.BindingManager;
import cn.yyx.research.program.ir.element.ConstantUniqueElement;
import cn.yyx.research.program.ir.element.IRElementKind;
import cn.yyx.research.program.ir.element.UnSourceResolvedLambdaElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedTypeElement;
import cn.yyx.research.program.ir.parse.ConstantElementParser;
import cn.yyx.research.program.ir.search.IRSearchMethodRequestor;
import cn.yyx.research.program.ir.storage.connection.SuperConnect;
import cn.yyx.research.program.ir.storage.connection.VariableConnect;
import cn.yyx.research.program.ir.storage.graph.IRGraph;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.graph.IRMethodInvoke;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodParamElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodReturnElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorForOneExpression extends ASTVisitor {

	protected IJavaProject java_project = null;
	protected IRGraphManager graph_manager = null;
	protected ASTNode node = null;
	protected AST ast = null;
	protected ASTRewrite rewrite = null;
	protected IRElementFactory ele_factory = null;
	protected IRStatementFactory stmt_factory = null;
	protected IRGraph graph = null;
	protected IRStatementNode iir_stmt_node = null;
	protected IRJavaElementNode super_class_element = null;
	protected List<ASTNode> forbid_visit = new LinkedList<ASTNode>();
	protected IType it = null;
	protected IMethod im = null;
	protected ICompilationUnit type_declare_resource = null;
	protected CompilationUnit type_declare = null;
	
//	protected Set<SimpleName> variable_of_declaration = new HashSet<SimpleName>();

	public IRGeneratorForOneExpression(IJavaProject java_project, IRGraphManager graph_manager, ASTNode node,
			ASTRewrite rewrite, IRElementFactory ele_factory, IRStatementFactory stmt_factory, IRGraph graph, IRStatementNode iir_stmt_node,
			IRJavaElementNode super_class_element, IType it, IMethod im, ICompilationUnit type_declare_resource, CompilationUnit type_declare) {
		this.java_project = java_project;
		this.graph_manager = graph_manager;
		this.node = node;
		this.ast = node.getAST();
		this.rewrite = rewrite;
		this.ele_factory = ele_factory;
		this.stmt_factory = stmt_factory;
		this.graph = graph;
		this.iir_stmt_node = iir_stmt_node;
		this.super_class_element = super_class_element;
		this.it = it;
		this.im = im;
		this.type_declare_resource = type_declare_resource;
		this.type_declare = type_declare;
	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		if (forbid_visit.contains(node)) {
			return super.preVisit2(node) && false;
		}
		return super.preVisit2(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {
		String content = node.toString();
		HandleICommonJavaElement(new ConstantUniqueElement(content), node, IRElementKind.NumericValue.Value());
		// IRConstantMeta.NumberConstant + "$" +
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		String content = node.toString();
		HandleICommonJavaElement(new ConstantUniqueElement(content), node, IRElementKind.NullValue.Value());
		// IRConstantMeta.NullConstant + "$" +
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		String content = node.toString();
		HandleICommonJavaElement(new ConstantUniqueElement(content), node, IRElementKind.CharacterValue.Value());
		// IRConstantMeta.CharConstant + "$" +
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		String content = node.toString();
		HandleICommonJavaElement(new ConstantUniqueElement(content), node, IRElementKind.BooleanValue.Value());
		// IRConstantMeta.BooleanConstant + "$" +
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {
		String content = node.toString();
		HandleICommonJavaElement(new ConstantUniqueElement(StringEscapeUtils.escapeHtml4(content)), node, IRElementKind.StringValue.Value());
		// IRConstantMeta.StringConstant + "$" +
		return super.visit(node);
	}

	// protected void HandleType(IBinding ib, String represent, ASTNode happen) {
	// IJavaElementState source_resolved = HandleBinding(ib, happen);
	// if (source_resolved == IJavaElementState.HandledWrong) {
	// UnSourceResolvedTypeElement ele = new UnSourceResolvedTypeElement(represent);
	// HandleIJavaElement(represent, ele, happen);
	// }
	// }

	@Override
	public boolean visit(AnonymousClassDeclaration node) {
		boolean handled = false;
		ITypeBinding itb = node.resolveBinding();
		if (itb != null) {
			IJavaElement ije = itb.getJavaElement();
			if (ije != null) {
				IType it = (IType) ije;
				HandleITypeElement(it, node); // it.getElementName(), 
				handled = true;
				IRGeneratorForOneClass irgfoc = new IRGeneratorForOneClass(it, java_project, graph, graph_manager,
						ele_factory, stmt_factory, type_declare_resource, type_declare);
				node.accept(irgfoc);
			}
		}
		if (!handled) {
			String content = node.toString();
			HandleITypeElement(new UnSourceResolvedTypeElement(content), node); // content, 
		}
		return super.visit(node) && false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(LambdaExpression node) {
		boolean handled = false;
		IMethodBinding imb = node.resolveMethodBinding();
		if (imb != null) {
			IJavaElement jele = imb.getJavaElement();
			if (jele != null && jele instanceof IMethod) {
				IMethod im = (IMethod) jele;
				HandleIMethodElement(im, node); // im.getElementName(), 
				handled = true;
				// take it as a method.
				List<SingleVariableDeclaration> para_list = node.parameters();
				IRGeneratorHelper.HandleMethodDeclaration(java_project, graph_manager, node.getBody(), ele_factory, stmt_factory, imb, im,
						it, para_list, super_class_element, type_declare_resource, type_declare);
				// IRGeneratorForStatements irgfocb = new IRGeneratorForStatements(java_project,
				// imb, graph_manager, pool, super_class_element);
				// node.getBody().accept(irgfocb);
			}
		}
		if (!handled) {
			String content = node.toString();
			HandleIMethodElement(new UnSourceResolvedLambdaElement(content), node); // content, 
		}
		return super.visit(node) && false;
	}
	
//	@Override
//	public boolean visit(SingleVariableDeclaration node) {
//		variable_of_declaration.add(node.getName());
//		return super.visit(node);
//	}
//	
//	@Override
//	public void endVisit(SingleVariableDeclaration node) {
//		variable_of_declaration.remove(node.getName());
//	}
//	
//	@Override
//	public boolean visit(VariableDeclarationFragment node) {
//		variable_of_declaration.add(node.getName());
//		return super.visit(node);
//	}
//	
//	@Override
//	public void endVisit(VariableDeclarationFragment node) {
//		variable_of_declaration.remove(node.getName());
//	}

	@Override
	public boolean visit(QualifiedName node) {
		boolean is_source_resolved = TreatName(node);
		// if (!TreatName(node)) {
		//	forbid_visit.add(node.getName());
		// }
		return super.visit(node) && !is_source_resolved;
	}

	@Override
	public boolean visit(SimpleName node) {
		boolean is_source_resolved = TreatName(node);
//		if (!TreatName(node)) {
//			String content = node.toString();
//			HandleCommonIJavaElementByTypeSpecifically(new UnSourceResolvedNameElement(content), node, IRElementMeta.NumericValue); // content, 
//		}
//		else {
//			System.err.println("SimpleName:" + node + ";" + node.resolveBinding().getJavaElement());
//		}
		return super.visit(node) && !is_source_resolved;
	}

	protected boolean TreatName(Name node) {
		IBinding ib = node.resolveBinding();
		if (BindingManager.SourceResolvedBinding(ib)) {
			IJavaElement ije = ib.getJavaElement();
			boolean special_handled = false;
//			if (node instanceof SimpleName) {
//				SimpleName sn = (SimpleName)node;
//				if (variable_of_declaration.contains(sn)) {
//					special_handled = true;
//					HandleICommonJavaElement(ije, node, "W" + iir_stmt_node.GetVariableIndex());
//				}
//			}
			if (!special_handled) {
				HandleCommonIJavaElementByTypeSpecifically(ije, node); // , IRElementKind.QualifiedName.Value()
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean visit(FieldAccess node) {
		IVariableBinding ib = node.resolveFieldBinding();
		if (BindingManager.SourceResolvedBinding(ib)) {
			IJavaElement jele = ib.getJavaElement();
			HandleIFieldElement((IField)jele, node); // jele.getElementName(), , "V"
			return super.visit(node) && false;
		}
		// else {
		// String content = node.toString();
		// HandleINameElement(content, new UnSourceResolvedNameElement(content), node);
		// }
		return super.visit(node);
	}

	@Override
	public boolean visit(SuperFieldAccess node) {
		// do not replace the key-word 'super', just add connect to super_node.
		IVariableBinding ib = node.resolveFieldBinding();
		if (BindingManager.SourceResolvedBinding(ib)) {
			IJavaElement jele = ib.getJavaElement();
			HandleIFieldElement((IField)jele, node); // jele.getElementName(), , "V"
			return super.visit(node) && false;
		} else {
			// String content = node.toString();
			// UnSourceResolvedNameElement usrnofae = new
			// UnSourceResolvedNameElement(content);
			// content, usrnofae
			HandleSuperConnect();
		}
		return super.visit(node);
	}

	// AnnotatableType:
	// PrimitiveType
	// SimpleType
	// QualifiedType
	// NameQualifiedType
	// WildcardType
	// ArrayType
	// ParameterizedType
	// UnionType
	// IntersectionType
	//
	// PrimitiveType:
	// { Annotation } byte
	// { Annotation } short
	// { Annotation } char
	// { Annotation } int
	// { Annotation } long
	// { Annotation } float
	// { Annotation } double
	// { Annotation } boolean
	// { Annotation } void
	// ArrayType:
	// Type Dimension { Dimension }
	// SimpleType:
	// { Annotation } TypeName
	// QualifiedType:
	// Type . {Annotation} SimpleName
	// NameQualifiedType:
	// Name . { Annotation } SimpleName
	// WildcardType:
	// { Annotation } ? [ ( extends | super) Type ]
	// ParameterizedType:
	// Type < Type { , Type } >
	// UnionType:
	// Type | Type { | Type }
	// IntersectionType:
	// Type & Type { & Type }

	@Override
	public boolean visit(PrimitiveType node) {
		HandleType(node.resolveBinding(), node);
		return super.visit(node) && false;
	}

	protected boolean HandleType(ITypeBinding ib, ASTNode node) {
		if (BindingManager.SourceResolvedBinding(ib)) {
			IType it = (IType) ib.getJavaElement();
			HandleITypeElement(it, node); // it.getElementName(), 
			return false;
		}
//		else {
//			String content = node.toString();
//			HandleITypeElement(new UnSourceResolvedTypeElement(content), node); // content, 
//		}
		return true;
	}

	@Override
	public boolean visit(SimpleType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(QualifiedType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(NameQualifiedType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(WildcardType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(ArrayType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(ParameterizedType node) {
		ITypeBinding ib = node.getType().resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(UnionType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	@Override
	public boolean visit(IntersectionType node) {
		ITypeBinding ib = node.resolveBinding();
		return super.visit(node) && HandleType(ib, node);
	}

	protected void HandleSuperConnect() {
		// String content, IJavaElement ele
		// IRJavaElementNode irje = pool.UniversalElement(content, ele);
		// graph.AddNonVirtualVariableNode(irje);
		if (super_class_element != null) {
			IRGraph.RegistConnection(super_class_element, iir_stmt_node, new SuperConnect());
		}
	}
	
	protected void HandleCommonIJavaElementByTypeSpecifically(IJavaElement ije, ASTNode node) { // , String symbol
		boolean handle = false;
		if (ije instanceof ConstantUniqueElement) {
			handle = true;
			ConstantUniqueElement cue = (ConstantUniqueElement)ije;
			String represent = cue.getElementName();
			IRElementKind ele_kind = ConstantElementParser.ParseElementKind(represent);
			if (ele_kind == null) {
				System.err.println("Literal processing runs into error. The system will exit to check that problem.");
				System.exit(1);
			}
			HandleICommonJavaElement(cue, node, ele_kind.Value());
		}
		if (ije instanceof IMethod) {
			handle = true;
			HandleIMethodElement((IMethod)ije, node);
		}
		if (ije instanceof IType) {
			handle = true;
			HandleITypeElement((IType)ije, node);
		}
		if (ije instanceof ILocalVariable) {
			handle = true;
			ILocalVariable ilv = (ILocalVariable)ije;
			HandleILocalVariableElement(ilv, node);
		}
		if (ije instanceof IField) {
			handle = true;
			IField ifd = (IField)ije;
			HandleIFieldElement(ifd, node);
		}
		if (!handle) {
			// HandleICommonJavaElement(ije, node, symbol);
			System.err.println("Source_resolved common element is not consant nor IMethod nor IType nor ILocalVariable nor IField.");
			System.exit(1);
		}
	}

	protected void HandleICommonJavaElement(IJavaElement ije, ASTNode node, String symbol) {
		// String content, 
		IRJavaElementNode uni_ele = ele_factory.UniversalElement(ije);
		graph.AddNonVirtualVariableNode(uni_ele);
		IRGraph.RegistConnection(uni_ele, iir_stmt_node,
				new VariableConnect(iir_stmt_node.IncreaseAndGetVariableIndex()));
		rewrite.replace(node, ast.newSimpleName(symbol), null);
	}

//	protected void HandleIConstantElement(ConstantUniqueElement icue, ASTNode node) {
//		// String content, 
//		HandleCommonIJavaElement(icue, node, "C");
//	}

	protected void HandleIMethodElement(IMethod imd, ASTNode node) {
		// String content, 
		HandleICommonJavaElement(imd, node, IRElementKind.Method.Value());
	}
	
	protected void HandleITypeElement(IType it, ASTNode node) {
		HandleICommonJavaElement(it, node, IRElementKind.Type.Value());
	}

	protected void HandleILocalVariableElement(ILocalVariable ilv, ASTNode node) {
		HandleICommonJavaElement(ilv, node, IRElementKind.Variable.Value());
	}

	protected void HandleIFieldElement(IField ifd, ASTNode node) {
		HandleICommonJavaElement(ifd, node, IRElementKind.Variable.Value());
	}

	protected boolean PreHandleMethodInvocation(IMethodBinding imb, ASTNode node, List<Expression> arg_list) {
		boolean is_source_resolved = false;
		if (imb != null) {
			IType decl_type = null;
			if (imb.isConstructor()) {
				ITypeBinding itb = imb.getDeclaringClass();
				IJavaElement jele = itb.getJavaElement();
				if (jele != null && jele instanceof IType) {
					decl_type = (IType) jele;
				}
			}
			IJavaElement ije = imb.getJavaElement();
			if (ije != null) {// && ije instanceof IMethod
				IMethod search_im = (IMethod) ije;
				Collection<IMethod> methods = null;
				try {
					IRSearchMethodRequestor sr = new IRSearchMethodRequestor(java_project, search_im);
					EclipseSearchForIMember search = new EclipseSearchForIMember();
					search.SearchForWhereTheMethodIsConcreteImplementated(search_im, sr);
					methods = sr.GetSourceMethods();
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if (methods != null && methods.size() > 0) {
					is_source_resolved = true;
					// method invocation is source.
					// Solved. handle method invokes (call-graph).
					Iterator<IMethod> mitr = methods.iterator();
					while (mitr.hasNext()) {
						IMethod imd = mitr.next();
						LinkedList<IMember> invokes = new LinkedList<IMember>();
						invokes.add(imd);
						if (imb.isConstructor()) {
							invokes.add(decl_type);
						}
						graph_manager.AddMemberMethodInvoke(im, new IRMethodInvoke(invokes));
					}
					// create the special node for source_method_invocation.
					IRSourceMethodStatementNode irsmsn = stmt_factory.CreateIRSourceMethodStatementNode(methods); // 0, 
					graph.AddSourceMethodStatement(irsmsn);
					// handle argument expressions.
					int index = 0;
					Iterator<Expression> aitr = arg_list.iterator();
					while (aitr.hasNext()) {
						index++;
						Expression expr = aitr.next();
						// Document doc = new Document(expr.toString());
						ASTRewrite expr_rewrite = ASTRewrite.create(expr.getAST());
						IRStatementNode expr_iirn = stmt_factory.CreateIRStatementNode(1);
						IRGeneratorForOneExpression ir_gfoe = new IRGeneratorForOneExpression(java_project,
								graph_manager, expr, expr_rewrite, ele_factory, stmt_factory, graph, expr_iirn, super_class_element, it, im, type_declare_resource, type_declare);
						expr.accept(ir_gfoe);
						String doc_new = ASTRewriteHelper.GetRewriteContent(expr, expr_rewrite, type_declare_resource, type_declare);
//						try {
//							TextEdit edits = expr_rewrite.rewriteAST();
//							edits.apply(doc);
//						} catch (Exception e) {
//							e.printStackTrace();
//							System.exit(1);
//						}
						// expr_rewrite.rewriteAST(doc, null);
						expr_iirn.SetContent("V=" + doc_new + ";"); // doc.get()
						IRSourceMethodParamElementNode irsmpen = ele_factory.CreateIRSourceMethodParamElementNode(irsmsn, index);
						//		new IRSourceMethodParamElementNode(irsmsn, index);
						graph.AddSourceMethodParam(irsmpen);
						IRGraph.RegistConnection(irsmpen, expr_iirn, new VariableConnect(1));
						irsmsn.AddArgumentStatement(expr_iirn);
					}
					// method_node goes forward one step.
					graph.GoForwardAStep(irsmsn);

					// replace node with return element.
					IRSourceMethodReturnElementNode ir_mi_return = new IRSourceMethodReturnElementNode(irsmsn);
					graph.AddSourceMethodReturn(ir_mi_return);
					IRGraph.RegistConnection(ir_mi_return, iir_stmt_node,
							new VariableConnect(iir_stmt_node.IncreaseAndGetVariableIndex()));
					rewrite.replace(node, ast.newSimpleName(IRElementKind.Variable.Value()), null);
				}
			} else {
				if (imb.isConstructor()) {
					LinkedList<IMember> invokes = new LinkedList<IMember>();
					invokes.add(decl_type);
					graph_manager.AddMemberMethodInvoke(im, new IRMethodInvoke(invokes));
				}
			}
		}
		if (!is_source_resolved) {
			return true;
		}
		return false;
	}

	// method_invocation should be handled.
	// method_invocation expressions.
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodInvocation node) {
		List<Expression> nlist = node.arguments();
		super.visit(node);
		return PreHandleMethodInvocation(node.resolveMethodBinding(), node, nlist);
	}

	@Override
	public void endVisit(MethodInvocation node) {
		// do nothing.
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(ClassInstanceCreation node) {
		List<Expression> nlist = node.arguments();
		super.visit(node);
		return PreHandleMethodInvocation(node.resolveConstructorBinding(), node, nlist);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(SuperMethodInvocation node) {
		List<Expression> nlist = node.arguments();
		super.visit(node);
		boolean is_not_source_resolved = PreHandleMethodInvocation(node.resolveMethodBinding(), node, nlist);
		boolean must_continue = is_not_source_resolved;
		if (must_continue) {
			HandleSuperConnect();
		}
		return must_continue;
	}

	// method_invocation statements.
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(ConstructorInvocation node) {
		// if this(...); statement is not resolved, there are no other implementations
		// so ignored it.
		List<Expression> nlist = node.arguments();
		super.visit(node);
		boolean is_not_source_resolved = PreHandleMethodInvocation(node.resolveConstructorBinding(), node, nlist);
		if (is_not_source_resolved) {
			return false;
		}
		return super.visit(node);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		List<Expression> nlist = node.arguments();
		super.visit(node);
		boolean is_not_source_resolved = PreHandleMethodInvocation(node.resolveConstructorBinding(), node, nlist);
		boolean must_continue = is_not_source_resolved;
		if (must_continue) {
			HandleSuperConnect();
		}
		return must_continue;
	}

	// Solved. handle method_reference.

	@Override
	public boolean visit(ExpressionMethodReference node) {
		return HandleMethodReference(node.resolveMethodBinding(), node);
	}

	@Override
	public boolean visit(CreationReference node) {
		return HandleMethodReference(node.resolveMethodBinding(), node);
	}

	@Override
	public boolean visit(TypeMethodReference node) {
		return HandleMethodReference(node.resolveMethodBinding(), node);
	}

	@Override
	public boolean visit(SuperMethodReference node) {
		boolean is_not_source_resolved = HandleMethodReference(node.resolveMethodBinding(), node);
		if (is_not_source_resolved) {
			HandleSuperConnect();
		}
		return is_not_source_resolved;
	}

	protected boolean HandleMethodReference(IMethodBinding imb, MethodReference node) {
		if (BindingManager.SourceResolvedBinding(imb)) {
			IMethod im = (IMethod) imb.getJavaElement();
			HandleIMethodElement(im, node);
			return false;
		}
		// else {
		// String content = node.toString();
		// UnSourceResolvedMethodReferenceElement ele = new
		// UnSourceResolvedMethodReferenceElement(content);
		// HandleIMethodElement(content, ele, node);
		// }
		return true;
	}

}
