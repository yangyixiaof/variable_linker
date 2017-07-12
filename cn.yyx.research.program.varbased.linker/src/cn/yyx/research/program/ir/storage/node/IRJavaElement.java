package cn.yyx.research.program.ir.storage.node;

import org.eclipse.jdt.core.IJavaElement;

public class IRJavaElement extends IIRNode {
	
	private IJavaElement ele = null;
	
	public IRJavaElement(String content, IJavaElement ele) {
		super(content);
		this.setEle(ele);
	}

	public IJavaElement getEle() {
		return ele;
	}

	public void setEle(IJavaElement ele) {
		this.ele = ele;
	}

}
