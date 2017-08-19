package cn.yyx.research.program.ir.generation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.research.program.ir.storage.graph.IRGraph;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorForOneClass extends IRGeneratorForStatements {

	private Initializer initial_node = null;
	private IType it = null;

	public IRGeneratorForOneClass(IType it, IJavaProject java_project, IRGraph graph, IRGraphManager graph_manager,
			IRElementFactory ele_factory, IRStatementFactory stmt_factory, ICompilationUnit type_declare_resource, CompilationUnit type_declare) {
		super(java_project, graph, graph_manager, ele_factory, stmt_factory, null, it, null, type_declare_resource, type_declare);
		this.it = it;
	}

	// public IRGeneratorForOneClass(IType it) {
	// super(null, new IRForOneField(it));
	// this.it = it;
	// }

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
			IMethod im = (IMethod) ije;
			List<SingleVariableDeclaration> para_list = node.parameters();
			IRGeneratorHelper.HandleMethodDeclaration(java_project, graph_manager, node.getBody(), ele_factory,
					stmt_factory, imb, im, it, para_list, super_class_element, type_declare_resource, type_declare);
		}
		return false;
	}

	@Override
	public boolean preVisit2(ASTNode node) {
		boolean goon = super.preVisit2(node);
		if (node instanceof AbstractTypeDeclaration || node instanceof AnonymousClassDeclaration) {
			IType resolved_type = NodeBinding(node);
			if (resolved_type != null) {
				boolean type_equals = resolved_type.equals(it);// resolved_type == null ? false :
				IType super_it = null;
				// boolean has_element = irc.IsHasElement();
				if (node instanceof TypeDeclaration) {
					Type super_type = ((TypeDeclaration) node).getSuperclassType();
					if (super_type != null) {
						ITypeBinding itb = super_type.resolveBinding();
						if (itb != null) {
							IJavaElement super_ije = itb.getJavaElement();
							if (super_ije != null) {
								super_it = (IType) super_ije;
							}
						}
					}
				}
				if (type_equals) {// && has_element
					if (super_it != null) {
						this.super_class_element = ele_factory.UniversalElement(super_it); // super_it.getElementName(),
					}
					goon = goon && true;
					// IRGeneratorForOneProject.GetInstance().FetchITypeIR((it)).SetFieldLevel((IRForOneField)irc);
				} else {
					IRGraph graph = new IRGraph();
					graph_manager.AddIRGraph(resolved_type, graph);
					IRGeneratorForOneClass irgfoc = new IRGeneratorForOneClass(resolved_type, java_project, graph,
							graph_manager, ele_factory, stmt_factory, type_declare_resource, type_declare);
					node.accept(irgfoc);
					goon = goon && false;
				}
			} else {
				goon = goon && false;
			}
		}
		return goon;
	}

	// Solved. internal type should be visited.

	@Override
	public void postVisit(ASTNode node) {
		if (node instanceof AbstractTypeDeclaration || node instanceof AnonymousClassDeclaration) {
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
			AbstractTypeDeclaration atd = (AbstractTypeDeclaration) node;
			ITypeBinding tb = atd.resolveBinding();
			if (tb != null) {
				IJavaElement tele = tb.getJavaElement();
				if (tele != null && tele instanceof IType) {
					return (IType) tele;
				}
			}
		}
		if (node instanceof AnonymousClassDeclaration) {
			AnonymousClassDeclaration atd = (AnonymousClassDeclaration) node;
			ITypeBinding tb = atd.resolveBinding();
			if (tb != null) {
				IJavaElement tele = tb.getJavaElement();
				if (tele != null && tele instanceof IType) {
					return (IType) tele;
				}
			}
		}
		return null;
	}

}
