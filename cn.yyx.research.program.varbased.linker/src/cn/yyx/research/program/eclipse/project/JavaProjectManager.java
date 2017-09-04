package cn.yyx.research.program.eclipse.project;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;

import cn.yyx.research.program.eclipse.exception.ProjectAlreadyExistsException;

public class JavaProjectManager {

	private static JavaProjectManager unique = new JavaProjectManager();
	protected Map<String, IJavaProject> projects = new TreeMap<String, IJavaProject>();

	private JavaProjectManager() {
	}

	public IJavaProject CreateJavaProject(String projname, List<IClasspathEntry> entries) throws ProjectAlreadyExistsException, CoreException {
		IProject project = null;
		IJavaProject java_project = null;
		{
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject(projname);
			if (project.exists())
			{
				System.err.println("Project " + projname + " already existed! It will be deleted!");
				DeleteJavaProject(projname);
			}
			project.create(null);
			project.open(null);
		}

		{
			IProjectDescription description = project.getDescription();
			description.setNatureIds(new String[] { JavaCore.NATURE_ID });
			project.setDescription(description, null);
		}

		{
			java_project = JavaCore.create(project);

			IFolder binFolder = project.getFolder("bin");
			binFolder.create(false, true, null);
			java_project.setOutputLocation(binFolder.getFullPath(), null);

			java_project.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

			IFolder sourceFolder = project.getFolder("src");
			sourceFolder.create(false, true, null);

			IPackageFragmentRoot root = java_project.getPackageFragmentRoot(sourceFolder);
			IClasspathEntry[] oldEntries = java_project.getRawClasspath();
			IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
			System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);
			newEntries[oldEntries.length] = JavaCore.newSourceEntry(root.getPath());
			java_project.setRawClasspath(newEntries, null);
		}
		// add to set to record.
		projects.put(projname, java_project);

		return java_project;
	}

	public void DeleteJavaProject(String projname) throws CoreException {
		try {
			IProject project = null;
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			project = root.getProject(projname);
			project.delete(true, true, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void DeleteAllJavaProject() throws CoreException {
		List<String> keys = new LinkedList<String>(projects.keySet());
		Iterator<String> kitr = keys.iterator();
		while (kitr.hasNext()) {
			String proj_name = kitr.next();
			projects.remove(proj_name);
			DeleteJavaProject(proj_name);
		}
		projects.clear();
	}
	
	public IJavaProject GetJavaProject(String proj_name) {
		return projects.get(proj_name);
	}

	public static JavaProjectManager UniqueManager() {
		return unique;
	}

}
