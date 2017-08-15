package cn.yyx.research.program.ir.generation;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphManager;

public class IRGeneratorForClassesInICompilationUnit extends ASTVisitor {
	
	IJavaProject java_project = null;
	IRGraphManager graph_manager = null;
	IRElementPool pool = null;
	
	public IRGeneratorForClassesInICompilationUnit(IJavaProject java_project, IRGraphManager graph_manager,
			IRElementPool pool) {
		this.java_project = java_project;
		this.graph_manager = graph_manager;
		this.pool = pool;
	}
	
	// private List<IRForOneClass> classes = new LinkedList<IRForOneClass>();
	
//	@Override
//	public boolean visit(AnonymousClassDeclaration node) {
//		IType it = ResolveAbstractType(node.resolveBinding());
//		if (it != null)
//		{
//			IRGeneratorForOneClass irfoc = new IRGeneratorForOneClass(it);
//			node.accept(irfoc);
//			// getClasses().add(irfoc.GetClassLevelGeneration());
//		}
//		return false;
//	}
	
	@Override
	public boolean preVisit2(ASTNode node) {
		if (node instanceof AbstractTypeDeclaration) {
			IType it = ResolveAbstractType(((AbstractTypeDeclaration)node).resolveBinding());
			if (it != null) {
				IRGeneratorForOneClass irfoc = new IRGeneratorForOneClass(it, java_project, new IRGraph(), graph_manager, pool, super_class_element);
				node.accept(irfoc);
				// getClasses().add(irfoc.GetClassLevelGeneration());
			}
		}
		return super.preVisit2(node);
	}

//	@Override
//	public boolean visit(TypeDeclaration node) {
//		IType it = ResolveAbstractType(node.resolveBinding());
//		if (it != null)
//		{
//			IRGeneratorForOneClass irfoc = new IRGeneratorForOneClass(it);
//			node.accept(irfoc);
//			// getClasses().add(irfoc.GetClassLevelGeneration());
//		}
//		return false;
//	}
	
	private IType ResolveAbstractType(ITypeBinding type_bind)
	{
		IJavaElement j_ele = null;
		if (type_bind != null)
		{
			j_ele = type_bind.getJavaElement();
		}
		if (j_ele instanceof IType)
		{
			IType it = (IType)j_ele;
			return it;
		}
		return null;
	}
	
}
