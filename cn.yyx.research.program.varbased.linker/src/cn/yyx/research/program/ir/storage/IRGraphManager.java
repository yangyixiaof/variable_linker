package cn.yyx.research.program.ir.storage;

import java.util.LinkedList;
import java.util.List;

public class IRGraphManager {
	
	List<IRGraph> graphs = new LinkedList<IRGraph>();
	
	public void AddIRGraph(IRGraph graph) {
		graphs.add(graph);
	}
	
}
