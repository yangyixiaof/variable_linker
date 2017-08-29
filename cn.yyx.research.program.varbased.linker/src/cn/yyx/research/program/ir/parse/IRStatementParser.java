package cn.yyx.research.program.ir.parse;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import cn.yyx.research.program.eclipse.jdtutil.JDTParser;
import cn.yyx.research.program.ir.parse.visitor.VariableResolveCheckVisitor;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

public class IRStatementParser {

	public static void ParseAStatement(IRStatementInfo info) {
		JDTParser parser = JDTParser.CreateJDTStatementParserWithManualEnvironment();
		StringBuilder build = new StringBuilder("");
		build.append("Integer N = 0;\n");
		build.append("Object E = null;\n");
		build.append("Character C = null;\n");
		build.append("Boolean B = null;\n");
		build.append("String S = null;\n");
		build.append("Object V = null;\n");
		build.append("Object M = null;\n");
		build.append(info.GetContent() + "\n");
		
		// testing.
		System.err.println("Parse-Content:" + build.toString());
		
		Block block = parser.ParseStatements(build.toString());
		// "int i = 9; \n int j = i+1;"
		Statement last_stmt = (Statement) block.statements().get(block.statements().size() - 1);
		// String str = block.statements().get(0).toString();
		// testing.
		// System.out.println("First statement:" + str);
		ASTVisitor visitor = new VariableResolveCheckVisitor(info.GetAmountOfVariables());
		last_stmt.accept(visitor);
	}

}
