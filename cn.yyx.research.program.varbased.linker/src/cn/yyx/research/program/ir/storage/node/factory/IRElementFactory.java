package cn.yyx.research.program.ir.storage.node.factory;

import java.util.Collection;
import java.util.Iterator;
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
		// testing.
		// System.out.println("debugging...element_name:" + ije.getElementName() + ";element_handler:" + ije.getHandleIdentifier() + ";element_class:" + ije.getClass());
		// String content = ije.getElementName();
		String pool_key = GetIJavaElementKey(ije);
		IRJavaElementNode ele = pool.get(pool_key);
		if (ele == null) {
			ele = new IRJavaElementNode(ije); // content, 
			pool.put(pool_key, ele);
		}
		return ele;
	}
	
	public static String GetIJavaElementKey(IJavaElement ije) {
		String pool_key = ije.getElementName() + "#" + ije.getHandleIdentifier();
		return pool_key;
	}
	
	public Collection<IRJavaElementNode> GetAllIRJavaElementNodes() {
		Collection<IRJavaElementNode> result = new LinkedList<IRJavaElementNode>();
		result.addAll(non_universal_elements);
		result.addAll(pool.values());
		return result;
	}
	
	public void RefineSelf() {
		List<String> pkeys = new LinkedList<String>(pool.keySet());
		Iterator<String> pkitr = pkeys.iterator();
		while (pkitr.hasNext()) {
			String pkey = pkitr.next();
			IRJavaElementNode irjen = pool.get(pkey);
			if (irjen.IsIsolate()) {
				pool.remove(pkey);
			}
		}
		Iterator<IRJavaElementNode> nue_itr = non_universal_elements.iterator();
		while (nue_itr.hasNext()) {
			IRJavaElementNode irjen = nue_itr.next();
			if (!irjen.IsIsolate()) {
				System.err.println("Strange! non_universal_element is not isolated. The strange node is: " + irjen);
				System.exit(1);
			}
		}
		non_universal_elements.clear();
	}
	
}
