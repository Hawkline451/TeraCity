package coloring;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.registry.CoreRegistry;

import processor.PMDProcessor;




public class PMDColoring extends AbstractColoring{
	Map<String,String> colors = new HashMap<String,String>();
	PMDProcessor pmdP;
	String rootPath = CoreRegistry.get(CodeRepresentation.class).getPath();
	
	@Override
	public String getColor(String path) {
		String color = colors.get(path);
		return color == null ? "verde" : color;
	}
	
	@Override
	public void getDataColoring() throws IOException {
		if (pmdP == null ||  !(rootPath.equals(CoreRegistry.get(CodeRepresentation.class).getPath())))
			pmdP = new PMDProcessor(rootPath);
		colors = pmdP.getMap();
	}
}