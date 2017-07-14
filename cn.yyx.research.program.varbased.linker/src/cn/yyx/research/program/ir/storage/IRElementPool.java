package cn.yyx.research.program.ir.storage;

import java.util.TreeMap;

import cn.yyx.research.program.ir.storage.node.IRJavaElement;

public class IRElementPool {
	
	TreeMap<String, IRJavaElement> pool = new TreeMap<String, IRJavaElement>();
	
	public IRElementPool() {
	}
	
	public IRJavaElement UniversalElement(IRJavaElement irje) {
		String content = irje.getElement().toString();
		IRJavaElement ele = pool.get(content);
		if (ele == null) {
			ele = irje;
			pool.put(content, ele);
		}
		return ele;
	}
	
}
