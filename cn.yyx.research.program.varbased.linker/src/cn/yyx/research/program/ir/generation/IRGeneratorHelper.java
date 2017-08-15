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
import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraphForMethod;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRGeneratorHelper {

	public static void HandleMethodDeclaration(IJavaProject java_project, IRGraphManager graph_manager, ASTNode node, IRElementPool pool, IMethodBinding imb, IMethod im, IType it, List<SingleVariableDeclaration> para_list, IRJavaElementNode super_class_element) {
		// IRGeneratorForOneProject.GetInstance().AddCalleeCaller(im, null);
		// IRForOneMethod imb = null;
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
		// if (node.isConstructor()) {
		// imb = IRGeneratorForOneProject.GetInstance().FetchIConstructorIR(im, it);
		// } else {
		// imb = IRGeneratorForOneProject.GetInstance().FetchIMethodIR(im);
		// }
		// IRGeneratorForOneLogicBlock irgfocb = new IRGeneratorForOneLogicBlock(im,
		// imb);
		node.accept(irgfs);
		// IRGeneratorForOneProject.GetInstance().FetchITypeIR((it)).AddMethodLevel((IRForOneMethod)irgfocb.GetGeneration());
	}

}
