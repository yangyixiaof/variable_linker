package cn.yyx.research.program.eclipse.groovy.gradleutil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

import cn.yyx.research.program.fileutil.FileUtil;

public class DependenciesSeeker {

	public DependenciesSeeker() {
	}

	public List<GradleDependency> SeekDepemdemcies(File gradle_file) {
		String gradle_file_content = FileUtil.ReadFromFile(gradle_file);
		SourceUnit unit = SourceUnit.create("gradle", gradle_file_content);
		unit.parse();
		unit.completePhase();
		unit.convert();
		return VisitScriptCode(unit);
	}

	protected List<GradleDependency> VisitScriptCode(SourceUnit source) {
		List<GradleDependency> dependencies = new LinkedList<GradleDependency>();
		// source.getAST().getStatementBlock().visit(transformer);
		for (Object method : source.getAST().getMethods()) {
			MethodNode methodNode = (MethodNode) method;
			GradleParser gp = new GradleParser();
			methodNode.getCode().visit(gp);
			dependencies.addAll(gp.GetAllDependencies());
		}
		return dependencies;
	}

}
