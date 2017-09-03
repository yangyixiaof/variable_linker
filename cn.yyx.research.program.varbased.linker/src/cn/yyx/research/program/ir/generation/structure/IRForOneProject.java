package cn.yyx.research.program.ir.generation.structure;

import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;
import cn.yyx.research.program.linker.bootstrap.meta.BootstrapMeta;

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
	
	public IRStatementFactory GetIRStatementPool() {
		return stmt_factory;
	}
	
	public void RefineSelf() {
		ele_factory.RefineSelf();
		stmt_factory.RefineSelf();
		if (BootstrapMeta.check_every_ir_statement) {
			stmt_factory.CheckEveryStatement();
		}
	}
	
}
