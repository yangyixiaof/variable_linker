package cn.yyx.research.program.ir.element;

import cn.yyx.research.program.ir.IRElementMeta;

public class UnresolvedNameOrFieldAccessElement extends VirtualDefinedElement {
	
	public UnresolvedNameOrFieldAccessElement(String reference) {
		super(reference);
	}
	
	@Override
	public String getElementName() {
		return IRElementMeta.UnresolvedNameOrFieldAccessElement + "#" + represent;
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.UnresolvedNameOrFieldAccessElement + "#" + represent;
	}
	
}
