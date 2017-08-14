package cn.yyx.research.program.ir.storage;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRGraphForMethod extends IRGraph {
	
	List<IRJavaElementNode> params = new LinkedList<IRJavaElementNode>();
	IRJavaElementNode return_element_node = null;
	
	public IRGraphForMethod(List<IRJavaElementNode> params, IRJavaElementNode return_element_node) {
		this.params.addAll(params);
		this.return_element_node = return_element_node;
	}
	
}
