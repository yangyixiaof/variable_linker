package cn.yyx.research.program.ir.generation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.Expression;
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
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.WildcardType;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;

import cn.yyx.research.program.eclipse.searchutil.EclipseSearchForIMember;
import cn.yyx.research.program.ir.IRConstantMeta;
import cn.yyx.research.program.ir.bind.BindingManager;
import cn.yyx.research.program.ir.element.ConstantUniqueElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedLambdaElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedMethodReferenceElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedNameOrFieldAccessElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedTypeElement;
import cn.yyx.research.program.ir.generation.state.IJavaElementState;
import cn.yyx.research.program.ir.search.IRSearchMethodRequestor;
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.connection.SuperConnect;
import cn.yyx.research.program.ir.storage.connection.VariableConnect;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRMethodInvokeReturnElementNode;

public class IRGeneratorForOneExpression extends ASTVisitor {
	
	protected IJavaProject java_project = null;
	protected IRGraphManager graph_manager = null;
	protected ASTNode node = null;
	protected AST ast = null;
	protected ASTRewrite rewrite = null;
	protected IRElementPool pool = null;
	protected IRGraph graph = null;
	protected IIRNode iirn_node = null;
	protected IRJavaElementNode super_class_element = null;
	protected int element_index = 0;
	
