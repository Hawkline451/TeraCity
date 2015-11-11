package processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PMDProcessor {

	private String rootPath;
	private String rule;
	private final String outPutType = "text";
	private Map<String, Integer> counters;
	
	public PMDProcessor(String rootPath, String rule) {
		this.rootPath = rootPath;
		this.rule = rule;
		process();
	}
	
	public Map<String, Integer> getCounterMap() {
		return counters;
	}
	
	private String buildInputString() {
		
		String beforePath = File.pathSeparator;
		String separator = File.separator;

		StringBuilder sb = new StringBuilder();
		sb.append("java -cp ");
		sb.append(beforePath);
		
		StringBuilder pmdRoute = new StringBuilder();
		pmdRoute.append('.');
		pmdRoute.append(separator);
		pmdRoute.append("modules");
		pmdRoute.append(separator);
		pmdRoute.append("PMDColoring");
		pmdRoute.append(separator);
		pmdRoute.append("libs");
		pmdRoute.append(separator);
		pmdRoute.append("pmd");
		pmdRoute.append(separator);
		
		sb.append(pmdRoute);
		sb.append("lib");
		sb.append(separator);
		sb.append("* net.sourceforge.pmd.PMD -d ");
		sb.append(rootPath);
		sb.append(" -f ");
		sb.append(outPutType);
		sb.append(" -R ");
		sb.append(pmdRoute);
		sb.append("Metrics");
		sb.append(separator);
		sb.append(rule);
		sb.append(".xml");
		return sb.toString();
	}
	
	private void process(){
		counters = new HashMap<String, Integer>();
		String inputString = buildInputString();
		invokePMD(inputString);
	}

	private void invokePMD(String inputString) {
		Process process;
		try {
			process = Runtime.getRuntime().exec(inputString);
		
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			
			String line;
			while ((line = br.readLine()) != null) 
			{
				if (line.indexOf(':') != -1) {	
					try{
						// get filename only
						String pathClass = line.substring(0, line.lastIndexOf(".java")+5);
						System.out.print("pre: " + pathClass);
						File fi = new File(pathClass);
						pathClass = fi.getName();
						System.out.println(", post: " + pathClass);
						
						if (!counters.containsKey(pathClass)) {
							counters.put(pathClass, 0);
						}
						counters.put(pathClass, counters.get(pathClass)+1);
						
					} catch(IndexOutOfBoundsException e){}
				}
			}
		}catch (IOException e) {
			System.out.println("Error al llamar a comando pmd desde consola");
		}
	}
	
}