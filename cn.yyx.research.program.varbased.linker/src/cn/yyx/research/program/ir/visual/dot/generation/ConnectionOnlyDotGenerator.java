package cn.yyx.research.program.ir.visual.dot.generation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
import cn.yyx.research.program.ir.storage.connection.Connect;
import cn.yyx.research.program.ir.storage.connection.ConnectHelper;
import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.connection.MethodJumpConnect;
import cn.yyx.research.program.ir.storage.connection.SuperConnect;
import cn.yyx.research.program.ir.storage.connection.VariableConnect;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;
import cn.yyx.research.program.ir.visual.dot.DotGenerator;
import cn.yyx.research.program.ir.visual.node.IVNode;
import cn.yyx.research.program.ir.visual.picture.generation.DotView;

public class ConnectionOnlyDotGenerator implements DotGenerator {

	String dot_generation_dir = null;
	String dot_pic_dir = null;
	private static final String dot_file = "IRGraphsForOneProject.dot";
	
	IRForOneProject one_project = null;

	int idx = 0;
	Map<IVNode, Integer> ivn_id = new HashMap<IVNode, Integer>();
	
	Set<IIRConnection> non_cluster_ivn_conns = new HashSet<IIRConnection>();
	Map<IVNode, DotCluster> ivn_cluster = new HashMap<IVNode, DotCluster>();
	Set<DotCluster> clusters = new HashSet<DotCluster>();

	public ConnectionOnlyDotGenerator(String dot_generation_dir, String dot_pic_dir, IRForOneProject one_project) {
		this.dot_generation_dir = dot_generation_dir;
		this.dot_pic_dir = dot_pic_dir;
		this.one_project = one_project;
		FileUtil.EnsureDirectoryExist(dot_generation_dir);
		FileUtil.EnsureDirectoryExist(dot_pic_dir);
	}

	@Override
	public void GenerateDotsAndPrintToPictures() {
		IRElementFactory ele_pool = one_project.GetIRElementPool();
		Collection<IRJavaElementNode> eles = ele_pool.GetAllIRJavaElementNodes();
		HandleOutConnectionsFromIIRNodes(eles);
		IRStatementFactory stmt_pool = one_project.GetIRStatementPool();
		Collection<IRStatementNode> stmts = stmt_pool.GetAllIRStatementNodes();
		HandleOutConnectionsFromIIRNodes(stmts);
		DrawClusters();
		DotView.HandleAllDotsInDirectory(dot_generation_dir, dot_pic_dir);
	}

	public void HandleOutConnectionsFromIIRNodes(Collection<? extends IIRNode> nodes) {
		Iterator<? extends IIRNode> nitr = nodes.iterator();
		while (nitr.hasNext()) {
			IIRNode iirn = nitr.next();
			Collection<IIRConnection> outs = iirn.GetAllOutConnections();
			Iterator<IIRConnection> oitr = outs.iterator();
			while (oitr.hasNext()) {
				IIRConnection iirc = oitr.next();
				IIRNode source = iirc.getSource();
				IIRNode target = iirc.getTarget();
				DotCluster source_cluster = GetCluster(source);
				DotCluster target_cluster = GetCluster(target);
				if (ConnectHelper.ConnectedNodesShouldBeInCluster(iirc)) {
					// handle other merges.
					if (!source_cluster.equals(target_cluster)) {
						clusters.remove(target_cluster);
						source_cluster.Merge(target_cluster);
						Set<IVNode> target_ivns = target_cluster.GetIvns();
						Iterator<IVNode> titr = target_ivns.iterator();
						while (titr.hasNext()) {
							IVNode tivn = titr.next();
							ivn_cluster.put(tivn, source_cluster);
						}
					}
					source_cluster.AddIVConnection(iirc);
				} else {
					non_cluster_ivn_conns.add(iirc);
				}
			}
		}
	}

	private void DrawConnections(Set<IIRConnection> conns, StringBuffer one_bw, String line_seperator) {
		Iterator<IIRConnection> conitr = conns.iterator();
		while (conitr.hasNext()) {
			IIRConnection conn = conitr.next();
			String color = "black";
			if (ConnectHelper.HasSpecificType(conn, VariableConnect.class)) {
				color = "pink";
			}
			if (ConnectHelper.HasSpecificType(conn, MethodJumpConnect.class)) {
				color = "green";
			}
			if (ConnectHelper.HasSpecificType(conn, SuperConnect.class)) {
				color = "blue";
			}
			IIRNode source = conn.getSource();
			IIRNode target = conn.getTarget();
			int source_id = GetNodeID(source);
			String source_node = "n" + source_id;
			int target_id = GetNodeID(target);
			String target_node = "n" + target_id;

			Set<Connect> details = conn.GetAllConnects();
			if (details == null || details.size() == 0) {
				one_bw.append(source_node + "->" + target_node + "[color=" + color + "];" + line_seperator);
			} else {
				Iterator<Connect> ditr = details.iterator();
				while (ditr.hasNext()) {
					Connect cd = ditr.next();
					one_bw.append(source_node + "->" + target_node + "[label=\"" + cd.toString() + "\", color=" + color
							+ "];" + line_seperator);
				}
			}
		}
	}

	public void DrawClusters() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dot_generation_dir + "/" + dot_file)));
			bw.write("digraph {");
			bw.newLine();
			bw.write("label=\"" + "whole_graph" + "\";");
			bw.newLine();

			String line_seperator = System.getProperty("line.separator");
			int cluster_idx = 0;
			Iterator<DotCluster> citr = clusters.iterator();
			while (citr.hasNext()) {
				cluster_idx++;
				DotCluster dc = citr.next();
				StringBuffer cluster_bw = new StringBuffer();
				cluster_bw.append("subgraph cluster" + cluster_idx + " {" + line_seperator);
				cluster_bw.append("node [style = filled,color = white];" + line_seperator);
				cluster_bw.append("style = filled;" + line_seperator);
				cluster_bw.append("color = lightgrey;" + line_seperator);
				cluster_bw.append("label = \"\";" + line_seperator);
				cluster_bw.append(line_seperator);

				DrawConnections(dc.GetIvnConns(), cluster_bw, line_seperator);

				cluster_bw.append("}" + line_seperator);
				bw.write(cluster_bw.toString());
			}

			StringBuffer share_bw = new StringBuffer();
			DrawConnections(non_cluster_ivn_conns, share_bw, line_seperator);
			bw.write(share_bw.toString());

			StringBuffer node_bf = new StringBuffer();
			Set<IVNode> iv_keys = ivn_id.keySet();
			Iterator<IVNode> iitr = iv_keys.iterator();
			while (iitr.hasNext()) {
				IVNode ivn = iitr.next();
				int ivn_id = GetNodeID(ivn);
				String ivn_node = "n" + ivn_id;
				node_bf.append(ivn_node + "[label=\"" + ivn.ToVisual() + "\"];" + line_seperator);
			}
			bw.write(node_bf.toString() + line_seperator);

			bw.newLine();
			bw.write("}");
			bw.newLine();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int GetNodeID(IVNode node) {
		Integer id = ivn_id.get(node);
		if (id == null) {
			id = idx++;
			ivn_id.put(node, id);
		}
		return id;
	}

	private DotCluster GetCluster(IIRNode ivn) {
		DotCluster cluster = ivn_cluster.get(ivn);
		if (cluster == null) {
			cluster = new DotCluster(ivn);
			ivn_cluster.put(ivn, cluster);
			clusters.add(cluster);
		}
		return cluster;
	}

}
