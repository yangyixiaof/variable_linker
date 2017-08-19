package cn.yyx.research.program.ir.element;

import cn.yyx.research.program.ir.meta.IRElementMeta;

public class ConstantUniqueElement extends VirtualDefinedElement {
	
	private String represent = null;
	
	public ConstantUniqueElement(String represent) {
		super(represent);
	}
	
	@Override
	public String getElementName() {
		return represent;
		// IRElementMeta.ConstantUniqueElement + "#" + 
	}
	
	@Override
	public String getHandleIdentifier() {
		return IRElementMeta.HumanMadeHandler;
	}
	
}
