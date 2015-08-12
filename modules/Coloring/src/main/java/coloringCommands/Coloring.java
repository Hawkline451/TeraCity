package coloringCommands;

import java.io.IOException;

public interface Coloring {
	
	public void executeColoring(String[] parameters) throws IOException;
	
	public String getColor(String name);
	
}
