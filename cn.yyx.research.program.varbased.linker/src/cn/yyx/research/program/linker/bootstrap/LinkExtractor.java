package cn.yyx.research.program.linker.bootstrap;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IJavaProject;

import cn.yyx.research.logger.DebugLogger;
import cn.yyx.research.program.analysis.fulltrace.generation.IRGeneratorForFullTrace;
import cn.yyx.research.program.eclipse.exception.WrongArgumentException;
import cn.yyx.research.program.eclipse.project.AnalysisEnvironment;
import cn.yyx.research.program.eclipse.project.ProjectInfo;
import cn.yyx.research.program.ir.generation.IRGeneratorForOneProject;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
import cn.yyx.research.program.ir.meta.IRControlMeta;
import cn.yyx.research.program.ir.visual.dot.generation.ConnectionOnlyDotGenerator;
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
		// waiting to initialize the workbench.
		SystemUtil.Delay(5000);
		EnvironmentUtil.Clear();
		IJavaProject java_project = LoadProjectAccordingToArgs((String[])context.getArguments().get(IApplicationContext.APPLICATION_ARGS));
		try {
			// DebugLogger.Log("Start is invoked!");
			// SystemUtil.Delay(1000);	
			// testing.
			if (IRControlMeta.test) {
				TestJavaSearch.TestInAll(java_project);
			} else {
				// generate and print each local method.
				SystemUtil.Delay(5000);
				IRGeneratorForOneProject irgfop = new IRGeneratorForOneProject(java_project);
				IRForOneProject one_project = irgfop.GenerateForOneProject();
				
				ConnectionOnlyDotGenerator irproj_local_generation = new ConnectionOnlyDotGenerator(DotMeta.ProjectEachMethodDotDir, DotMeta.ProjectEachMethodPicDir, one_project);
				irproj_local_generation.GenerateDotsAndPrintToPictures();
				
				// generate and print all methods connected.
				IRGeneratorForFullTrace irgft = new IRGeneratorForFullTrace(one_project.GetIRGraphManager());
				irgft.GenerateFullTraceOnInitialIRGraphs();
				
				ConnectionOnlyDotGenerator irproj_global_generation = new ConnectionOnlyDotGenerator(DotMeta.ProjectFullTraceDotDir, DotMeta.ProjectFullTracePicDir, one_project);
				irproj_global_generation.GenerateDotsAndPrintToPictures();
			}
		} catch (Exception e) {
			e.printStackTrace();
			AnalysisEnvironment.DeleteAllAnalysisEnvironment();
		}
		SystemUtil.Flush();
		SystemUtil.Delay(2000);
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
