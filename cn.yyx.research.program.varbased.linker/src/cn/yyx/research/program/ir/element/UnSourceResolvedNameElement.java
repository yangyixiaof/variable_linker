package cn.yyx.research.program.ir.element;

import cn.yyx.research.program.ir.meta.IRElementMeta;

public class UnSourceResolvedNameElement extends VirtualDefinedElement {
	
	public UnSourceResolvedNameElement(String reference) {
		super(reference);
	}
	
	@Override
	public String getElementName() {
		return represent;
		// IRElementMeta.UnresolvedNameOrFieldAccessElement + "#" + 
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.HumanMadeHandler;
		// IRElementMeta.UnresolvedNameOrFieldAccessElement + "#" + represent
	}
	
}
