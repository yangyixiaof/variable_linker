package cn.yyx.research.program.systemutil;

import java.io.File;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.meta.IRResourceMeta;
import cn.yyx.research.program.ir.visual.meta.DotMeta;

public class EnvironmentUtil {
	
	public EnvironmentUtil() {
	}
	
	public static boolean IsWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("windows")>=0;
	}

	public static void Clear() {
		File f = null;
		f = new File(DotMeta.ProjectFullTraceDotDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		f = new File(DotMeta.ProjectFullTracePicDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		f = new File(DotMeta.ProjectEachMethodDotDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		f = new File(DotMeta.ProjectEachMethodPicDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		f = new File(DotMeta.DebugDotDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		f = new File(DotMeta.DebugPicDir);
		if (f.exists()) {
			FileUtil.DeleteFile(f);
		}
		String user_home = System.getProperty("user.home");
		File dependency_dir = new File(user_home + "/" + IRResourceMeta.ProjectDependencyDirectory);
		if (dependency_dir.exists()) {
			FileUtil.DeleteFile(dependency_dir);
		}
	}
	
}
