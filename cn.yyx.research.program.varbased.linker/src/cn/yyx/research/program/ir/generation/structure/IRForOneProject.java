package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.creation.IRElementFactory;

public class IRForOneProject {
	
	IRGraphManager graph_manager = null;
	IRElementFactory pool = null;
	
	public IRForOneProject(IRGraphManager graph_manager, IRElementFactory pool) {
		this.graph_manager = graph_manager;
		this.pool = pool;
	}
	
	public IRGraphManager GetIRGraphManager() {
		return graph_manager;
	}
	
	public IRElementFactory GetIRElementPool() {
		return pool;
	}
	
}
