package metrics;

import java.util.ArrayList;

import org.terasology.logic.console.Console;

public class NullMetric extends Metric {
	public NullMetric() {
	}
	
	@Override
	public void execute(String pathFile, Console console) {
		console.addMessage("No existe la métrica pedida");
	}
	
	@Override
	public void setMetricValue(Integer max, ArrayList<String> lines) {
	}
	
	@Override
	public ArrayList<String> getMetricText() {
		return null;
	}
}
