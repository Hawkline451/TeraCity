package coloringCommands;

import java.io.IOException;
import java.util.ArrayList;

import utility.CheckStyleParser;
import utility.DataColour;
import metrics.*;

public abstract class CheckStyleColoring implements Coloring{
	ArrayList<DataColour> result;
	
	@Override
	public void executeColoring(String[] parameters) throws IOException {
		String metricString = parameters[0];
		int max = Integer.parseInt(parameters[1]);
		String path = parameters[2];
		Metric metric = Metric.createMetric(metricString, max);
		metric.execute(path, null);
		CheckStyleParser cp = new CheckStyleParser();
		cp.parse(path);
		result = cp.getData();
	}

	@Override
	public abstract String getColor(String name);
}