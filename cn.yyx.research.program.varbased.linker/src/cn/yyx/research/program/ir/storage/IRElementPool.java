package cn.yyx.research.program.ir.storage;

import java.util.TreeMap;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRElementPool {
	
	TreeMap<String, IRJavaElementNode> pool = new TreeMap<String, IRJavaElementNode>();
	
	public IRElementPool() {
	}
	
	public IRJavaElementNode UniversalElement(String content, IJavaElement ije) {
		// IRJavaElement irje
		// String content = irje.getElement().toString();
		IRJavaElementNode ele = pool.get(content);
		if (ele == null) {
			ele = new IRJavaElementNode(content, ije);
			pool.put(content, ele);
		}
		return ele;
	}
	
}
