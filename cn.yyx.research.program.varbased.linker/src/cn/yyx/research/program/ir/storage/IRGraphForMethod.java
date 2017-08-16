package cn.yyx.research.program.ir.storage;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;

public class IRGraphForMethod extends IRGraph {
	
	List<IRJavaElementNode> params = new LinkedList<IRJavaElementNode>();
	IRJavaElementNode return_element_node = null;
	
	public IRGraphForMethod(List<IRJavaElementNode> params, IRJavaElementNode return_element_node) {
		this.params.addAll(params);
		this.return_element_node = return_element_node;
		Iterator<IRJavaElementNode> pitr = params.iterator();
		while (pitr.hasNext()) {
			IRJavaElementNode irjen = pitr.next();
			AddNonVirtualVariableNode(irjen);
		}
		AddNonVirtualVariableNode(return_element_node);
	}
	
	public List<IRJavaElementNode> GetParameterElementNodes() {
		return params;
	}
	
	public IRJavaElementNode GetReturnElementNode() {
		return return_element_node;
	}
	
}