	public IRGeneratorForOneExpression(IJavaProject java_project, IRGraphManager graph_manager, ASTNode node, ASTRewrite rewrite, IRElementPool pool, IRGraph graph, IIRNode iirn_node, IRJavaElementNode super_class_element, int base_index) {
		this.java_project = java_project;
		this.graph_manager = graph_manager;
		this.node = node;
		this.ast = node.getAST();
		this.rewrite = rewrite;
		this.pool = pool;
		this.graph = graph;
		this.iirn_node = iirn_node;
		this.super_class_element = super_class_element;
		this.element_index = base_index;
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		String content = node.toString();
		HandleIVariableElement(content, new ConstantUniqueElement(IRConstantMeta.NumberConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		String content = node.toString();
		HandleIVariableElement(content, new ConstantUniqueElement(IRConstantMeta.NullConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		String content = node.toString();
		HandleIVariableElement(content, new ConstantUniqueElement(IRConstantMeta.CharConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		String content = node.toString();
		HandleIVariableElement(content, new ConstantUniqueElement(IRConstantMeta.BooleanConstant + "$" + content), node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		String content = node.toString();
		HandleIVariableElement(content, new ConstantUniqueElement(IRConstantMeta.StringConstant + "$" + content), node);
		return super.visit(node);
	}
	
//	protected void HandleType(IBinding ib, String represent, ASTNode happen) {
//		IJavaElementState source_resolved = HandleBinding(ib, happen);
//		if (source_resolved == IJavaElementState.HandledWrong) {
//			UnSourceResolvedTypeElement ele = new UnSourceResolvedTypeElement(represent);
//			HandleIJavaElement(represent, ele, happen);
//		}
//	}
	
	@Override
	public boolean visit(LambdaExpression node) {
		boolean handled = false;
		IMethodBinding imb = node.resolveMethodBinding();
		if (imb != null) {
			IJavaElement jele = imb.getJavaElement();
			if (jele != null && jele instanceof IMethod) {
				IMethod im = (IMethod) jele;
				HandleIVariableElement(im.toString(), im, node);
				handled = true;
				// take it as a method.
				IRGeneratorForStatements irgfocb = new IRGeneratorForStatements(java_project, imb, graph_manager, pool, super_class_element);
				node.getBody().accept(irgfocb);
			}
		}
		if (!handled) {
			String content = node.toString();
			HandleIVariableElement(content, new UnSourceResolvedLambdaElement(content), node);
		}
		return false;
	}
	
	@Override
	public boolean visit(QualifiedName node) {
		TreatName(node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(SimpleName node) {
		TreatName(node);
		super.visit(node);
		return false;
	}
	
	protected void TreatName(Name node) {
		IBinding ib = node.resolveBinding();
		// IJavaElementState state = null;
		IJavaElementState bind_state = HandleBinding(ib, node);
		if (bind_state == IJavaElementState.HandledWrong) {
			String content = node.toString();
			HandleIVariableElement(content, new UnSourceResolvedNameOrFieldAccessElement(content), node);
		}
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		IVariableBinding ib = node.resolveFieldBinding();
		IJavaElementState state = HandleBinding(ib, node);
		if (state == IJavaElementState.HandledWrong) {
			String content = node.toString();
			HandleIVariableElement(content, new UnSourceResolvedNameOrFieldAccessElement(content), node);
		}
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(SuperFieldAccess node) {
		IVariableBinding ib = node.resolveFieldBinding();
		IJavaElementState state = HandleBinding(ib, node);
		if (state == IJavaElementState.HandledWrong) {
			String content = node.toString();
			UnSourceResolvedNameOrFieldAccessElement usrnofae = new UnSourceResolvedNameOrFieldAccessElement(content);
			HandleSuperConnect(content, usrnofae);
			HandleIVariableElement(content, usrnofae, node);
		}
		super.visit(node);
		return false;
	}
	
//	AnnotatableType:
//	       PrimitiveType
//	       SimpleType
//	       QualifiedType
//	       NameQualifiedType
//	       WildcardType
//	    ArrayType
//	    ParameterizedType
//	    UnionType
//	    IntersectionType
//	    
//	 PrimitiveType:
//	    { Annotation } byte
//	    { Annotation } short
//	    { Annotation } char
//	    { Annotation } int
//	    { Annotation } long
//	    { Annotation } float
//	    { Annotation } double
//	    { Annotation } boolean
//	    { Annotation } void
//	 ArrayType:
//	    Type Dimension { Dimension }
//	 SimpleType:
//	    { Annotation } TypeName
//	 QualifiedType:
//	    Type . {Annotation} SimpleName
//	 NameQualifiedType:
//	    Name . { Annotation } SimpleName
//	 WildcardType:
//	    { Annotation } ? [ ( extends | super) Type ]
//	 ParameterizedType:
//	    Type < Type { , Type } >
//	 UnionType:
//	    Type | Type { | Type }
//	 IntersectionType:
//	    Type & Type { & Type }
	
	@Override
	public boolean visit(PrimitiveType node) {
		HandleBinding(node.resolveBinding(), node);
		super.visit(node);
		return false;
	}
	
	protected void HandleType(ITypeBinding ib, ASTNode node) {
		IJavaElementState state = HandleBinding(ib, node);
		if (state == IJavaElementState.HandledWrong) {
			String content = node.toString();
			HandleIVariableElement(content, new UnSourceResolvedTypeElement(content), node);
		}
	}
	
	@Override
	public boolean visit(SimpleType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(QualifiedType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(NameQualifiedType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(WildcardType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(ArrayType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(ParameterizedType node) {
		ITypeBinding ib = node.getType().resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(UnionType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	@Override
	public boolean visit(IntersectionType node) {
		ITypeBinding ib = node.resolveBinding();
		HandleType(ib, node);
		super.visit(node);
		return false;
	}
	
	protected IJavaElementState HandleBinding(IBinding ib, ASTNode node) {
		if (!BindingManager.QualifiedBinding(ib)) {
			return IJavaElementState.HandledWrong;
		}
		IJavaElement jele = ib.getJavaElement();
		HandleIVariableElement(jele.getElementName(), jele, node);
		return IJavaElementState.HandledSuccessful;
	}
	
	protected void HandleMethodReference(IMethodBinding imb, MethodReference node) {
		IMethod im = null;
		if (imb != null) {
			IJavaElement jele = imb.getJavaElement();
			if (jele != null && jele instanceof IMethod) {
				im = (IMethod) jele;
				if (im.getDeclaringType().isBinary()) {
					im = null;
				}
			}
		}
		if (im != null) {
			HandleIVariableElement(im.toString(), im, node);
		} else {
			String content = node.toString();
			UnSourceResolvedMethodReferenceElement ele = new UnSourceResolvedMethodReferenceElement(content);
			HandleSuperConnect(content, ele);
			HandleIVariableElement(content, ele, node);
		}
	}
	
	protected void HandleSuperConnect(String content, IJavaElement ele) {
		IRJavaElementNode irje = pool.UniversalElement(content, ele);
		graph.AddVariableNode(irje);
		if (super_class_element != null) {
			graph.RegistConnection(irje, super_class_element, new SuperConnect());
		}
	}
	
	protected void HandleCommonIJavaElement(String content, IJavaElement ije, ASTNode node, String symbol) {
		IRJavaElementNode uni_ele = pool.UniversalElement(content, ije);
		graph.AddVariableNode(uni_ele);
		graph.RegistConnection(uni_ele, expression_node, new VariableConnect(++element_index));
		rewrite.replace(node, ast.newSimpleName(symbol), null);
	}
	
	protected void HandleIVariableElement(String content, ILocalVariable ilv, ASTNode node) {
		HandleCommonIJavaElement(content, ilv, node, "V");
	}
	
	protected void HandleIFieldElement(String content, IField ifd, ASTNode node) {
		HandleCommonIJavaElement(content, ifd, node, "V");
	}
	
	protected void HandleIMethodElement(String content, IMethod imd, ASTNode node) {
		HandleCommonIJavaElement(content, imd, node, "M");
	}
	
	protected void HandleSourceMethodInvokeReturnElementAndNode(String content, IRMethodInvokeReturnElementNode ir_miren, ASTNode node) {
		graph.AddVariableNode(ir_miren);
		graph.RegistConnection(ir_miren, expression_node, new VariableConnect(++element_index));
		rewrite.replace(node, ast.newSimpleName("R"), null);
	}
	
	public int GetElementIndex() {
		return element_index;
	}
	
	protected void PreHandleMethodInvocation(IMethodBinding imb, ASTNode node, List<Expression> arg_list) {
		boolean handle_source = false;
		if (imb != null) {
			IJavaElement ije = imb.getJavaElement();
			if (ije != null && ije instanceof IMethod) {
				IMethod im = (IMethod)ije;
				IRMethodInvokeReturnElementNode ir_miren = new IRMethodInvokeReturnElementNode(node.toString(), im);
				
			}
		} else {
			
		}
		
		Iterator<Expression> aitr = arg_list.iterator();
		while (aitr.hasNext()) {
			Expression expr = aitr.next();
			Document doc = new Document(expr.toString());
			ASTRewrite expr_rewrite = ASTRewrite.create(expr.getAST());
			IIRNode expr_iirn = new IIRNode("");
			IRGeneratorForOneExpression ir_gfoe = new IRGeneratorForOneExpression(java_project, graph_manager, expr, expr_rewrite, pool, graph, expr_iirn, super_class_element, 0);
			expr.accept(ir_gfoe);
			expr_rewrite.rewriteAST(doc, null);
			expr_iirn.SetContent("V=" + doc.toString());
			
		}
		
		boolean handle_source = false;
		if (imb != null) {
			IJavaElement ije = imb.getJavaElement();
			if (ije != null && ije instanceof IMethod) {
				IMethod im = (IMethod)ije;
				Collection<IMethod> methods = null;
				try {
					IRSearchMethodRequestor sr = new IRSearchMethodRequestor(
							java_project, im);
					EclipseSearchForIMember search = new EclipseSearchForIMember();
					search.SearchForWhereTheMethodIsConcreteImplementated(im, sr);
					methods = sr.GetSourceMethods();
				} catch (CoreException e) {
					e.printStackTrace();
				}
				if (methods != null && methods.size() > 0) {
					handle_source = true;
					// TODO
					
				}
			}
		}
		if (!handle_source) {
			// TODO
		}
	}
	
	// method_invocation should be handled.
	// method_invocation expressions.
	@Override
	public boolean visit(MethodInvocation node) {
		// TODO Auto-generated method stub
		List<Expression> nlist = node.arguments();
		return super.visit(node);
	}
	
	@Override
	public void endVisit(MethodInvocation node) {
		
	}
	
	@Override
	public boolean visit(ClassInstanceCreation node) {
		// TODO Auto-generated method stub
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperMethodInvocation node) {
		// TODO Auto-generated method stub
		
		return super.visit(node);
	}
	
	// method_invocation statements.
	@Override
	public boolean visit(ConstructorInvocation node) {
		// TODO Auto-generated method stub
		
		return super.visit(node);
	}
	
	@Override
	public boolean visit(SuperConstructorInvocation node) {
		// TODO Auto-generated method stub
		
		return super.visit(node);
	}
	
}
