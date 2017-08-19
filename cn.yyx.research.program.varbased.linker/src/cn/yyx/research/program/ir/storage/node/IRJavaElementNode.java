package cn.yyx.research.program.ir.storage.node;

import org.eclipse.jdt.core.IJavaElement;

public class IRJavaElementNode extends IIRNode {
	
	private IJavaElement ele = null;
	
	// String content, 
	public IRJavaElementNode(IJavaElement ele) {
		super(null);
		if (ele != null) {
			this.SetContent(ele.getElementName());
		}
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
		int result = super.hashCode();
		if (ele != null) {
			result = ele.hashCode();
		}
		return result;
	}
	
}
