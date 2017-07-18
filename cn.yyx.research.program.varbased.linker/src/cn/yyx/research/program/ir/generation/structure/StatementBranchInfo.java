package cn.yyx.research.program.ir.generation.structure;

import java.util.LinkedList;
import java.util.List;

import cn.yyx.research.program.ir.storage.node.IIRNode;

public class StatementBranchInfo {
	
	private IIRNode branch_root = null;
	private List<IIRNode> branches = new LinkedList<IIRNode>();
	
	public StatementBranchInfo(IIRNode branch_root) {
		this.branch_root = branch_root;
	}

	public IIRNode GetBranchRoot() {
		return branch_root;
	}
	
	public void AddBranch(IIRNode branch) {
		branches.add(branch);
	}
	
	public List<IIRNode> GetBranches() {
		return branches;
	}
	
}
