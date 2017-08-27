package cn.yyx.research.program.eclipse.repositories;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
	
	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		Set<RepositoryDependency> url_set = new HashSet<RepositoryDependency>(urls);
		Set<JarDependency> jar_set = new HashSet<JarDependency>(jars);
		build.append("Repos:" + url_set.toString() + "#Jars:" + jar_set.toString());
		return build.toString();
	}
	
}
