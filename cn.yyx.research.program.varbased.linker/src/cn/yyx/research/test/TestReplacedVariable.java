package cn.yyx.research.test;

import java.io.File;

import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.yyx.research.program.eclipse.jdtutil.JDTParser;

public class TestReplacedVariable {
	
	public static void main(String[] args) {
		JDTParser unique_parser = JDTParser.GetUniquePrimitiveParser();
		CompilationUnit cu = unique_parser.ParseJavaFile(new File("test_examples/ReplacedVariableClass.java"));
		cu.accept(new CompilableTestVisitor());
	}
	
}
