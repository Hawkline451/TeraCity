package coloring;

import java.io.IOException;

public interface IColoring{
	
	public void executeColoring();
	
	public void getDataColoring() throws IOException ;
	
	public String getColor();
	
	public void execute(String[] params);
	
}
