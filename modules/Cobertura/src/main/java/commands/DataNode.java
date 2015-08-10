package commands;

public class DataNode {
	private String className;
	private double lineRate;
	private double branchRate;
	private double complexity;
	
	public DataNode(String name, double line, double branch, double comp){
		className = name;
		lineRate = line;
		branchRate = branch;
		complexity = comp;
	}
	
	public String getName(){ return className; }
	public double getLineRate(){ return lineRate; }
	public double getBranchRate(){ return branchRate; }
	public double getComplexity(){ return complexity; }
}
