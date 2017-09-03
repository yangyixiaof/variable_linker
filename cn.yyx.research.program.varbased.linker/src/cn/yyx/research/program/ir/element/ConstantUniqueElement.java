package cn.yyx.research.program.ir.element;

public class ConstantUniqueElement extends VirtualDefinedElement {
		
	public ConstantUniqueElement(String represent) {
		super(represent);
	}
	
	@Override
	public String getElementName() {
		return represent;
		// IRElementMeta.ConstantUniqueElement + "#" + 
	}
	
}
