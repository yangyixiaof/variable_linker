package cn.yyx.research.program.snippet.bootstrap;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IJavaProject;

import cn.yyx.research.logger.DebugLogger;
import cn.yyx.research.program.eclipse.exception.WrongArgumentException;
import cn.yyx.research.program.eclipse.project.AnalysisEnvironment;
import cn.yyx.research.program.eclipse.project.ProjectInfo;
import cn.yyx.research.program.ir.meta.IRControlMeta;
import cn.yyx.research.program.ir.visual.dot.generation.GenerateDotForIRGraphs;
import cn.yyx.research.program.ir.visual.meta.DotMeta;
import cn.yyx.research.program.systemutil.EnvironmentUtil;
import cn.yyx.research.program.systemutil.SystemUtil;
import cn.yyx.research.test.TestJavaSearch;

public class LinkExtractor implements IApplication {
	
	public static IJavaProject LoadProjectAccordingToArgs(String[] args) throws Exception 
	{
		if (args.length != 2) {
			throw new WrongArgumentException();
		}
		DebugLogger.Log("Just for test, this is the args:", args);
		
		// load projects
		ProjectInfo epi = new ProjectInfo(args[0], args[1]);//args[0]:no_use args[1]:D:/eclipse-workspace-pool/eclipse-rcp-neon-codecompletion/cn.yyx.research.program.snippet.extractor
		IJavaProject jproj = AnalysisEnvironment.CreateAnalysisEnvironment(epi);
		
		return jproj;
	}
	
	@Override
	public Object start(IApplicationContext context) throws Exception {
		EnvironmentUtil.Clear();
		IJavaProject java_project = LoadProjectAccordingToArgs((String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS));
		try {
			// DebugLogger.Log("Start is invoked!");
			// SystemUtil.Delay(1000);	
			// testing.
			if (IRControlMeta.test) {
				TestJavaSearch.TestInAll(java_project);
			} else {
				IRGeneratorForOneProject.GenerateForAllICompilationUnits(java_project);
				// generate and print each local method.
				GenerateDotForIRGraphs irproj_local_generation = new GenerateDotForIRGraphs(DotMeta.ProjectEachMethodDotDir, DotMeta.ProjectEachMethodPicDir);
				irproj_local_generation.GenerateDots();
				
				// generate and print all methods connected.
				IRGeneratorForOneProject irinstance = IRGeneratorForOneProject.GetInstance();
				// TODO 
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			AnalysisEnvironment.DeleteAllAnalysisEnvironment();
		}
		SystemUtil.Flush();
		SystemUtil.Delay(1000);
		return IApplication.EXIT_OK;
	}
	
	@Override
	public void stop() {
//		DebugLogger.Log("Force Stop is invoked!");
//		try {
//			AnalysisEnvironment.DeleteAllAnalysisEnvironment();
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
	}
	
}
