package cn.yyx.research.program.eclipse.repositories;

import java.util.LinkedList;
import java.util.List;

public class OverAllDependency {
	
	List<RepositoryDependency> urls = new LinkedList<RepositoryDependency>();
	List<JarDependency> jars = new LinkedList<JarDependency>();
	
	public OverAllDependency() {
	}
	
	public void AddJar(JarDependency jd) {
		jars.add(jd);
	}
	
	public List<JarDependency> GetJars() {
		return jars;
	}
	
	public void AddUrl(RepositoryDependency url) {
		urls.add(url);
	}
	
	public List<RepositoryDependency> GetUrls() {
		return urls;
	}
	
}
