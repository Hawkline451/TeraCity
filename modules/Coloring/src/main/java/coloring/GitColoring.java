package coloring;

import java.io.IOException;
import java.util.Hashtable;

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
	}

	@Override
	public void getDataColoring() throws IOException {
		String url = params[1];
		String metricString = params[0];
		String projectName = param[2];
		String output = "modules/GitHub/temp";
		
		metric = GitMetric.createMetric(metricString, url, output, projectName);
		metric.execute();
		try {
			data = metric.getData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	}

}