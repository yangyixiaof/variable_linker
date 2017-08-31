package cn.yyx.research.program.eclipse.project.meta;

public class FakedProjectEnvironmentMeta {
	
	public static final String FakedProject = "fake_project";
	public static final String FakedEnvironment = "YYX_f_proj_e_d_i_r";
	
	public static String GetFakedEnvironment() {
		return System.getProperty("user.home") + "/" + FakedEnvironment;
	}
	
}
