package cn.yyx.research.program.ir.generation;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRGeneratorForOneClass extends IRGeneratorForStatements {

	private Initializer initial_node = null;
	private IType it = null;
	
	public IRGeneratorForOneClass(IType it, IJavaProject java_project, IRGraph graph, IRGraphManager graph_manager,
			IRElementPool pool, IRJavaElementNode super_class_element) {
		super(java_project, null, graph, graph_manager, pool, super_class_element);
		this.it = it;
	}
	
//	public IRGeneratorForOneClass(IType it) {
//		super(null, new IRForOneField(it));
//		this.it = it;
//	}
		
	@Override
	public boolean visit(Initializer node) {
		this.initial_node = node;
		return false;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		// no need to do anything.
		return super.visit(node);
	}
	// Solved. return element needs to be handled.
	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(MethodDeclaration node) {
		IMethodBinding imb = node.resolveBinding();
		IJavaElement ije = imb.getJavaElement();
		if (ije instanceof IMethod) {
			IMethod im = (IMethod)ije;
			List<SingleVariableDeclaration> para_list = node.parameters();
			IRGeneratorHelper.HandleMethodDeclaration(java_project, graph_manager, node.getBody(), pool, imb, im, para_list, super_class_element);
		}
		return false;
	}
	
	@Override
	public void postVisit(ASTNode node) {
		if (node instanceof AbstractTypeDeclaration || node instanceof AnonymousClassDeclaration)
		{
			IType resolved_type = NodeBinding(node);
			boolean type_equals = resolved_type == null ? false : resolved_type.equals(it);
			// boolean has_element = irc.IsHasElement();
			if (type_equals) {// && has_element
				if (this.initial_node != null) {
					this.initial_node.accept(this);
				}
				// IRGeneratorForOneProject.GetInstance().FetchITypeIR((it)).SetFieldLevel((IRForOneField)irc);
			}
		}
		super.postVisit(node);
	}
	
	private IType NodeBinding(ASTNode node) {
		if (node instanceof AbstractTypeDeclaration) {
			AbstractTypeDeclaration atd = (AbstractTypeDeclaration)node;
			ITypeBinding tb = atd.resolveBinding();
			if (tb != null) {
				IJavaElement tele = tb.getJavaElement();
				if (tele != null && tele instanceof IType) {
					return (IType)tele;
				}
			}
		}
		if (node instanceof AnonymousClassDeclaration) {
			AnonymousClassDeclaration atd = (AnonymousClassDeclaration)node;
			ITypeBinding tb = atd.resolveBinding();
			if (tb != null) {
				IJavaElement tele = tb.getJavaElement();
				if (tele != null && tele instanceof IType) {
					return (IType)tele;
				}
			}
		}
		return null;
	}

}
