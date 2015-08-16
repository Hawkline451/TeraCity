package coloring;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import processor.PMDProcessor;




public class PMDColoring extends AbstractColoring{
	Map<String,String> colors = new HashMap<String,String>();
	Map<String, PMDProcessor> rulesApplied = new HashMap<String, PMDProcessor>();
	
	//String rootPath = CoreRegistry.get(CodeRepresentation.class).getPath();//Modify once we have the path
	String rootPath = "."+ File.separator+"modules"+ File.separator+"WorldCodecity"+ File.separator+"src"
				+ File.separator+"main"+ File.separator+"java"+ File.separator+"org"+ File.separator+"terasology"
			+ File.separator+"codecity"+ File.separator+"world";

	
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
		System.out.println("ROOT: "+rootPath);
		String rule = params[0];
		if (!rulesApplied.keySet().contains(rule)) //|| !rootPath.equals(CoreRegistry.get(CodeRepresentation.class)))
			rulesApplied.put(rule, new PMDProcessor(rootPath, rule));
		return rulesApplied.get(rule);
		
	}
}