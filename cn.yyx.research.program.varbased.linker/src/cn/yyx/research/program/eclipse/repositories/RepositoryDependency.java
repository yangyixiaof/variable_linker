package cn.yyx.research.program.eclipse.repositories;

public class RepositoryDependency {
	
	private String repository_type = "maven";
	private String address = "";
	
	public RepositoryDependency(String repository_type, String address) {
		this.repository_type = repository_type;
		this.address = address;
	}

	public String GetRepositoryType() {
		return repository_type;
	}

	public String GetAddress() {
		return address;
	}

}
