package cn.yyx.research.program.eclipse.project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.LibraryLocation;

import cn.yyx.research.program.analysis.prepare.PreProcessHelper;
import cn.yyx.research.program.eclipse.exception.NoAnalysisSourceException;
import cn.yyx.research.program.eclipse.exception.ProjectAlreadyExistsException;
import cn.yyx.research.program.eclipse.groovy.gradleutil.DependenciesSeeker;
import cn.yyx.research.program.eclipse.groovy.gradleutil.GradleDependency;
import cn.yyx.research.program.eclipse.jdtutil.JDTParser;
import cn.yyx.research.program.fileutil.FileIterator;
import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.meta.IRResourceMeta;
import cn.yyx.research.program.systemutil.CommandLineUtil;

public class AnalysisEnvironment {

	public static IJavaProject CreateAnalysisEnvironment(ProjectInfo pi)
			throws NoAnalysisSourceException, ProjectAlreadyExistsException, CoreException {
		String user_home = System.getProperty("user.home");
		File dependency_dir = new File(user_home + "/" + IRResourceMeta.ProjectDependencyDirectory);
		if (dependency_dir.exists()) {
			FileUtil.DeleteFile(dependency_dir);
		}
		dependency_dir.mkdir();
		File gradle_dir = new File(dependency_dir + "/gradle_dependencies");
		if (!gradle_dir.exists()) {
			gradle_dir.mkdirs();
		}
		File maven_dir = new File(dependency_dir + "/maven_dependencies");
		if (!maven_dir.exists()) {
			maven_dir.mkdirs();
		}
		IJavaProject java_project = null;
		File dir = null;
		{
			// import legal .java files into IJavaProject.
			Map<String, TreeMap<String, String>> dir_files_map = new TreeMap<String, TreeMap<String, String>>();
			dir = new File(pi.getBasedir());
			if (!dir.exists() || !dir.isDirectory()) {
				throw new NoAnalysisSourceException();
			}
			FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+(?<!-yyx-copy)\\.java$");
			Iterator<File> fitr = fi.EachFileIterator();
			while (fitr.hasNext()) {
				File f = fitr.next();
				String f_norm_path = f.getAbsolutePath().trim().replace('\\', '/');
				// testing
				System.out.println("f_norm_path:" + f_norm_path);
				JDTParser unique_parser = JDTParser.GetUniqueEmptyParser();
				CompilationUnit cu = unique_parser.ParseJavaFile(f);
				PackageDeclaration pack = cu.getPackage();
				if (pack != null) {
					String fname = f.getName();
					String packagename = pack.getName().toString();
					String packagepath = packagename.replace('.', '/');
					String packagepath_with_classfile = packagepath + "/" + fname;
					String class_full_qualified_name = packagename + "."
							+ fname.substring(0, fname.lastIndexOf(".java"));
					if (f_norm_path.endsWith(packagepath_with_classfile)) {
						String f_dir = f_norm_path.substring(0, f_norm_path.lastIndexOf(packagepath_with_classfile))
								.replace('\\', '/');
						while (f_dir.endsWith("/")) {
							f_dir = f_dir.substring(0, f_dir.length() - 1);
						}
						TreeMap<String, String> files_in_dir = dir_files_map.get(f_dir);
						if (files_in_dir == null) {
							files_in_dir = new TreeMap<String, String>();
							dir_files_map.put(f_dir, files_in_dir);
						}
						// How to judge which java file is more complete? Currently, just judge the last
						// update time of a file.
						if (files_in_dir.containsKey(class_full_qualified_name)) {
							String full_name = files_in_dir.get(class_full_qualified_name);
							File full_f = new File(full_name);
							if (f.lastModified() > full_f.lastModified()) {
								files_in_dir.put(class_full_qualified_name, f_norm_path);
							}
						} else {
							files_in_dir.put(class_full_qualified_name, f_norm_path);
						}
					}
				}
			}
			// all_need_handle_files.put(f_norm_path, f_dir);

			// Create and fill the source folder of the project.
			JavaProjectManager manager = JavaProjectManager.UniqueManager();
			java_project = manager.CreateJavaProject(pi.getName());
			JavaImportOperation.ImportFileSystem(java_project, dir_files_map);
		}

		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		IVMInstall vmInstall = JavaRuntime.getDefaultVMInstall();
		LibraryLocation[] locations = JavaRuntime.getLibraryLocations(vmInstall);
		for (LibraryLocation element : locations) {
			entries.add(JavaCore.newLibraryEntry(element.getSystemLibraryPath(), null, null));
		}

		{
			FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+\\.gradle$");
			Iterator<File> fitr = fi.EachFileIterator();
			int index = 0;
			while (fitr.hasNext()) {
				File f = fitr.next();
				index++;
				File f_dir = new File(gradle_dir.getAbsolutePath() + "/" + index);
				f_dir.mkdirs();
				File gradle = new File(f_dir.getAbsolutePath() + "/" + "build.gradle");
				DependenciesSeeker seeker = new DependenciesSeeker();
				List<GradleDependency> depds = seeker.SeekDepemdemcies(f);
				if (!depds.isEmpty()) {
					FileWriter fw = null;
					try {
						fw = new FileWriter(gradle);
						fw.append("apply plugin: 'java'\n" + "repositories { mavenCentral() }\n" + "dependencies {\n");
						Iterator<GradleDependency> ditr = depds.iterator();
						while (ditr.hasNext()) {
							GradleDependency gdepd = ditr.next();
							fw.write("comple" + " group: '" + gdepd.getGroup() + "', name: '" + gdepd.getName() + "', version: '" + gdepd.getVersion() + "'\n");
						}
						fw.write("}\n" + "task download(type: Copy) {\n" + "  from configurations.runtime\n"
								+ "  into 'target'\n" + "}");
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							fw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				CommandLineUtil.ExecuteCommand(f_dir, "gradle download");
			}
			IterateAllJarsToFillEntries(gradle_dir, entries);
		}

		{
			FileIterator fi = new FileIterator(dir.getAbsolutePath(), "^pom\\.xml$");
			Iterator<File> fitr = fi.EachFileIterator();
			int index = 0;
			while (fitr.hasNext()) {
				File f = fitr.next();
				index++;
				File f_dir = new File(maven_dir.getAbsolutePath() + "/" + index);
				f_dir.mkdirs();
				File pom = new File(f_dir.getAbsolutePath() + "/" + "pom.xml");
				FileUtil.CopyFile(f, pom);
				CommandLineUtil.ExecuteCommand(f_dir, "mvn dependency:copy-dependencies");
			}
			IterateAllJarsToFillEntries(maven_dir, entries);
		}

		{
			IterateAllJarsToFillEntries(dir, entries);
		}

		// add libs to project class path
		java_project.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

		PreProcessHelper.EliminateAllParameterizedTypeAndReformAssignment(java_project);
		return java_project;
	}
	
	private static void IterateAllJarsToFillEntries(File dir, List<IClasspathEntry> entries) {
		FileIterator fi = new FileIterator(dir.getAbsolutePath(), ".+\\.jar$");
		Iterator<File> fitr = fi.EachFileIterator();
		while (fitr.hasNext()) {
			File f = fitr.next();
			entries.add(JavaCore.newLibraryEntry(new Path(f.getAbsolutePath()), null, null));
		}
	}

	public static void DeleteAnalysisEnvironment(ProjectInfo pi) throws CoreException {
		JavaProjectManager.UniqueManager().DeleteJavaProject(pi.getName());
	}

	public static void DeleteAllAnalysisEnvironment() throws CoreException {
		JavaProjectManager.UniqueManager().DeleteAllJavaProject();
	}

}
