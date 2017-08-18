package cn.yyx.research.program.ir.generation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import cn.yyx.research.program.ir.element.VirtualMethodReturnElement;
import cn.yyx.research.program.ir.storage.graph.IRGraphForMethod;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorHelper {

	public static void HandleMethodDeclaration(IJavaProject java_project, IRGraphManager graph_manager, ASTNode node,
			IRElementFactory ele_factory, IRStatementFactory stmt_factory, IMethodBinding imb, IMethod im, IType it,
			List<SingleVariableDeclaration> para_list, IRJavaElementNode super_class_element) {
		IRJavaElementNode return_element_node = ele_factory
				.UniversalElement(new VirtualMethodReturnElement(im.getKey())); // im.getKey(),
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
					params.add(ele_factory.UniversalElement(sije)); // sije.getElementName(), 
				}
			}
		}
		IRGraphForMethod irgfm = new IRGraphForMethod(params, return_element_node);
		IRGeneratorForStatements irgfs = new IRGeneratorForStatements(java_project, irgfm, graph_manager, ele_factory,
				stmt_factory, super_class_element, it, im);
		graph_manager.AddIRGraph(im, irgfm);
		graph_manager.AddMemberRelation(it, im);
		node.accept(irgfs);
	}

}
