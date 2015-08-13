package coloring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.registry.CoreRegistry;

import processor.PMDProcessor;




public class PMDColoring extends AbstractColoring{
	Map<String,String> colors = new HashMap<String,String>();
	Map<String, PMDProcessor> rulesApplied = new HashMap<String, PMDProcessor>();
	
	String rootPath = CoreRegistry.get(CodeRepresentation.class).getPath();//Modify once we have the path
	
	
	
	@Override
	public String getColor(String path) {
		String color = colors.get(path);
		return color == null ? "Green" : color;
	}
	
	@Override
	public void getDataColoring() throws IOException {
		PMDProcessor pmdP = getProcessor();
		colors = pmdP.getMap();
	}

	private PMDProcessor getProcessor() {
		String rule = params[0];
		if (!rulesApplied.keySet().contains(rule))
			rulesApplied.put(rule, new PMDProcessor(rootPath, rule));
		return rulesApplied.get(rule);
		
	}
}