package coloring;

import java.util.Hashtable;

import gitMetrics.GitMetric;

public class GitColoring extends AbstractColoring{
	Hashtable<String, Integer> data;
	GitMetric metric;

	@Override
	public String getColor(String path) {
		int classData = data.get(path);
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
		String url = params[1];
		String metricString = params[0];
		String projectName = params[2];
		String output = "modules/GitHub/tempRepo/";
		
		metric = new GitMetric(metricString, url, projectName, output, null);
		metric.execute();
		metric.setData();
		data = metric.getData();
	}
}