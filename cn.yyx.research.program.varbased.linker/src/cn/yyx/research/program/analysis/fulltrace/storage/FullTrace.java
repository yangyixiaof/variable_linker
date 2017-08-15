package cn.yyx.research.program.analysis.fulltrace.storage;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.visual.node.IVNode;
import cn.yyx.research.program.ir.visual.node.connection.IVConnection;
import cn.yyx.research.program.ir.visual.node.container.IVNodeContainer;

public class FullTrace implements IVNodeContainer {
	
	Set<IRGraph> root_graphs = new HashSet<IRGraph>();
	
	public FullTrace() {
	}

	@Override
	public Set<IVConnection> GetOutConnection(IVNode source) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
