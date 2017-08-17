package cn.yyx.research.program.ir.visual.dot.generation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.util.IMemberDescriptionHelper;
import cn.yyx.research.program.ir.visual.DotView;
import cn.yyx.research.program.ir.visual.dot.DotGenerator;
import cn.yyx.research.program.ir.visual.node.IVNode;

public class IRGraphGenerateDot implements DotGenerator {
	
	String dot_generation_dir = null;
	String dot_pic_dir = null;
	IRGraphManager graph_manager = null;
	
	public IRGraphGenerateDot(String dot_generation_dir, String dot_pic_dir, IRGraphManager graph_manager) {
		this.dot_generation_dir = dot_generation_dir;
		this.dot_pic_dir = dot_pic_dir;
		this.graph_manager = graph_manager;
		FileUtil.EnsureDirectoryExist(dot_generation_dir);
		FileUtil.EnsureDirectoryExist(dot_pic_dir);
	}
	
	@Override
	public void GenerateDotsAndPrintToPictures() {
		int idx = 0;
		List<IRCode> ircodes = IRGeneratorForOneProject.GetInstance().GetAllIRCodes();
		Iterator<IRCode> iitr = ircodes.iterator();
		while (iitr.hasNext()) {
			IRCode irc = iitr.next();
//			if (!irc.IsHasElement()) {
//				continue;
//			}
			idx++;
			IRTreeForOneControlElement control_ir = irc.GetControlLogicHolderElementIR();
			HashSet<IVNode> pc = new HashSet<IVNode>();
			pc.add(control_ir.GetRoot());
			Set<IJavaElement> eles = irc.GetAllElements();
			Iterator<IJavaElement> eitr = eles.iterator();
			while (eitr.hasNext()) {
				IJavaElement ije = eitr.next();
				pc.add(irc.GetFirstIRTreeNode(ije));
			}
			VariableOperationDotGenerator cdg = new VariableOperationDotGenerator(pc, IRGeneratorForOneProject.GetInstance(), dot_generation_dir + "/" + "IRCode" + idx + ".dot", IMemberDescriptionHelper.GetDescription(irc.getIm()));
			cdg.GenerateDot();
		}
		DotView.HandleAllDotsInDirectory(dot_generation_dir, dot_pic_dir);
	}
	
}
