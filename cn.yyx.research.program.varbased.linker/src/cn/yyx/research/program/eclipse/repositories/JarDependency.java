package cn.yyx.research.program.eclipse.repositories;

public class JarDependency {
	
	private String group = null;
	private String name = null;
	// private String version = null;
	
	public JarDependency(String group, String name) {
		this.group = group;
		this.name = name;
	}

	public String GetGroup() {
		return group;
	}

	public String GetName() {
		return name;
	}

	@Override
	public String toString() {
		return "$group:" + group + ";name:" + name;
	}
	
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof JarDependency) {
			return toString().trim().equals((obj + "").trim());
		}
		return super.equals(obj);
	}
	
}
