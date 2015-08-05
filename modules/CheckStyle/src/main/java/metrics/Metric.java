package metrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public abstract class Metric {
	String path = "modules/CheckStyle/libs/CheckStyle/Metrics/";
	String jar = "modules/CheckStyle/libs/CheckStyle/checkstyle-6.6-all.jar";
	int max;
	
	public Metric() {
	}
	
	public static Metric createMetric(String StringMetric, int max) throws IOException {
		Metric metric;
		if (StringMetric.equals("-b")) metric = new BooleanMetric(max);
		else if (StringMetric.equals("-c")) metric = new CyclomaticMetric(max);
		else if (StringMetric.equals("-d")) metric = new DataAbstractionCouplingMetric(max);
		else if (StringMetric.equals("-f")) metric = new FanOutMetric(max);
		else if (StringMetric.equals("-n")) metric = new NPathMetric(max);
		else metric = new NullMetric();
		
		ArrayList<String> linesArray = metric.getMetricText();
		metric.setMetricValue(max, linesArray);
		return metric;
	}
	
	private String createJarCommand(String pathFile) {
		String commandJar = "-jar " + jar  + " ";
		String commandMetric = " -c " + path + " ";
		String commandOut = " -f xml -o modules/CheckStyle/Project/out.xml ";
		
		return "java " + commandJar + commandMetric + commandOut + pathFile;
	}
	
	public void setMetricValue(Integer max, ArrayList<String> lines) {
		FileWriter bw = null; 
		PrintWriter pw = null;
		try {
			bw = new FileWriter(path); 
			pw = new PrintWriter(bw);
			for (String actualLine : lines) {
				String regExp = " *<property name=\"max\" value=\".*\"/>";
				if (actualLine.matches(regExp)){
					actualLine = "      <property name=\"max\" value=\"" + max + "\"/>";
				}
				pw.write(actualLine + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			pw.close();
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ArrayList<String> getMetricText() throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> lines = new ArrayList<String>();
		try {
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			String line = "";
			while ((line = br.readLine()) != null) {
				lines.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			br.close();
			fr.close();
		}
		return lines;
	}

	public boolean execute(String pathFile) throws IOException {
		String commandJar = createJarCommand(pathFile);
		System.out.println(commandJar);
		Runtime.getRuntime().exec(commandJar);
		return true;
	}
}
