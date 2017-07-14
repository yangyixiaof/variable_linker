package cn.yyx.research.program.ir.storage.connection;

public class VariableConnect extends Connect {
	
	private int ele_index = -1;
	
	public VariableConnect(int ele_index) {
		this.ele_index = ele_index;
	}
	
	public int GetVariableHoleIndex() {
		return ele_index;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof VariableConnect) {
			VariableConnect vc = (VariableConnect)obj;
			if (ele_index == vc.ele_index) {
				return true;
			}
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return ele_index;
	}
	
}
