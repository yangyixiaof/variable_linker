package cn.yyx.research.program.eclipse.repositories.maven;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import cn.yyx.research.program.eclipse.repositories.JarDependency;
import cn.yyx.research.program.eclipse.repositories.OverAllDependency;
import cn.yyx.research.program.eclipse.repositories.RepositoryDependency;

public class PomParser {
	
	OverAllDependency overall_dependencies = new OverAllDependency();
	
	public PomParser() {
	}
	
	public void Parse(File pom_file) {
		try {
			MavenXpp3Reader reader = new MavenXpp3Reader();
			Model model = reader.read(new FileReader(pom_file));
			// System.out.println("Model:" + model.toString());
			List<Repository> repos = model.getRepositories();
			for (Repository repo : repos) {
				// System.err.println("Repository:" + repo);
				if (!(new URL(repo.getUrl()).getHost().equals(new URL("http://central.maven.org/maven2/").getHost()))) {
					overall_dependencies.AddUrl(new RepositoryDependency("maven", repo.getUrl()));
				}
			}
			List<Dependency> depds = model.getDependencies();
			for (Dependency depd : depds) {
				// System.err.println("Dependency:" + depd);
				overall_dependencies.AddJar(new JarDependency(depd.getGroupId(), depd.getArtifactId()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public OverAllDependency GetOverAllDependency() {
		return overall_dependencies;
	}
	
}
