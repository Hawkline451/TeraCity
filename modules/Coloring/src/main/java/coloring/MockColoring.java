package coloring;

import java.util.Hashtable;
import java.util.Random;

public class MockColoring extends AbstractColoring{
	Hashtable<String, Integer> data;
	String metric;
	
	@Override
	public String getColor(String path) {
		
		int value;
		
		// metric logic
		if (metric == "random") {
			Random r = new Random();
			value = r.nextInt(100);
		} else if (metric == "good") {
			value = 100;
		} else {
			value = 50;
		}
		
		// coloring
		if (value < 30) {
			return "red";
		} else if (value < 60) {
			return "orange";
		} else {
			return "green";
		}
		
	}

	@Override
	public void getDataColoring(){
		
		metric = params[0];	
		data = new Hashtable<String, Integer>();
	}
}