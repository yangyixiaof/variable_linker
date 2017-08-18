package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;

public class IRForOneProject {
	
	IRGraphManager graph_manager = null;
	IRElementFactory ele_factory = null;
	IRStatementFactory stmt_factory = null;
	
	public IRForOneProject(IRGraphManager graph_manager, IRElementFactory ele_factory, IRStatementFactory stmt_factory) {
		this.graph_manager = graph_manager;
		this.ele_factory = ele_factory;
		this.stmt_factory = stmt_factory;
	}
	
	public IRGraphManager GetIRGraphManager() {
		return graph_manager;
	}
	
	public IRElementFactory GetIRElementPool() {
		return ele_factory;
	}
	
}
