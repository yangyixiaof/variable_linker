package cn.yyx.research.program.eclipse.groovy.gradleutil;

public class GradleDependency {
	
	private String group = null;
	private String name = null;
	private String version = null;
	
	public GradleDependency(String group, String name, String version) {
		this.setGroup(group);
		this.setName(name);
		this.setVersion(version);
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
}
