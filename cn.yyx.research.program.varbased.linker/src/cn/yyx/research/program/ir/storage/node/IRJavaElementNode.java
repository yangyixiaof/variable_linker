package cn.yyx.research.program.ir.storage.node;

import org.eclipse.jdt.core.IJavaElement;

public class IRJavaElementNode extends IIRNode {
	
	private IJavaElement ele = null;
	
	public IRJavaElementNode(String content, IJavaElement ele) {
		super(content);
		this.ele = ele;
	}
	
	public IJavaElement getElement() {
		return ele;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof IRJavaElementNode) {
			IRJavaElementNode irje = (IRJavaElementNode)obj;
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