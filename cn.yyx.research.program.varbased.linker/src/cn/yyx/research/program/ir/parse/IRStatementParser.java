package cn.yyx.research.program.ir.parse;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;

import cn.yyx.research.program.ir.parse.visitor.VariableResolveCheckVisitor;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

public class IRStatementParser {

	public static void ParseAStatement(IRStatementInfo info) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource("int i = 9; \n int j = i+1;".toCharArray());
		parser.setKind(ASTParser.K_STATEMENTS);
		Block block = (Block) parser.createAST(null);
		String str = block.statements().get(0).toString();
		// testing.
		System.out.println("First statement:" + str);
		ASTVisitor visitor = new VariableResolveCheckVisitor();
		block.accept(visitor);
	}

}
