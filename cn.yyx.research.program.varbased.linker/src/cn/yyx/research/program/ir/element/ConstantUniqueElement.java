package cn.yyx.research.program.ir.element;

import cn.yyx.research.program.ir.meta.IRElementMeta;

public class ConstantUniqueElement extends VirtualDefinedElement {
	
	private String represent = null;
	
	public ConstantUniqueElement(String represent) {
		super(represent);
	}
	
	@Override
	public String getElementName() {
		return IRElementMeta.ConstantUniqueElement + "#" + represent;
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.ConstantUniqueElement + "#" + represent;
	}
	
}
