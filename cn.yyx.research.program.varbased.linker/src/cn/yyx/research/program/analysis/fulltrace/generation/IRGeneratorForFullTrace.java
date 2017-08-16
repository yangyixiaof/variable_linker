package cn.yyx.research.program.analysis.fulltrace.generation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import cn.yyx.research.program.ir.storage.IRGraph;
import cn.yyx.research.program.ir.storage.IRGraphForMethod;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodParamElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodReturnElementNode;
import cn.yyx.research.program.ir.storage.node.IRSourceMethodStatementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;

public class IRGeneratorForFullTrace {
	
	IRGraphManager graph_manager = null;
	
	public IRGeneratorForFullTrace(IRGraphManager graph_manager) {
		this.graph_manager = graph_manager;
	}
	
	public void GenerateFullTraceOnInitialIRGraphs() {
		Collection<IRGraph> irgraphs = graph_manager.GetAllGraphs();
		Iterator<IRGraph> itr = irgraphs.iterator();
		while (itr.hasNext()) {
			IRGraph graph = itr.next();
			Set<IRSourceMethodParamElementNode> mparams = graph.GetSourceMethodParams();
			HandleSourceMethodParams(graph, mparams);
			Set<IRSourceMethodReturnElementNode> mreturns = graph.GetSourceMethodReturns();
			HandleSourceMethodReturns(graph, mreturns);
			Set<IRSourceMethodStatementNode> mstatements = graph.GetSourceMethodStatements();
			HandleSourceMethodStatements(graph, mstatements);
		}
	}
	
	private void HandleSourceMethodParams(IRGraph graph, Set<IRSourceMethodParamElementNode> mparams) {
		Iterator<IRSourceMethodParamElementNode> mpitr = mparams.iterator();
		while (mpitr.hasNext()) {
			IRSourceMethodParamElementNode irsmpen = mpitr.next();
			int para_index = irsmpen.GetParamIndex();
			IRSourceMethodStatementNode stmt_node = irsmpen.GetIRSourceMethodStatementNode();
			Collection<IMethod> methods = stmt_node.GetMethods();
			Iterator<IMethod> mitr = methods.iterator();
			while (mitr.hasNext()) {
				IMethod im = mitr.next();
				IRGraphForMethod im_graph = (IRGraphForMethod)graph_manager.GetGraphByIMember(im);
				if (im_graph != null) {
					List<IRJavaElementNode> params = im_graph.GetParameterElementNodes();
					if (params.size() > para_index) {
						IRJavaElementNode irjen = params.get(para_index);
						if (irjen != null) {
							graph.TransferConnectionFromNodeToNode(irsmpen, irjen);
						}
					}
				}
			}
			graph.RemoveConnectionsOfNode(irsmpen);
		}
	}
	
	private void HandleSourceMethodReturns(IRGraph graph, Set<IRSourceMethodReturnElementNode> mreturns) {
		Iterator<IRSourceMethodReturnElementNode> mritr = mreturns.iterator();
		while (mritr.hasNext()) {
			IRSourceMethodReturnElementNode irsmren = mritr.next();
			IRSourceMethodStatementNode stmt_node  = irsmren.GetIRSourceMethodStatementNode();
			Collection<IMethod> methods = stmt_node.GetMethods();
			Iterator<IMethod> mitr = methods.iterator();
			while (mitr.hasNext()) {
				IMethod im = mitr.next();
				IRGraphForMethod im_graph = (IRGraphForMethod)graph_manager.GetGraphByIMember(im);
				if (im_graph != null) {
					IRJavaElementNode mreturn = im_graph.GetReturnElementNode();
					if (mreturn != null) {
						graph.TransferConnectionFromNodeToNode(irsmren, mreturn);
					}
				}
			}
			graph.RemoveConnectionsOfNode(irsmren);
		}
	}
	

	private void HandleSourceMethodStatements(IRGraph graph, Set<IRSourceMethodStatementNode> mstatements) {
		// TODO Auto-generated method stub
		Iterator<IRSourceMethodStatementNode> msitr = mstatements.iterator();
		while (msitr.hasNext()) {
			IRSourceMethodStatementNode irsmsn = msitr.next();
			List<IRStatementNode> arg_stmts = irsmsn.GetArgumentStatements();
			Collection<IMethod> methods = irsmsn.GetMethods();
			Iterator<IMethod> mitr = methods.iterator();
			while (mitr.hasNext()) {
				IMethod im = mitr.next();
				// IRSourceMethodStatementNode connects to List<IRStatementNode>.
				
				// List<IRStatementNode> connect to root of im.
				
				// out nodes of im connect to IRSourceMethodStatementNode
				
			}
			
		}
	}
	
}
