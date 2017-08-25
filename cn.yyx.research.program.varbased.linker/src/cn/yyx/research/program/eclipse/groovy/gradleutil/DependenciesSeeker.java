package cn.yyx.research.program.eclipse.groovy.gradleutil;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.builder.AstBuilder;

import cn.yyx.research.program.fileutil.FileUtil;

public class DependenciesSeeker {

	public DependenciesSeeker() {
	}

	public List<GradleDependency> SeekDepemdemcies(File gradle_file) {
		List<GradleDependency> dependencies = new LinkedList<GradleDependency>();
		String gradle_file_content = FileUtil.ReadFromFile(gradle_file);
		AstBuilder builder = new AstBuilder();
		List<ASTNode> nodes = builder.buildFromString(gradle_file_content);
		// System.err.println("Gradle_ASTNode_Size:" + nodes.size());
		for (ASTNode node : nodes) {
			// System.err.println("One_Gradle_ASTNode:" + node);
			GradleParser gp = new GradleParser();
			node.visit(gp);
			dependencies.addAll(gp.GetAllDependencies());
		}
		// SourceUnit unit = SourceUnit.create("gradle", gradle_file_content);
		// unit.parse();
		// unit.completePhase();
		// unit.convert();
		return dependencies;
	}

	// protected List<GradleDependency> VisitScriptCode(SourceUnit source) {
	// System.err.println("VisitScriptCode:Source:" + source.getName());
	// System.err.println("VisitScriptCode:SourceMethodSize:" +
	// source.getAST().getMethods().size());
	// List<GradleDependency> dependencies = new LinkedList<GradleDependency>();
	// // source.getAST().getStatementBlock().visit(transformer);
	// for (Object method : source.getAST().getMethods()) {
	// MethodNode methodNode = (MethodNode) method;
	// GradleParser gp = new GradleParser();
	// methodNode.getCode().visit(gp);
	// dependencies.addAll(gp.GetAllDependencies());
	// }
	// return dependencies;
	// }

}
