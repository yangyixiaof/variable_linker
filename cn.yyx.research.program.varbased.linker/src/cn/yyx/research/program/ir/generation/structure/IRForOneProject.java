package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.IRElementPool;
import cn.yyx.research.program.ir.storage.IRGraphManager;

public class IRForOneProject {
	
	IRGraphManager graph_manager = null;
	IRElementPool pool = null;
	
	public IRForOneProject(IRGraphManager graph_manager, IRElementPool pool) {
		this.graph_manager = graph_manager;
		this.pool = pool;
	}
	
	public IRGraphManager GetIRGraphManager() {
		return graph_manager;
	}
	
	public IRElementPool GetIRElementPool() {
		return pool;
	}
	
}
