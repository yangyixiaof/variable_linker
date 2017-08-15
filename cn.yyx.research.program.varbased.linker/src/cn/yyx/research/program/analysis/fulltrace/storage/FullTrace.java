package cn.yyx.research.program.analysis.fulltrace.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.analysis.fulltrace.storage.connection.DynamicConnection;
import cn.yyx.research.program.analysis.fulltrace.storage.node.DynamicNode;
import cn.yyx.research.program.ir.meta.IRMeta;
import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.connection.ConnectionInfo;
import cn.yyx.research.program.ir.storage.connection.EdgeBaseType;
import cn.yyx.research.program.ir.storage.connection.EdgeTypeUtil;
import cn.yyx.research.program.ir.storage.node.lowlevel.IRForOneBranchControl;
import cn.yyx.research.program.ir.storage.node.lowlevel.IRForOneInstruction;
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
