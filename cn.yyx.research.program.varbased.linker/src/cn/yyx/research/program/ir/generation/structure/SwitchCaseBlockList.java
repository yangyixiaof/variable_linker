package cn.yyx.research.program.ir.generation.structure;

import java.util.LinkedList;
import java.util.List;

public class SwitchCaseBlockList {
	
	private List<SwitchCaseBlock> switch_blocks = new LinkedList<SwitchCaseBlock>();
	
//	private List<ASTNode> branch_first_stats = new LinkedList<ASTNode>();
//	private List<ASTNode> branch_last_stats = new LinkedList<ASTNode>();
//	private LinkedList<LinkedList<ASTNode>> branch_first_to_last = new LinkedList<LinkedList<ASTNode>>();
	
	public SwitchCaseBlockList() {
	}
	
	public void AddSwitchBlock(SwitchCaseBlock block) {
		switch_blocks.add(block);
	}
	
	public List<SwitchCaseBlock> GetSwitchBlocks() {
		return switch_blocks;
	}
	
}
