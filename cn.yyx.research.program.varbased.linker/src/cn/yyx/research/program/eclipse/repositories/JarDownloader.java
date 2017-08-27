package cn.yyx.research.program.eclipse.repositories;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import cn.yyx.research.program.systemutil.CommandLineUtil;

public class JarDownloader {
	
	public JarDownloader() {
	}

	public static void DownloadJar(File to_dir, List<RepositoryDependency> repo_depds, JarDependency jdepd) {
		File gradle = new File(to_dir.toString() + "/" + "build.gradle");
		FileWriter fw = null;
		try {
			fw = new FileWriter(gradle);
			final String gap = "  ";
			fw.write("apply plugin: 'java'\n");
			fw.write("repositories { mavenCentral() }\n");
			fw.write("dependencies {\n");
			fw.write(gap + "compile group: '" + jdepd.GetGroup() + "', name: '" + jdepd.GetName() + "', version: '+'");
			fw.write("}\n");
			fw.write("task download(type: Copy) {\n");
			fw.write(gap + "from configurations.runtime\n");
			fw.write(gap + "into 'target'\n");
			fw.write("}\n");
			fw.flush();
		} catch (Exception e) {
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
			}
		}
		CommandLineUtil.ExecuteCommand(to_dir, "gradle download"); // mvn dependency:copy-dependencies
	}
	
	public static void DownloadOverAllJar(File to_dir, OverAllDependency overall_depend) {
		List<JarDependency> jars = overall_depend.GetJars();
		Iterator<JarDependency> jar_itr = jars.iterator();
		int index = 0;
		while (jar_itr.hasNext()) {
			JarDependency jdepd = jar_itr.next();
			File dir = new File(to_dir.getAbsolutePath() + "/" + index);
			dir.mkdir();
			JarDownloader.DownloadJar(dir, overall_depend.GetUrls(), jdepd);
			index++;
		}
	}
	
}
