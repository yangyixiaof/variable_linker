package cn.yyx.research.program.ir.storage.node;

import org.eclipse.jdt.core.IJavaElement;

public class IRJavaElement extends IIRNode {
	
	private IJavaElement ele = null;
	
	public IRJavaElement(String content, IJavaElement ele) {
		super(content);
		this.ele = ele;
	}
	
	public IJavaElement getElement() {
		return ele;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IRJavaElement) {
			IRJavaElement irje = (IRJavaElement)obj;
			if (ele.equals(irje.ele)) {
				return true;
			}
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return ele.hashCode();
	}
	
}
