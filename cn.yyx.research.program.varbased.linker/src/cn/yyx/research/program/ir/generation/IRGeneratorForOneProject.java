package cn.yyx.research.program.ir.generation;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cn.yyx.research.logger.DebugLogger;
import cn.yyx.research.program.eclipse.jdtutil.JDTParser;
import cn.yyx.research.program.eclipse.searchutil.EclipseSearchForICompilationUnits;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRGeneratorForOneProject {
	
	IJavaProject java_project = null;
	
	public IRGeneratorForOneProject(IJavaProject java_project) {
		this.java_project = java_project;
	}
	
	public IRForOneProject GenerateForOneProject() {
		IRGraphManager graph_manager = new IRGraphManager();
		IRElementFactory ele_factory = new IRElementFactory();
		IRStatementFactory stmt_factory = new IRStatementFactory();
		List<ICompilationUnit> units = null;
		try {
			units = EclipseSearchForICompilationUnits.SearchForAllICompilationUnits(java_project);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		DebugLogger.Log("ICompilationUnit_size:" + units.size());
		if (units != null) {
			for (ICompilationUnit icu : units) {
				CompilationUnit cu = JDTParser.CreateJDTParserWithJavaProject(java_project).ParseICompilationUnit(icu);
				IRGeneratorForClassesInICompilationUnit irgfcicu = new IRGeneratorForClassesInICompilationUnit(
						java_project, graph_manager, ele_factory, stmt_factory, icu, cu);
				cu.accept(irgfcicu);
			}
		}
		return new IRForOneProject(graph_manager, ele_factory, stmt_factory);
	}
	
}
