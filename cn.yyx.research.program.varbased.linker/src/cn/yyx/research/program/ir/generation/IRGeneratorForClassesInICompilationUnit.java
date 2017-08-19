package cn.yyx.research.program.ir.generation;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;

import cn.yyx.research.program.ir.storage.graph.IRGraph;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorForClassesInICompilationUnit extends ASTVisitor {
	
	IJavaProject java_project = null;
	IRGraphManager graph_manager = null;
	IRElementFactory ele_factory = null;
	IRStatementFactory stmt_factory = null;
	ICompilationUnit type_declare_resource = null;
	CompilationUnit type_declare = null;
	
	public IRGeneratorForClassesInICompilationUnit(IJavaProject java_project, IRGraphManager graph_manager,
			IRElementFactory ele_factory, IRStatementFactory stmt_factory, ICompilationUnit type_declare_resource, CompilationUnit type_declare) {
		this.java_project = java_project;
		this.graph_manager = graph_manager;
		this.ele_factory = ele_factory;
		this.stmt_factory = stmt_factory;
		this.type_declare_resource = type_declare_resource;
		this.type_declare = type_declare;
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
			// testing.
			// System.out.println("Debug Info: IType:" + it);
			if (it != null) {
				IRGraph graph = new IRGraph();
				graph_manager.AddIRGraph(it, graph);
				IRGeneratorForOneClass irfoc = new IRGeneratorForOneClass(it, java_project, graph, graph_manager, ele_factory, stmt_factory, type_declare_resource, type_declare);
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
