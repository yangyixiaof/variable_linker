package cn.yyx.research.program.ir.parse;

import cn.yyx.research.program.ir.meta.IRElementKind;

public class ConstantElementParser {
	
	public static IRElementKind ParseElementKind(String literal_value) {
		if (literal_value.startsWith("&quot;") && literal_value.endsWith("&quot;")) {
			return IRElementKind.StringValue;
		}
		if (literal_value.startsWith("'") && literal_value.endsWith("'")) {
			return IRElementKind.CharacterValue;
		}
		if (literal_value.equals("null")) {
			return IRElementKind.NullValue;
		}
		if (literal_value.equals("true") || literal_value.equals("false")) {
			return IRElementKind.BooleanValue;
		}
		try {
			Integer.parseInt(literal_value);
			return IRElementKind.NumericValue;
		} catch (Exception e) {
		}
		try {
			Long.parseLong(literal_value);
			return IRElementKind.NumericValue;
		} catch (Exception e) {
		}
		try {
			Float.parseFloat(literal_value);
			return IRElementKind.NumericValue;
		} catch (Exception e) {
		}
		try {
			Double.parseDouble(literal_value);
			return IRElementKind.NumericValue;
		} catch (Exception e) {
		}
		return null;
	}
	
}
