package cn.yyx.research.program.ir.storage;

import java.util.TreeMap;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.ir.storage.node.IRJavaElement;

public class IRElementPool {
	
	TreeMap<String, IRJavaElement> pool = new TreeMap<String, IRJavaElement>();
	
	public IRElementPool() {
	}
	
	public IRJavaElement UniversalElement(String content, IJavaElement ije) {
		// IRJavaElement irje
		// String content = irje.getElement().toString();
		IRJavaElement ele = pool.get(content);
		if (ele == null) {
			ele = new IRJavaElement(content, ije);
			pool.put(content, ele);
		}
		return ele;
	}
	
}
