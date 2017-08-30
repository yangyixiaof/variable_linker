package cn.yyx.research.program.eclipse.jdtutil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;

import cn.yyx.research.program.eclipse.project.meta.FakedProjectEnvironmentMeta;
import cn.yyx.research.program.fileutil.FileUtil;

public class JDTParser {
	
	private static JDTParser Unique_Primitive_Parser = new JDTParser();// , null
	
	private ASTParser parser = null;
	
	private IJavaProject javaProject = null;
	// private Set<String> source_classes = new HashSet<String>();
	
	public static JDTParser CreateJDTParserWithJavaProject(IJavaProject java_project)
	{
		return new JDTParser(java_project);
	}
	
	public static JDTParser CreateJDTParserWithPrimitiveEnvironment()
	{
		return new JDTParser();
	}
	
	private JDTParser(IJavaProject javaProject) {// , Set<String> source_classes
		this.javaProject = javaProject;
//		if (source_classes != null)
//		{
//			this.source_classes.addAll(source_classes);
//		}
		parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		parser.setProject(javaProject);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	}
	
	private JDTParser() {// , Set<String> source_classes
		parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		String faked_env_path = System.getProperty("user.home") + "/" + FakedProjectEnvironmentMeta.FakedEnvironment;
		// set class_path of ASTParser.
		List<String> entries = new ArrayList<String>();
		String jre_home = System.getProperty("java.home");
		entries.add(jre_home + "/lib/rt.jar");
		String[] classpath_array = entries.toArray(new String[entries.size()]);
		parser.setEnvironment(classpath_array, new String[]{faked_env_path}, new String[]{"UTF-8"}, true);
		// parser.setProject(javaProject);
		// TODO set environment manually.
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
	}
	
	public CompilationUnit ParseICompilationUnit(ICompilationUnit icu)
	{
		parser.setSource(icu);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public CompilationUnit ParseJavaFile(File f)
	{
		File dest = new File(FakedProjectEnvironmentMeta.GetFakedEnvironment() + "/" + f.getName());
		FileUtil.CopyFile(f, dest);
		parser.setUnitName(f.getName());
		parser.setSource(FileUtil.ReadFromFile(f).toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public CompilationUnit ParseJavaContent(IDocument doc, String unit_name)
	{
		File dest = new File(FakedProjectEnvironmentMeta.GetFakedEnvironment() + "/" + unit_name);
		FileUtil.WriteToFile(dest, doc.get());
		parser.setUnitName(unit_name);
		parser.setSource(doc.get().toCharArray());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public CompilationUnit ParseOneClass(IType f)
	{
		parser.setSource(f.getClassFile());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
//	public Block ParseStatements(String statements)
//	{
//		parser.setSource(statements.toCharArray());
//		Block block = (Block) parser.createAST(null);
//		return block;
//	}

	public static JDTParser GetUniquePrimitiveParser() {
		return Unique_Primitive_Parser;
	}

	public IJavaProject GetJavaProject() {
		return javaProject;
	}
		
}
