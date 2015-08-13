package processor;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class PMDProcessor {

	private String rootPath;
	private String rule;
	private final String outPutType = "text";

	public PMDProcessor(String rootPath, String rule) {
		this.rootPath = rootPath;
		this.rule = rule;
		System.out.println(buildInputString());
		//process();
	}

	public Map<String, String> getMap() {
		return new HashMap<String, String>();
	}
	
	private String buildInputString() {
		
		String OS = System.getProperty("os.name");
		String beforePath = null;
		String separator = null;
		if (OS.startsWith("Linux"))
		{
			beforePath = ":";
			separator = "/";
		}
		else if (OS.startsWith("Windows"))
		{
			beforePath = "";
			separator = "\\";
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("java -cp ");
		sb.append(beforePath);
		sb.append('.');
		sb.append(separator);
		sb.append("modules");
		sb.append(separator);
		sb.append("PMDColoring");
		sb.append(separator);
		sb.append("libs");
		sb.append(separator);
		sb.append("pmd");
		sb.append(separator);
		sb.append("lib");
		sb.append(separator);
		sb.append("* net.sourceforge.pmd.PMD -d ");
		sb.append(rootPath);
		sb.append(" -f ");
		sb.append(outPutType);
		sb.append(" -R rulesets/java/");
		sb.append(rule);
		sb.append(".xml");
		return sb.toString();
	}
/*//FIRST COUNT OCURRENCES, THEN ASSIGN A COLOR
	private void process() {
		String inputString = buildInputString();
		try {
			Process process;
			process = Runtime.getRuntime().exec(inputString);
			InputStream is = process.getInputStream();
			 InputStreamReader isr = new InputStreamReader(is);
			 BufferedReader br = new BufferedReader(isr);
				
			 String line;
			 int messageLines = 0;
			 while ((line = br.readLine()) != null) 
			 {
				 //console.addMessage(line);
				 ++messageLines;
			 }
			 console.addMessage("Lineas del mensage: "+ messageLines);
			 
			 int totalLines = new LineCounter(LineCounter.JAVA_REGEX).countLines(sourcePath);
			 
			 console.addMessage("Lineas totales: "+ totalLines);
			 if(rule.equals("comments"))
			 {
				 String color = new CommentsMetric(messageLines, totalLines).getColor();
				 currentColor = color;
				 console.addMessage(color);
			 }
			 else if(rules.equals("codesize"))
			 {
				 String color = new CodeSizesMetric(messageLines, totalLines).getColor();
				 currentColor = color;
				 console.addMessage(color);
			 }
			 console.addMessage("Fin del Analisis");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
*/
}
