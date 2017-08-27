package cn.yyx.research.program.eclipse.repositories.gradle;

import java.io.File;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.builder.AstBuilder;

import cn.yyx.research.program.eclipse.repositories.JarDownloader;
import cn.yyx.research.program.fileutil.FileUtil;

public class GradleTransformer {

	public GradleTransformer() {
	}

	public void TransformIntoDirectoryAndExecute(File gradle_file, File to_dir) {
		String gradle_file_content = FileUtil.ReadFromFile(gradle_file);
		AstBuilder builder = new AstBuilder();
		List<ASTNode> nodes = builder.buildFromString(gradle_file_content);
		GradleParser gp = new GradleParser();
		for (ASTNode node : nodes) {
			node.visit(gp);
		}
		JarDownloader.DownloadOverAllJar(to_dir, gp.GetOverAllDependency());
	}
	
}
