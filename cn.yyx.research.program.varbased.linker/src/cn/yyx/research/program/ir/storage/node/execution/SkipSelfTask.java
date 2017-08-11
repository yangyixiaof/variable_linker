package cn.yyx.research.program.ir.storage.node.execution;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.analysis.fulltrace.storage.FullTrace;
import cn.yyx.research.program.analysis.fulltrace.storage.connection.DynamicConnection;
import cn.yyx.research.program.analysis.fulltrace.storage.node.DynamicNode;
import cn.yyx.research.program.ir.exception.ConflictConnectionDetailException;
import cn.yyx.research.program.ir.meta.IRMeta;
import cn.yyx.research.program.ir.meta.storage.IIRNodeTask;
import cn.yyx.research.program.ir.storage.connection.ConnectionInfo;
import cn.yyx.research.program.ir.storage.connection.EdgeBaseType;
import cn.yyx.research.program.ir.storage.node.lowlevel.IRForOneInstruction;

public class SkipSelfTask extends IIRNodeTask {

	public SkipSelfTask(IRForOneInstruction iirnode) {
		super(iirnode);
	}

	@Override
	public void HandleOutConnection(DynamicNode source, DynamicNode target, ConnectionInfo connect_info,
			FullTrace ft) {
		// Solved. need to handle IRForOneRawMethodBarrier.
		
		
		// debugging.
		if (source.toString().trim().endsWith(IRMeta.VirtualBranch + "#1")) {
			Math.abs(0);
		}
		if (source.toString().trim().startsWith("y^Op:@LeftAssign")) {
			Math.abs(0);
		}
		if (source.toString().trim().startsWith("y^Op:+")) {
			Math.abs(0);
		}
		if (target.toString().trim().startsWith("y^Op:*")) {
			Math.abs(0);
		}
		
		
		int final_type = TaskExecutionHelper.ComputeFinalType(source, target, connect_info);
		
		Set<DynamicConnection> in_conns = ft.GetInConnections(source);
		if (in_conns.isEmpty()) {
			try {
				ft.AddConnection(new DynamicConnection(source, target, (ConnectionInfo)connect_info.clone()));
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			// System.err.println("====== Real skip task is running!");
			boolean skip = true;
			IJavaElement oim = null;
			Iterator<DynamicConnection> icitr = in_conns.iterator();
			while (icitr.hasNext()) {
				DynamicConnection dc = icitr.next();
				DynamicNode sour = dc.GetSource();
				IJavaElement im = sour.getInstr().getIm();
				if (oim == null) {
					oim = im;
				} else {
					if (!oim.equals(im)) {
						skip = false;
						break;
					}
				}
			}
			if (skip) {
				Iterator<DynamicConnection> iitr = in_conns.iterator();
				while (iitr.hasNext()) {
					DynamicConnection dc = iitr.next();
					ft.RemoveConnection(dc);
					DynamicNode nsource = dc.GetSource();
					DynamicNode ntarget = target;
					int addition = ntarget.getInstr().getIm().equals(nsource.getInstr().getIm()) ? EdgeBaseType.Self.Value() : 0;
					ConnectionInfo source_info = dc.getInfo();
					ConnectionInfo new_info = null;
					try {
						new_info = source_info.VerticalMerge(connect_info);
					} catch (ConflictConnectionDetailException e) {
						e.printStackTrace();
						System.exit(1);
					}
					DynamicConnection new_dc = new DynamicConnection(nsource, ntarget, new ConnectionInfo(final_type | addition, new_info.GetDetails()));
					ft.AddConnection(new_dc);
				}
				ft.HandleRootsAfterRemovingAllConnections(in_conns);
			} else {
				try {
					ft.AddConnection(new DynamicConnection(source, target, (ConnectionInfo)connect_info.clone()));
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
//			DynamicConnection conn = ft.GetSpecifiedConnection(source, target);
//			if (conn == null) {
//				System.err.println("Strange! specified connection is null!" + ";Source:" + source + ";Target:" + target);
//			}
//			ft.RemoveConnection(conn);
		}
		Math.abs(0);
	}
	
}
