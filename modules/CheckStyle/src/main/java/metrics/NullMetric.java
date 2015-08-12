package metrics;

import java.util.ArrayList;

public class NullMetric extends Metric {
	public NullMetric() {
	}
	
	@Override
	public void execute(String pathFile) {
		if (console != null) System.out.println("No existe la métrica pedida");
		else console.addMessage("No existe la métrica pedida");
	}
	
	@Override
	public void setMetricValue(Integer max, ArrayList<String> lines) {
	}
	
	@Override
	public ArrayList<String> getMetricText() {
		return null;
	}
}
