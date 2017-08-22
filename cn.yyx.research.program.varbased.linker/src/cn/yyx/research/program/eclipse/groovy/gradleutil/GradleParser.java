package cn.yyx.research.program.eclipse.groovy.gradleutil;

import java.util.LinkedList;
import java.util.List;

import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;

public class GradleParser extends CodeVisitorSupport {

	protected List<GradleDependency> dependencies = new LinkedList<GradleDependency>();

	@Override
	public void visitMethodCallExpression(MethodCallExpression call) {
		if (!(call.getMethodAsString().equals("buildscript"))) {
			if (call.getMethodAsString().equals("dependencies")) {
				super.visitMethodCallExpression(call);
			}
		}
	}

	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		System.out.println("unknown arg exprs:" + ale);
		List<Expression> expressions = ale.getExpressions();
		if (expressions.size() == 1 && expressions.get(0) instanceof ConstantExpression) {
			String depStr = expressions.get(0).getText();
			String[] deps = depStr.split(":");
			if (deps.length == 3) {
				dependencies.add(new GradleDependency(deps[0], deps[1], deps[2]));
			}
		}
		super.visitArgumentlistExpression(ale);
	}

}
