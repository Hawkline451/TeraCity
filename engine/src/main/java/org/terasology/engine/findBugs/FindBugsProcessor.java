package org.terasology.engine.findBugs;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class FindBugsProcessor {

	private String rootPath;
	private Map<String, Integer> counters;

	public FindBugsProcessor(String rootPath) {
		this.rootPath = rootPath;
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
		
		StringBuilder fbRoute = new StringBuilder();
		fbRoute.append('.');
		fbRoute.append(separator);
		fbRoute.append("modules");
		fbRoute.append(separator);
		fbRoute.append("FindBugsColoring");
		fbRoute.append(separator);
		fbRoute.append("libs");
		fbRoute.append(separator);
		fbRoute.append("findBugs");

		sb.append(" -jar");
		sb.append(" findbugs-plugin.jar ");
		sb.append(" -onlyAnalyze ");
		sb.append(this.rootPath);

		return sb.toString();
	}

	private void process(){
		counters = new HashMap<String, Integer>();
		String inputString = buildInputString();
		invokefindBugs(inputString);
	}
	
	public void invokefindBugs(String inputString) {
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
						File fi = new File(pathClass);
						pathClass = fi.getName();
						
						if (!counters.containsKey(pathClass)) {
							counters.put(pathClass, 0);
						}
						counters.put(pathClass, counters.get(pathClass)+1);
						
					} catch(IndexOutOfBoundsException e){}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
