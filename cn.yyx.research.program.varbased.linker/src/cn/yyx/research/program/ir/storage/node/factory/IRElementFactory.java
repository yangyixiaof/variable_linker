package cn.yyx.research.program.ir.storage.node.factory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodParamElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodReturnElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;

public class IRElementFactory {
	
	TreeMap<String, IRJavaElementNode> pool = new TreeMap<String, IRJavaElementNode>();
	List<IRJavaElementNode> non_universal_elements = new LinkedList<IRJavaElementNode>();
	
	public IRElementFactory() {
	}
	
	public IRSourceMethodParamElementNode CreateIRSourceMethodParamElementNode(IRSourceMethodStatementNode irsmsn, int param_index) {
		IRSourceMethodParamElementNode result = new IRSourceMethodParamElementNode(irsmsn, param_index);
		non_universal_elements.add(result);
		return result;
	}
	
	public IRSourceMethodReturnElementNode CreateIRSourceMethodReturnElementNode(IRSourceMethodStatementNode irsmsn) {
		IRSourceMethodReturnElementNode result = new IRSourceMethodReturnElementNode(irsmsn);
		non_universal_elements.add(result);
		return result;
	}
	
	public IRJavaElementNode UniversalElement(IJavaElement ije) {
		// String content, 
		// IRJavaElement irje
		// String content = irje.getElement().toString();
		// TODO Here needs to be checked.
		System.out.println("debugging...element_name:" + ije.getElementName() + ";element_handler:" + ije.getHandleIdentifier() + ";element_class:" + ije.getClass());
		// String content = ije.getElementName();
		String pool_key = ije.getElementName() + "#" + ije.getHandleIdentifier();
		IRJavaElementNode ele = pool.get(pool_key);
		if (ele == null) {
			ele = new IRJavaElementNode(ije); // content, 
			pool.put(pool_key, ele);
		}
		return ele;
	}
	
	public Collection<IRJavaElementNode> GetAllIRJavaElementNodes() {
		Collection<IRJavaElementNode> result = new LinkedList<IRJavaElementNode>();
		result.addAll(non_universal_elements);
		result.addAll(pool.values());
		return result;
	}
	
}
