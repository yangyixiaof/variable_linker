package cn.yyx.research.program.ir.generation;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodReference;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import cn.yyx.research.program.ir.IRConstantMeta;
import cn.yyx.research.program.ir.bind.BindingManager;
import cn.yyx.research.program.ir.element.ConstantUniqueElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedLambdaElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedMethodReferenceElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedNameOrFieldAccessElement;
import cn.yyx.research.program.ir.element.UnSourceResolvedTypeElement;
import cn.yyx.research.program.ir.generation.state.IJavaElementState;
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.connection.SuperConnect;
import cn.yyx.research.program.ir.storage.connection.VariableConnect;
import cn.yyx.research.program.ir.storage.node.IRJavaElement;

public class IRGeneratorForOneExpression extends ASTVisitor {
	
	IRGraphManager graph_manager = null;
	ASTNode node = null;
	AST ast = null;
	ASTRewrite rewrite = null;
	IRElementPool pool = null;
	IRGraph graph = null;
	IRJavaElement super_class_element = null;
	int element_index = 0;
	
	public IRGeneratorForOneExpression(IRGraphManager graph_manager, ASTNode node, ASTRewrite rewrite, IRElementPool pool, IRGraph graph, IRJavaElement super_class_element, int base_index) {
		this.graph_manager = graph_manager;
		this.node = node;
		this.ast = node.getAST();
		this.rewrite = rewrite;
		this.pool = pool;
		this.graph = graph;
		this.super_class_element = super_class_element;
		this.element_index = base_index;
	}
	
	@Override
	public boolean visit(NumberLiteral node) {
		String content = node.toString();
		HandleIJavaElement(content, new ConstantUniqueElement(IRConstantMeta.NumberConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {
		String content = node.toString();
		HandleIJavaElement(content, new ConstantUniqueElement(IRConstantMeta.NullConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {
		String content = node.toString();
		HandleIJavaElement(content, new ConstantUniqueElement(IRConstantMeta.CharConstant + "$" + content), node);
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {
		String content = node.toString();
		HandleIJavaElement(content, new ConstantUniqueElement(IRConstantMeta.BooleanConstant + "$" + content), node);
		return super.visit(node);
	}
	
	@Override
	public boolean visit(StringLiteral node) {
		String content = node.toString();
		HandleIJavaElement(content, new ConstantUniqueElement(IRConstantMeta.StringConstant + "$" + content), node);
		return super.visit(node);
	}
	
	protected void HandleType(IBinding ib, String represent, ASTNode happen) {
		IJavaElementState source_resolved = HandleBinding(ib, happen);
		if (source_resolved == IJavaElementState.HandledWrong) {
			UnSourceResolvedTypeElement ele = new UnSourceResolvedTypeElement(represent);
			HandleIJavaElement(represent, ele, happen);
		}
	}
	
	@Override
	public boolean visit(LambdaExpression node) {
		boolean handled = false;
		IMethodBinding imb = node.resolveMethodBinding();
		if (imb != null) {
			IJavaElement jele = imb.getJavaElement();
			if (jele != null && jele instanceof IMethod) {
				IMethod im = (IMethod) jele;
				HandleIJavaElement(im.toString(), im, node);
				handled = true;
				// take it as a method.
				IRGeneratorForStatements irgfocb = new IRGeneratorForStatements(imb, graph_manager, pool, super_class_element);
				node.getBody().accept(irgfocb);
			}
		}
		if (!handled) {
			String content = node.toString();
			HandleIJavaElement(content, new UnSourceResolvedLambdaElement(content), node);
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
			HandleIJavaElement(content, new UnSourceResolvedNameOrFieldAccessElement(content), node);
		}
	}
	
	@Override
	public boolean visit(FieldAccess node) {
		IVariableBinding ib = node.resolveFieldBinding();
		IJavaElementState state = HandleBinding(ib, node);
		if (state == IJavaElementState.HandledWrong) {
			String content = node.toString();
			HandleIJavaElement(content, new UnSourceResolvedNameOrFieldAccessElement(content), node);
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
			HandleIJavaElement(content, usrnofae, node);
		}
		super.visit(node);
		return false;
	}
	
	protected IJavaElementState HandleBinding(IBinding ib, ASTNode node) {
		if (!BindingManager.QualifiedBinding(ib)) {
			return IJavaElementState.HandledWrong;
		}
		IJavaElement jele = ib.getJavaElement();
		HandleIJavaElement(jele.getElementName(), jele, node);
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
			HandleIJavaElement(im.toString(), im, node);
		} else {
			String content = node.toString();
			UnSourceResolvedMethodReferenceElement ele = new UnSourceResolvedMethodReferenceElement(content);
			HandleSuperConnect(content, ele);
			HandleIJavaElement(content, ele, node);
		}
	}
	
	protected void HandleSuperConnect(String content, IJavaElement ele) {
		IRJavaElement irje = pool.UniversalElement(content, ele);
		if (super_class_element != null) {
			graph.RegistConnection(irje, super_class_element, new SuperConnect());
		}
	}
	
	protected void HandleIJavaElement(String content, IJavaElement ije, ASTNode node) {
		// IRJavaElement irje = new IRJavaElement(content, ije);
		IRJavaElement uni_ele = pool.UniversalElement(content, ije); // irje
		graph.RegistConnection(uni_ele, graph.getActive(), new VariableConnect(++element_index));
		rewrite.replace(node, ast.newSimpleName("V"), null);
	}
	
	public int GetElementIndex() {
		return element_index;
	}
	
	// method_invocation should be handled.

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
