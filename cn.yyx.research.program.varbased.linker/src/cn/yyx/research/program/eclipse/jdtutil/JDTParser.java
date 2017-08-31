package cn.yyx.research.program.eclipse.jdtutil;

import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;

import cn.yyx.research.program.eclipse.project.AnalysisEnvironment;
import cn.yyx.research.program.eclipse.project.resource.FakeResourceCreationHelper;

public class JDTParser {
	
	private static JDTParser Unique_Primitive_Parser = CreateJDTParserWithPrimitiveEnvironment();// , null
	
	private ASTParser parser = null;
	
	private IJavaProject java_project = null;
	
	public static JDTParser CreateJDTParserWithJavaProject(IJavaProject java_project)
	{
		return new JDTParser(java_project);
	}
	
	protected static JDTParser CreateJDTParserWithPrimitiveEnvironment()
	{
		return new JDTParser(AnalysisEnvironment.CreateDefaultAnalysisEnironment());
	}
	
	private JDTParser(IJavaProject javaProject) {// , Set<String> source_classes
		this.java_project = javaProject;
		parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		parser.setCompilerOptions(options);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setProject(javaProject);
	}
	
//	private JDTParser() {// , Set<String> source_classes
//		parser = ASTParser.newParser(AST.JLS8);
//		parser.setResolveBindings(true);
//		parser.setBindingsRecovery(true);
//		Map<String, String> options = JavaCore.getOptions();
//		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
//		parser.setCompilerOptions(options);
//		parser.setKind(ASTParser.K_COMPILATION_UNIT);
//		String faked_env_path = FakedProjectEnvironmentMeta.GetFakedEnvironment();
//		// set class_path of ASTParser.
//		List<String> entries = new ArrayList<String>();
//		String jre_home = System.getProperty("java.home");
//		entries.add(jre_home + "/lib/rt.jar");
//		String[] classpath_array = entries.toArray(new String[entries.size()]);
//		parser.setEnvironment(classpath_array, new String[]{faked_env_path}, new String[]{"UTF-8"}, true);
//	}
	
	public CompilationUnit ParseICompilationUnit(ICompilationUnit icu)
	{
		parser.setSource(icu);
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public CompilationUnit ParseOneClass(IType f)
	{
		parser.setSource(f.getClassFile());
		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);
		return compilationUnit;
	}
	
	public CompilationUnit ParseJavaContent(String package_name, String unit_name, IDocument doc)
	{
		String file_unit_name = unit_name + ".java";
		String proj_name = java_project.getElementName();
		FakeResourceCreationHelper.CreateAndImportFakeJavaFile(proj_name, package_name, file_unit_name, doc);
		IType it = null;
		String full_qualified_name = package_name + (package_name.equals("") ? "" : ".") + unit_name;
		try {
			it = java_project.findType(full_qualified_name);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		CompilationUnit compilationUnit = null;
		if (it != null && it.getCompilationUnit() != null) {
			// parser.setUnitName("/" + proj_name + "/src/" + package_name.replace('.', '/') + (package_name.equals("") ? "" : "/") + file_unit_name);
			// parser.setSource(doc.get().toCharArray());
			parser.setSource(it.getCompilationUnit());
			compilationUnit = (CompilationUnit) parser.createAST(null);
		} else {
			System.err.println("Warning: " + full_qualified_name + " can not be founded!");
		}
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
		return java_project;
	}
	
}
