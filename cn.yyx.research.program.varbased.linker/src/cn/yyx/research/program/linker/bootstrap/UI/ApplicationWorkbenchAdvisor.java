package cn.yyx.research.program.linker.bootstrap.UI;

import org.eclipse.ui.application.WorkbenchAdvisor;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
		
	@Override
	public String getInitialWindowPerspectiveId() {
		return "cn.yyx.research.program.varbased.linker.app_perspective";
	}

}
