package cn.yyx.research.program.eclipse.repositories.gradle;

import java.util.List;

import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ArgumentListExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;

import cn.yyx.research.program.eclipse.repositories.JarDependency;
import cn.yyx.research.program.eclipse.repositories.OverAllDependency;
import cn.yyx.research.program.eclipse.repositories.RepositoryDependency;
import cn.yyx.research.program.systemutil.URLUtil;

public class GradleParser extends CodeVisitorSupport {
	
	protected OverAllDependency overall_dependencies = new OverAllDependency();
	
	protected boolean in_dependencies = false;
	protected boolean in_repositories = false;
	protected boolean in_repository_types = false;
	protected String type = null;

	@Override
	public void visitMethodCallExpression(MethodCallExpression call) {
		// System.err.println("in_repositories:" + in_repositories + ";MethodCallExpression:" + call);
		if (call.getMethodAsString().equals("dependencies")) {
			in_dependencies = true;
		}
		if (in_repository_types) {
			if (call.getMethodAsString().equals("url")) {
				Expression args = call.getArguments();
				if (args instanceof ArgumentListExpression) {
					List<Expression> exprs = ((ArgumentListExpression)args).getExpressions();
					if (exprs != null && exprs.size() == 1) {
						Expression expr = exprs.get(0);
						if (expr instanceof ConstantExpression) {
							String url = ((ConstantExpression)expr).getText();
							if (URLUtil.IsURLValid(url)) {
								overall_dependencies.AddUrl(new RepositoryDependency(type, url));
							}
						}
					}
				}
			}
		}
		if (in_repositories) {
			// System.err.println("known args:" + call.getArguments().getText() + ";method:" + call.getMethodAsString());
			if (call.getMethodAsString().equals("jcenter") || call.getMethodAsString().equals("maven") || call.getMethodAsString().equals("ivy")) {
				// System.err.println("unknown args:" + call.getArguments().getText());
				type = call.getMethodAsString();
				// System.err.println("type:" + type + ";args:" + call.getArguments() + ";");
				if (call.getArguments().getText().equals("()")) {
					overall_dependencies.AddUrl(new RepositoryDependency(type, null));
				} else {
					in_repository_types = true;
				}
			}
		}
		if (call.getMethodAsString().equals("repositories")) {
			in_repositories = true;
		}
		super.visitMethodCallExpression(call);
		if (call.getMethodAsString().equals("dependencies")) {
			in_dependencies = false;
		}
		if (in_repositories) {
			if (call.getMethodAsString().equals("jcenter") || call.getMethodAsString().equals("maven") || call.getMethodAsString().equals("ivy") || call.getMethodAsString().equals("ivy")) {
				in_repository_types = false;
				type = null;
			}
		}
		if (call.getMethodAsString().equals("repositories")) {
			in_repositories = false;
		}
	}
	
	@Override
	public void visitMapExpression(MapExpression expression) {
		// System.err.println("InDependency:" + in_dependencies + ";MapExpression:" + expression);
		if (in_dependencies) {
			List<MapEntryExpression> map_entries = expression.getMapEntryExpressions();
			if (map_entries.size() == 3 && MapEntryExpressionKeyValueAreConstantExpressionCondition(map_entries.get(0), "group") && MapEntryExpressionKeyValueAreConstantExpressionCondition(map_entries.get(1), "name") && MapEntryExpressionKeyValueAreConstantExpressionCondition(map_entries.get(2), "version")) {
				overall_dependencies.AddJar(new JarDependency(((ConstantExpression)map_entries.get(0).getValueExpression()).getText(), ((ConstantExpression)map_entries.get(1).getValueExpression()).getText()));
			}
		}
		super.visitMapExpression(expression);
	}
	
	protected boolean MapEntryExpressionKeyValueAreConstantExpressionCondition(MapEntryExpression expr, String expected_key) {
		Expression key = expr.getKeyExpression();
		Expression value = expr.getValueExpression();
		if ((key instanceof ConstantExpression) && (value instanceof ConstantExpression) && ((ConstantExpression)key).getText().trim().equals(expected_key.trim())) {
			return true;
		}
		return false;
	}
	
//	@Override
//	public void visitTupleExpression(TupleExpression expression) {
//		NamedArgumentListExpression e;
//		super.visitTupleExpression(expression);
//	}
	
	@Override
	public void visitArgumentlistExpression(ArgumentListExpression ale) {
		// System.err.println(";ArgumentListExpression:" + ale); // "InDependency:" + in_dependencies + 
		if (in_dependencies) {
			List<Expression> expressions = ale.getExpressions();
//			System.err.println("OneArgExpression:\n");
//			for (Expression expr : expressions) {
//				System.err.println(expr + ";\n");
//			}
//			System.err.println("OneArgExpressionEnd.\n");
			if (expressions.size() == 1 && expressions.get(0) instanceof ConstantExpression) {
				String depStr = expressions.get(0).getText();
				String[] deps = depStr.split(":");
				if (deps.length == 3) {
					overall_dependencies.AddJar(new JarDependency(deps[0], deps[1]));
				}
			}
		}
		super.visitArgumentlistExpression(ale);
	}
	
	public OverAllDependency GetOverAllDependency() {
		return overall_dependencies;
	}

}
