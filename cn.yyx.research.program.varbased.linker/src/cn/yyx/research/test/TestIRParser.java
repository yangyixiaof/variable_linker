package cn.yyx.research.test;

import cn.yyx.research.program.ir.parse.IRStatementParser;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

public class TestIRParser {
	
	public static void TestParseIRStatement() {
		// IRStatementParser parser = new IRStatementParser();
		System.err.println(IRStatementParser.CheckTheStatementContainsRightAmountOfVariables(new IRStatementInfo(2, "N *= N;")));
	}
	
}
