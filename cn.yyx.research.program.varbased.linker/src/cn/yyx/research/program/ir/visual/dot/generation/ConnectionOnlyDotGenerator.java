package cn.yyx.research.program.ir.visual.dot.generation;

import cn.yyx.research.program.fileutil.FileUtil;
import cn.yyx.research.program.ir.generation.structure.IRForOneProject;
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
		
	}
	
}
