package coloring;

import java.io.IOException;
import java.util.HashMap;

import metrics.Metric;
import utility.DataColour;

public class CheckStyleColoring extends AbstractColoring{
	HashMap<String, DataColour> data;

	@Override
	public void getDataColoring() throws IOException{
		String path = params[0];
		String metricString = params[1];
		int max = Integer.parseInt(params[2]);
		
		Metric metric = Metric.createMetric(metricString, max, null);
		metric.execute(path);
		try {
			data = metric.getData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

//
//	String def = System.getProperty("user.dir");
//	def = def.substring(0, def.lastIndexOf("\\"));
	@Override
	public String getColor(String path) {
		DataColour dc = data.get(path);
		if (dc == null) return "Coloring:verde";
		return dc.getColor();
	}
}