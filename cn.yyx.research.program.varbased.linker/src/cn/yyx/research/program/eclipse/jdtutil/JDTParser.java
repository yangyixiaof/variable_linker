package cn.yyx.research.program.eclipse.jdtutil;

import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.IDocument;

import cn.yyx.research.program.eclipse.project.AnalysisEnvironment;

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
	
	public CompilationUnit ParseJavaContent(String package_name, String file_unit_name, IDocument doc)
	{
//		String[] dirs = package_name.split("\\.");
//		String base_dir = FakedProjectEnvironmentMeta.GetFakedEnvironment();
//		if (dirs != null && dirs.length > 0) {
//			for (int i=0;i<dirs.length;i++) {
//				String one = dirs[i];
//				String one_dir = base_dir + "/" + one;
//				File file_one_dir = new File(one_dir);
//				if (!file_one_dir.exists()) {
//					file_one_dir.mkdirs();
//				}
//			}
//		}
//		File dest = new File(base_dir + "/" + file_unit_name);
//		FileUtil.WriteToFile(dest, doc.get());
//		Map<String, TreeMap<String, String>> dir_files_map = new TreeMap<String, TreeMap<String, String>>();
//		TreeMap<String, String> pack_unit = new TreeMap<String, String>();
//		pack_unit.put(package_name, file_unit_name);
//		dir_files_map.put(project_name, pack_unit);
//		IJavaProject proj = JavaProjectManager.UniqueManager().GetJavaProject(project_name);
//		try {
//			JavaImportOperation.ImportFileSystem(proj, dir_files_map);
//		} catch (JavaModelException e) {
//			e.printStackTrace();
//		}
		parser.setUnitName("/" + java_project.getElementName() + "/src/" + package_name.replace('.', '/') + (package_name.equals("") ? "" : "/") + file_unit_name);
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
		return java_project;
	}
	
}
