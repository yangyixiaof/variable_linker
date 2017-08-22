package cn.yyx.research.program.eclipse.groovy.gradleutil;

import org.codehaus.groovy.ast.GroovyCodeVisitor;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.control.SourceUnit;

public class DependenciesSeeker {

	public DependenciesSeeker() {
	}

	public void SeekDepemdemcies(String gradle_file_string) {
		SourceUnit unit = SourceUnit.create("gradle", gradle_file_string);
		unit.parse();
		unit.completePhase();
		unit.convert();
		VisitScriptCode(unit, new GradleParser());
	}

	protected void VisitScriptCode(SourceUnit source, GroovyCodeVisitor transformer) {
		source.getAST().getStatementBlock().visit(transformer);
		for (Object method : source.getAST().getMethods()) {
			MethodNode methodNode = (MethodNode) method;
			methodNode.getCode().visit(transformer);
		}
	}

}
