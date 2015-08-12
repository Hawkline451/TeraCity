
public class CodeSizesMetric implements Metric {

	private int requiredComments;
	private int linesOfCode;
	
	public CodeSizesMetric(int requiredComments, int linesOfCode) 
	{
		this.requiredComments = requiredComments;
		this.linesOfCode = linesOfCode;
	}
	
	@Override
	public String getColor() 
	{
		if(requiredComments > 10000)
			return "Red";
		else if(requiredComments > 1000)
			return "Blue";
		return "Green";
	}


}
