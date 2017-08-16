package cn.yyx.research.program.ir.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import cn.yyx.research.program.eclipse.jdtutil.JDTParser;
import cn.yyx.research.program.eclipse.searchutil.EclipseSearchForICompilationUnits;
import cn.yyx.research.program.ir.element.VirtualMethodReturnElement;
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraphForMethod;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRGeneratorHelper {

	public static void HandleMethodDeclaration(IJavaProject java_project, IRGraphManager graph_manager, ASTNode node,
			IRElementPool pool, IMethodBinding imb, IMethod im, IType it, List<SingleVariableDeclaration> para_list,
			IRJavaElementNode super_class_element) {
		IRJavaElementNode return_element_node = pool.UniversalElement(im.getKey(),
				new VirtualMethodReturnElement(im.getKey()));
		LinkedList<IRJavaElementNode> params = new LinkedList<IRJavaElementNode>();
		Iterator<SingleVariableDeclaration> sitr = para_list.iterator();
		while (sitr.hasNext()) {
			SingleVariableDeclaration svd = sitr.next();
			IBinding sib = svd.getName().resolveBinding();
			params.add(null);
			if (sib != null) {
				IJavaElement sije = sib.getJavaElement();
				if (sije != null) {
					params.removeLast();
					params.add(pool.UniversalElement(sije.getElementName(), sije));
				}
			}
		}
		IRGraphForMethod irgfm = new IRGraphForMethod(params, return_element_node);
		IRGeneratorForStatements irgfs = new IRGeneratorForStatements(java_project, irgfm, graph_manager, pool,
				super_class_element, it, im);
		graph_manager.AddIRGraph(im, irgfm);
		graph_manager.AddMemberRelation(it, im);
		node.accept(irgfs);
	}

	public static void GenerateForOneProject(IJavaProject java_project) {
		IRGraphManager graph_manager = new IRGraphManager();
		IRElementPool pool = new IRElementPool();
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		// System.err.println("unit_size:" + units.size());
		if (units != null) {
			for (final ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.CreateJDTParser(java_project).ParseICompilationUnit(icu);
				IRGeneratorForClassesInICompilationUnit irgfcicu = new IRGeneratorForClassesInICompilationUnit(
						java_project, graph_manager, pool);
				cu.accept(irgfcicu);
			}
		}
	}

}
