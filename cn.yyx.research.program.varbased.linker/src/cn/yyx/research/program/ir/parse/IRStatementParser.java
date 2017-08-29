package cn.yyx.research.program.ir.parse;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Statement;

import cn.yyx.research.program.ir.parse.visitor.VariableResolveCheckVisitor;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

public class IRStatementParser {

	public static void ParseAStatement(IRStatementInfo info) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		StringBuilder build = new StringBuilder("");
		build.append("Integer N = 0;\n");
		build.append("Object E = null;\n");
		build.append("Character C = null;\n");
		build.append("Boolean B = null;\n");
		build.append("String S = null;\n");
		build.append("Object V = null;\n");
		build.append("Object M = null;\n");
		build.append(info.GetContent() + "\n");
		parser.setSource(build.toString().toCharArray());
		// "int i = 9; \n int j = i+1;"
		parser.setKind(ASTParser.K_STATEMENTS);
		Block block = (Block) parser.createAST(null);
		Statement last_stmt = (Statement) block.statements().get(block.statements().size() - 1);
		// String str = block.statements().get(0).toString();
		// testing.
		// System.out.println("First statement:" + str);
		ASTVisitor visitor = new VariableResolveCheckVisitor(info.GetAmountOfVariables());
		last_stmt.accept(visitor);
	}

}
