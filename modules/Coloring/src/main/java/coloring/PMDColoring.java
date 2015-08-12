package coloring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PMDColoring extends AbstractColoring{
	Map<String,String> colors = new HashMap<String,String>();
	
	@Override
	public String getColor(String path) {
		return colors.get(path);
	}

	@Override
	public void getDataColoring() throws IOException {
		
	}
}
