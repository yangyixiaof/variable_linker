package cn.yyx.research.program.ir.storage.connection;

public class Connect {
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Connect) {
			return true;
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public String toString() {
		return "C";
	}
	
}
