package cn.yyx.research.test;

import org.apache.commons.lang3.StringEscapeUtils;

public class TestStringEscape {
	
	public static void main(String[] args) {
//		String str = "\"Hello World!\"";
		String str2 = "'Hello World!'";
//		System.out.println("Origin:" + str + ";Escaped:" + StringEscapeUtils.escapeHtml4(str));
		System.out.println("Origin:" + str2 + ";Escaped:" + StringEscapeUtils.escapeHtml4(str2));
	}
	
}
