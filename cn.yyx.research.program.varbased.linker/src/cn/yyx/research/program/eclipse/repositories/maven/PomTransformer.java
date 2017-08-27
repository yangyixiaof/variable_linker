package cn.yyx.research.program.eclipse.repositories.maven;

import java.io.File;

import cn.yyx.research.program.eclipse.repositories.JarDownloader;

public class PomTransformer {
	
	public PomTransformer() {
	}
	
	public void TransformIntoDirectoryAndExecute(File pom_file, File to_dir) {
		PomParser pp = new PomParser();
		pp.Parse(pom_file);
		JarDownloader.DownloadOverAllJar(to_dir, pp.GetOverAllDependency());
	}
	
}
