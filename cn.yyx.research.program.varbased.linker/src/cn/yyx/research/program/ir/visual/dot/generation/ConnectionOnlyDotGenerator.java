package cn.yyx.research.program.ir.visual.dot.generation;

import java.util.Collection;
import java.util.Iterator;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
import cn.yyx.research.program.ir.storage.connection.IIRConnection;
import cn.yyx.research.program.ir.storage.node.IIRNode;
import cn.yyx.research.program.ir.storage.node.IRJavaElementNode;
import cn.yyx.research.program.ir.storage.node.IRStatementNode;
import cn.yyx.research.program.ir.storage.node.factory.IRElementFactory;
import cn.yyx.research.program.ir.storage.node.factory.IRStatementFactory;
import cn.yyx.research.program.ir.visual.dot.DotGenerator;

public class ConnectionOnlyDotGenerator implements DotGenerator {
	
	String dot_generation_dir = null;
	String dot_pic_dir = null;
	IRForOneProject one_project = null;
	
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
	}
	
	public void HandleOutConnectionsFromIIRNodes(Collection<? extends IIRNode> nodes) {
		Iterator<? extends IIRNode> nitr = nodes.iterator();
		while (nitr.hasNext()) {
			IIRNode iirn = nitr.next();
			Collection<IIRConnection> outs = iirn.GetAllOutConnections();
			Iterator<IIRConnection> oitr = outs.iterator();
			while (oitr.hasNext()) {
				IIRConnection iirc = oitr.next();
				
			}
		}
	}
	
}
