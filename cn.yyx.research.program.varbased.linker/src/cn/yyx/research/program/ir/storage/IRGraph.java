package cn.yyx.research.program.ir.storage;

import java.util.HashSet;
import java.util.Set;

import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElement;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodInvocation;

public class IRGraph {
	
	IIRNode root = null;
	Set<IRJavaElement> java_elements = new HashSet<IRJavaElement>();
	Set<IRSourceMethodInvocation> source_method_invokes = new HashSet<IRSourceMethodInvocation>();
	
	public IRGraph() {
	}
	
	
	
}
