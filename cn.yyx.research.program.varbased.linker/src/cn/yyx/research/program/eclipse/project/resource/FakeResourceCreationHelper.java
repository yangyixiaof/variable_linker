package cn.yyx.research.program.eclipse.project.resource;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.IDocument;

import cn.yyx.research.program.eclipse.project.JavaImportOperation;
import cn.yyx.research.program.eclipse.project.JavaProjectManager;
import cn.yyx.research.program.eclipse.project.meta.FakedProjectEnvironmentMeta;
import cn.yyx.research.program.fileutil.FileUtil;

public class FakeResourceCreationHelper {
	
	public static void CreateAndImportFakeJavaFile(String project_name, String package_name, String file_unit_name, IDocument doc) {
		String[] dirs = package_name.split("\\.");
		String base_dir = FakedProjectEnvironmentMeta.GetFakedEnvironment();
		String base_dir_copy = base_dir;
		if (dirs != null && dirs.length > 0) {
			for (int i=0;i<dirs.length;i++) {
				String one = dirs[i];
				String one_dir = base_dir + "/" + one;
				File file_one_dir = new File(one_dir);
				if (!file_one_dir.exists()) {
					file_one_dir.mkdirs();
				}
			}
		}
		File dest = new File(base_dir + "/" + file_unit_name);
		FileUtil.WriteToFile(dest, doc.get());
		Map<String, TreeMap<String, String>> dir_files_map = new TreeMap<String, TreeMap<String, String>>();
		TreeMap<String, String> pack_unit = new TreeMap<String, String>();
		pack_unit.put(package_name, dest.getAbsolutePath());
		dir_files_map.put(base_dir_copy, pack_unit);
		IJavaProject proj = JavaProjectManager.UniqueManager().GetJavaProject(project_name);
		try {
			JavaImportOperation.ImportFileSystem(proj, dir_files_map);
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
	}
	
}
