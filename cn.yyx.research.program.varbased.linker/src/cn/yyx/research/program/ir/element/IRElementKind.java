package cn.yyx.research.program.ir.element;

public enum IRElementKind {
	
	// literals
	NumericValue("N"),
	NullValue("E"),
	CharacterValue("C"),
	BooleanValue("B"),
	StringValue ("S"),
	
	// 
	// QualifiedName("Q"),
	Variable("V"),
	Type("T"),
	Method("M");
	
	private String value = null;
	
	private IRElementKind(String value) {
		this.value = value;
	}

	public String Value() {
		return value;
	}
	
//	public static final String NumericValue = "N";
//	public static final String NullValue = "E";
//	public static final String CharacterValue = "C";
//	public static final String BooleanValue = "B";
//	public static final String StringValue = "S";
//	
//	public static final String QualifiedName = "Q";
//	public static final String Variable = "V";
//	public static final String Type = "T";
//	public static final String Method = "M";
	
}
