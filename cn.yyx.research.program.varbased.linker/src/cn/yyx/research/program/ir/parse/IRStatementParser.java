package cn.yyx.research.program.ir.parse;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;

import cn.yyx.research.program.eclipse.jdtutil.JDTParser;
import cn.yyx.research.program.ir.parse.visitor.VariableResolveCheckVisitor;
import cn.yyx.research.program.ir.storage.node.info.IRStatementInfo;

public class IRStatementParser {

	public static void ParseAStatement(IRStatementInfo info) {
		String gap = "	";
		JDTParser parser = JDTParser.GetUniquePrimitiveParser();
		StringBuilder build = new StringBuilder("");
		build.append("public class ParseEnv {\n");
		build.append(gap + "public void test() {\n");
		build.append(gap + gap + "Integer N = 0;\n");
		build.append(gap + gap + "Object E = null;\n");
		build.append(gap + gap + "Character C = null;\n");
		build.append(gap + gap + "Boolean B = null;\n");
		build.append(gap + gap + "String S = null;\n");
		build.append(gap + gap + "Object V = null;\n");
		build.append(gap + gap + "Object M = null;\n");
		build.append(gap + gap + info.GetContent() + "\n");
		build.append(gap + "}\n");
		build.append("}\n");
		// build.append("V = V = V;\n");
		// testing.
		System.err.println("Parse-Content:\n" + build.toString());

		CompilationUnit cu = parser.ParseJavaContent("", "ParseEnv", new Document(build.toString()));
		if (cu != null) {
			@SuppressWarnings("unchecked")
			List<AbstractTypeDeclaration> types = cu.types();
			TypeDeclaration td = (TypeDeclaration) types.get(0);
			MethodDeclaration[] mds = td.getMethods();
			Block block = mds[0].getBody();
			// "int i = 9; \n int j = i+1;"
			Statement last_stmt = (Statement) block.statements().get(block.statements().size() - 1);
			// testing.
			// String str = block.statements().get(0).toString();
			// System.out.println("First statement:" + str);
			ASTVisitor visitor = new VariableResolveCheckVisitor(info.GetAmountOfVariables());
			last_stmt.accept(visitor);
		}
	}

}
