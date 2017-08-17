package cn.yyx.research.program.ir.visual.dot.generation;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.storage.IRGraphManager;
import cn.yyx.research.program.ir.visual.dot.DotGenerator;

public class ConnectionOnlyDotGenerator implements DotGenerator {
	
	String dot_generation_dir = null;
	String dot_pic_dir = null;
	IRGraphManager graph_manager = null;
	
	public ConnectionOnlyDotGenerator(String dot_generation_dir, String dot_pic_dir, IRGraphManager graph_manager) {
		this.dot_generation_dir = dot_generation_dir;
		this.dot_pic_dir = dot_pic_dir;
		this.graph_manager = graph_manager;
		FileUtil.EnsureDirectoryExist(dot_generation_dir);
		FileUtil.EnsureDirectoryExist(dot_pic_dir);
	}
	
	@Override
	public void GenerateDotsAndPrintToPictures() {
		
	}
	
}
