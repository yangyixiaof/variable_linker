package cn.yyx.research.test;

import org.apache.commons.lang3.StringEscapeUtils;

public class TestStringEscape {
	
	public static void main(String[] args) {
		String str = "\"Hello World!\"";
		System.out.println("Origin:" + str + ";Escaped:" + StringEscapeUtils.escapeHtml4(str));
	}
	
}
