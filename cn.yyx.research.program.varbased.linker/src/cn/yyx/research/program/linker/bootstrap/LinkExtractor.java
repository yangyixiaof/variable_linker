package cn.yyx.research.program.linker.bootstrap;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import cn.yyx.research.logger.DebugLogger;
import cn.yyx.research.program.analysis.fulltrace.generation.IRGeneratorForFullTrace;
import cn.yyx.research.program.eclipse.exception.WrongArgumentException;
import cn.yyx.research.program.eclipse.project.AnalysisEnvironment;
import cn.yyx.research.program.eclipse.project.ProjectInfo;
import cn.yyx.research.program.ir.generation.IRGeneratorForOneProject;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
import cn.yyx.research.program.ir.visual.dot.generation.ConnectionOnlyDotGenerator;
import cn.yyx.research.program.ir.visual.meta.DotMeta;
import cn.yyx.research.program.linker.bootstrap.UI.ApplicationWorkbenchAdvisor;
import cn.yyx.research.program.linker.bootstrap.meta.BootstrapMeta;
import cn.yyx.research.program.systemutil.EnvironmentUtil;
import cn.yyx.research.program.systemutil.SystemUtil;
import cn.yyx.research.test.TestRoot;

public class LinkExtractor implements IApplication {

	public static IJavaProject LoadProjectAccordingToArgs(String[] args) throws Exception {
		if (args.length != 2) {
			throw new WrongArgumentException();
		}
		DebugLogger.Log("Just for test, this is the args:", args);

		// load projects
		ProjectInfo epi = new ProjectInfo(args[0], args[1]);// args[0]:no_use
															// args[1]:D:/eclipse-workspace-pool/eclipse-rcp-neon-codecompletion/cn.yyx.research.program.snippet.extractor
		IJavaProject jproj = AnalysisEnvironment.CreateAnalysisEnvironment(epi);

		return jproj;
	}

	@Override
	public Object start(IApplicationContext context) throws Exception {
		// waiting to initialize the workbench.
		EnvironmentUtil.Clear();
		Display display = PlatformUI.createDisplay();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!PlatformUI.isWorkbenchRunning()) {
					DebugLogger.Log("Waiting the creation of the workbench.");
					SystemUtil.Delay(1000);
				}
				// load and execute the project.
				IJavaProject java_project = null;
				try {
					java_project = LoadProjectAccordingToArgs(
							(String[]) context.getArguments().get(IApplicationContext.APPLICATION_ARGS));
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				try {
					// DebugLogger.Log("Start is invoked!");
					// SystemUtil.Delay(1000);
					// testing.
					if (BootstrapMeta.test) {
						TestRoot.TestInAll();
						// TestJavaSearch.TestInAll(java_project);
					} else {
						// generate and print each local method.
						SystemUtil.Delay(1000);
						IRGeneratorForOneProject irgfop = new IRGeneratorForOneProject(java_project);
						IRForOneProject one_project = irgfop.GenerateForOneProject();
//						{
//							ConnectionOnlyDotGenerator irproj_local_generation = new ConnectionOnlyDotGenerator(
//									DotMeta.ProjectEachMethodDotDir, DotMeta.ProjectEachMethodPicDir, one_project);
//							irproj_local_generation.GenerateDotsAndPrintToPictures();
//						}
						// generate and print all methods connected.
						IRGeneratorForFullTrace irgft = new IRGeneratorForFullTrace(one_project.GetIRGraphManager());
						irgft.GenerateFullTraceOnInitialIRGraphs();
						one_project.RefineSelf();
						{
							ConnectionOnlyDotGenerator irproj_global_generation = new ConnectionOnlyDotGenerator(
									DotMeta.ProjectFullTraceDotDir, DotMeta.ProjectFullTracePicDir, one_project);
							irproj_global_generation.GenerateDotsAndPrintToPictures();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						AnalysisEnvironment.DeleteAllAnalysisEnvironment();
					} catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
				SystemUtil.Flush();
				SystemUtil.Delay(1000);
				display.syncExec(new Runnable() {
					@Override
					public void run() {
						PlatformUI.getWorkbench().close();
					}
				});
			}
		}).start();
		DebugLogger.Log("Workbench created!");
		PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
		display.dispose();
		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// DebugLogger.Log("Force Stop is invoked!");
		// try {
		// AnalysisEnvironment.DeleteAllAnalysisEnvironment();
		// } catch (CoreException e) {
		// e.printStackTrace();
		// }
	}

}
