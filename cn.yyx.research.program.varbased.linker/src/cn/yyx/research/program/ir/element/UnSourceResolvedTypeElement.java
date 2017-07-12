package cn.yyx.research.program.ir.element;

import cn.yyx.research.program.ir.IRElementMeta;

public class UnSourceResolvedTypeElement extends VirtualDefinedElement {
	
	public UnSourceResolvedTypeElement(String represent) {
		super(represent);
	}
	
	@Override
	public String getElementName() {
		return IRElementMeta.UnresolvedTypeElement + "#" + represent;
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.UnresolvedTypeElement + "#" + represent;
	}
	
}
