package coloring;

import java.io.IOException;
import java.util.HashMap;

import metrics.Metric;
import utility.DataColour;

public class CheckStyleColoring extends AbstractColoring{
	HashMap<String, DataColour> data;
	Metric metric;

	@Override
	public void getDataColoring() throws IOException{
		String path = params[0];
		String metricString = params[1];
		int max = Integer.parseInt(params[2]);
		
		metric = Metric.createMetric(metricString, max, null);
		metric.execute(path);
		try {
			data = metric.getData();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getColor(String path) {
		return data.get(path).getDataWarnings().get(0).getColor();
	}
}