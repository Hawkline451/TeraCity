package coloring;

import java.io.File;
import java.util.Hashtable;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.registry.CoreRegistry;




import gitMetrics.GitMetric;

public class GitColoring extends AbstractColoring{
	Hashtable<String, Integer> data;
	GitMetric metric;

	@Override
	public String getColor(String path) {
		System.out.println(path);
		Integer classData = data.get(path);
		if (classData==null) return "yellow";
		if (metric.toString() == "bug") {
			if (classData == 1) return "red";
			return "green";
		}
		else if (metric.toString() == "version") {
			if (classData > 50) return "red";
			else if (classData > 20) return "yellow";
			else return "green";
		}
		return "normal";
	}

	@Override
	public void getDataColoring(){
		String url = CoreRegistry.get(CodeRepresentation.class).getGithubDir();
		String metricString = params[0];
		String projectName = params[2];
		String output = "modules/GitHub/tempRepo/";
		
		metric = new GitMetric(metricString, url, projectName, output, null);
		metric.execute();
		metric.setData();
		data = metric.getData();
	}
}