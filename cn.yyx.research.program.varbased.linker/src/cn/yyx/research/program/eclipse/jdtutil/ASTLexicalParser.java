package cn.yyx.research.program.eclipse.jdtutil;

import java.io.File;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.yyx.research.program.fileutil.FileUtil;

public class ASTLexicalParser {
	
	private static ASTLexicalParser unique_parser = new ASTLexicalParser();
	private ASTParser parser = null;
	
	public ASTLexicalParser() {
		parser = ASTParser.newParser(AST.JLS8);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	}
	
	public CompilationUnit ParseJavaFile(File f)
	{
		parser.setSource(FileUtil.ReadFromFile(f).toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public static ASTLexicalParser GetUniqueParser() {
		return unique_parser;
	}
	
}
