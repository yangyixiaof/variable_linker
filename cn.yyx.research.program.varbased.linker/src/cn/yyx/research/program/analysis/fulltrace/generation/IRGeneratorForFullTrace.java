package cn.yyx.research.program.analysis.fulltrace.generation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IMethod;

import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.MethodJumpConnect;
import cn.yyx.research.program.ir.storage.graph.IRGraph;
import cn.yyx.research.program.ir.storage.graph.IRGraphForMethod;
import cn.yyx.research.program.ir.storage.graph.IRGraphManager;
import cn.yyx.research.program.ir.storage.node.IIRNode;
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
				IRGraphForMethod im_graph = (IRGraphForMethod) graph_manager.GetGraphByIMember(im);
				if (im_graph != null) {
					List<IRJavaElementNode> params = im_graph.GetParameterElementNodes();
					if (params.size() > para_index) {
						IRJavaElementNode irjen = params.get(para_index);
						if (irjen != null) {
							IRGraph.TransferConnectionFromNodeToNode(irsmpen, irjen);
						}
					}
				}
			}
			IRGraph.RemoveConnectionsOfNode(irsmpen);
		}
	}

	private void HandleSourceMethodReturns(IRGraph graph, Set<IRSourceMethodReturnElementNode> mreturns) {
		Iterator<IRSourceMethodReturnElementNode> mritr = mreturns.iterator();
		while (mritr.hasNext()) {
			IRSourceMethodReturnElementNode irsmren = mritr.next();
			IRSourceMethodStatementNode stmt_node = irsmren.GetIRSourceMethodStatementNode();
			Collection<IMethod> methods = stmt_node.GetMethods();
			Iterator<IMethod> mitr = methods.iterator();
			while (mitr.hasNext()) {
				IMethod im = mitr.next();
				IRGraphForMethod im_graph = (IRGraphForMethod) graph_manager.GetGraphByIMember(im);
				if (im_graph != null) {
					IRJavaElementNode mreturn = im_graph.GetReturnElementNode();
					if (mreturn != null) {
						IRGraph.TransferConnectionFromNodeToNode(irsmren, mreturn);
					}
				}
			}
			IRGraph.RemoveConnectionsOfNode(irsmren);
		}
	}

	private void HandleSourceMethodStatements(IRGraph graph, Set<IRSourceMethodStatementNode> mstatements) {
		Iterator<IRSourceMethodStatementNode> msitr = mstatements.iterator();
		while (msitr.hasNext()) {
			IRSourceMethodStatementNode irsmsn = msitr.next();
			List<IRStatementNode> arg_stmts = irsmsn.GetArgumentStatements();

			IRStatementNode start = null;
			IRStatementNode end = null;
			IRStatementNode previous_irsn = null;
			Iterator<IRStatementNode> aitr = arg_stmts.iterator();
			while (aitr.hasNext()) {
				IRStatementNode irsn = aitr.next();
				if (start == null) {
					start = irsn;
				}
				if (!aitr.hasNext()) {
					end = irsn;
				}
				if (previous_irsn != null) {
					IRGraph.RegistConnection(previous_irsn, irsn, new Connect());
				}
				previous_irsn = irsn;
			}

			Collection<IMethod> methods = irsmsn.GetMethods();
			Iterator<IMethod> mitr = methods.iterator();
			while (mitr.hasNext()) {
				IMethod im = mitr.next();
				IRGraph im_graph = graph_manager.GetGraphByIMember(im);
				if (im_graph != null && im_graph.getRoot() != null) {
					// irsmsn connects to arg_stmts.
					if (start != null) {
						IRGraph.RegistConnection(irsmsn, start, new MethodJumpConnect());
					}
					// List<IRStatementNode> connect to root of im.
					if (end != null) {
						IRGraph.RegistConnection(end, im_graph.getRoot(), new Connect());
					}
					// control-out-nodes of im connect to irsmsn.
					Set<IIRNode> out_nodes = im_graph.GetControlOutNodes();
					if (out_nodes.size() > 0) {
						Iterator<IIRNode> oitr = out_nodes.iterator();
						while (oitr.hasNext()) {
							IIRNode iirn = oitr.next();
							IRGraph.RegistConnection(iirn, irsmsn, new MethodJumpConnect());
						}
					}
				}
			}
		}
	}

}
