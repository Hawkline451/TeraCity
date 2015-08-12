package metrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import org.terasology.logic.console.Console;
import org.terasology.registry.In;

import utility.CheckStyleParser;
import utility.DataColour;


public abstract class Metric{
	String path = "modules/CheckStyle/libs/CheckStyle/Metrics/";
	String jar = "modules/CheckStyle/libs/CheckStyle/checkstyle-6.6-all.jar";
	int max;
	Thread thread;
	MetricExecution me;
	
	@In
	Console console;
	
	public Metric() {
	}
	
	public static Metric createMetric(String StringMetric, int max, Console console) throws IOException {
		Metric metric;
		if (StringMetric.equals("-b")) metric = new BooleanMetric();
		else if (StringMetric.equals("-c")) metric = new CyclomaticMetric();
		else if (StringMetric.equals("-d")) metric = new DataAbstractionCouplingMetric();
		else if (StringMetric.equals("-f")) metric = new FanOutMetric();
		else if (StringMetric.equals("-n")) metric = new NPathMetric();
		else metric = new NullMetric();
		
		metric.setConsole(console);
		metric.setMax(max);
		ArrayList<String> linesArray = metric.getMetricText();
		metric.setMetricValue(max, linesArray);
		return metric;
	}
	
	private void setConsole(Console console) {
		this.console = console;
	}

	private void setMax(int max) {
		this.max = max;
	}

	private String createJarCommand(String pathFile) {
		String commandJar = "-jar " + jar  + " ";
		String commandMetric = " -c " + path + " ";
		String commandOut = " -f xml -o modules/CheckStyle/Project/out.xml ";
		
		return "java " + commandJar + commandMetric + commandOut + pathFile;
	}
	
	public void setMetricValue(Integer max, ArrayList<String> lines) throws IOException {
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
			bw.close();
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
	
	public void execute(String pathFile) {
		String commandJar = createJarCommand(pathFile);
		me = new MetricExecution(commandJar, pathFile, console);
		thread = new Thread(me);
		if (console != null) console.addMessage("Comenzó el análisis....");
		else System.out.println("Comenzó el análisis....");
		thread.start();
	}
	
	public ArrayList<DataColour> getData() throws InterruptedException {
		if (console != null) console.addMessage("Esperando termino del análisis...");
		else System.out.println("Esperando termino del análisis...");
		thread.join();
		return me.data;
	}
}


class MetricExecution implements Runnable{
	String path;
	String commandJar;
	ArrayList<DataColour> data;
	
	@In
	Console console;
	
	public MetricExecution(String commandJar, String path, Console console) {
		this.commandJar = commandJar;
		this.path = path;
		this.console = console;
	}
	
	public void analize() throws IOException {
		Process process = Runtime.getRuntime().exec(commandJar);
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		while (br.readLine()!=null);
	}
	
	public void parse() {
		String path = "modules/CheckStyle/Project/out.xml";
		CheckStyleParser cp = new CheckStyleParser();
		cp.parse(path);
		data = cp.getData();
	}

	@Override
	public void run() {
		try {
			analize();
			parse();
			if (console != null) console.addMessage("Terminó el análisis");
			else System.out.println("Terminó el análisis");
		} catch (IOException e) {
			if (console != null) console.addMessage("Falló el analisis");
			else System.err.println("Falló el analisis");
			e.printStackTrace();
		}
		
	}
}