package coloring;

import java.io.IOException;
import java.util.ArrayList;

import coloringCommands.PlaceBlockCommand;
import metrics.Metric;
import utility.DataColour;

public class CheckStyleColoring extends AbstractColoring{
	ArrayList<DataColour> data;
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
	public void executeColoring(){
		PlaceBlockCommand pbc = new PlaceBlockCommand();
		for (DataColour dc : data) {
			System.out.println("clase: |" + dc.getPath() + "|\n");
			System.out.println(pbc.ColorBuild(dc.getPath(), dc.getDataWarnings().get(0).getColor()));
		}
	}

	@Override
	public String getColor() {
		// TODO Auto-generated method stub
		return null;
	}
}