package commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;

@RegisterSystem
public class CoberturaCommand {
	private String WINbase = ".\\modules\\Cobertura\\Cobertura-2.1.1\\";
	@In
    private Console console;
	@Command(shortDescription = "Analysis Cobertura.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String AnalysisCobertura(
    		@CommandParam(value = "filesFolder",required = true) String filesFolder,
    		@CommandParam(value="testsFolder",required=true) String testsFolder) throws IOException
    {
    	//String inputString = buildInputString(filesFolder, testsFolder);
    	
    	//Thread t = new Thread(new ThreadCoberturaExecution(inputString, console));
		//t.start();
		
		return "Esperando por resultados del analisis...";
    }
	
	private String buildCompileTesteeCommand(String src){
		String res;
		res = "javac -g -d " + WINbase + "/analysis/classes " + src + "/*.java";
		return res;
	}
	
	private String buildCompileTestsCommand(String testSrc){
		String res;
		res = "javac -g -d "
				+ WINbase + "/analysis/testClasses " 
				+ "-classpath " + WINbase + "/analysis/classes/*;"
				+ WINbase + "./lib/* "
				+ testSrc+ "/*.java";
		return res;
	}

	private String buildInstrumentCommand(){
		String res;
		res = "cobertura-instrument.bat "
				+ "--datafile " + WINbase + "/analysis/datafile.ser" 
				+ "--destination "+ WINbase + "/analysis/instrumented "
				+ WINbase + "/analysis/classes";
		return res;
	}

	private String buildRunTestCommand(){
		String testClasses = WINbase + "/analysis/testClasses";
		File folder = new File(testClasses);
		File[] files = folder.listFiles();
		String testList = "";
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){
				testList += " "+files[i].getName();
			}
		}
		String res;
		res = "java -cp" + WINbase + "\\cobertura-2.1.1.jar;"
				+ WINbase + "/analysis/instrumented;"
				+ WINbase + "/analysis/classes;"
				+ WINbase + "lib/*; "
				+ "org.junit.runner.JUnitCore " + testList;
		return res;
	}
			

	private String buildReportingCommand(String src){
		String res;
		res = "cobertura-report.bat --format html "
				+ "--datafile" + WINbase + "/analysis/datafile.ser"
				+ "--destination" + WINbase + "/analysis/reports" + src;
		return res;
//		String OS = System.getProperty("os.name");
//		String beforePath = null;
//		String separator = null;
//		if (OS.startsWith("Linux"))
//		{
//			beforePath = ":";
//			separator = "/";
//		}
//		else if (OS.startsWith("Windows"))
//		{
//			beforePath = "";
//			separator = "\\";
//		}
		
//		StringBuilder sb = new StringBuilder();
//		sb.append("java -cp ");
//		sb.append(beforePath);
//		sb.append('.');
//		sb.append(separator);
//		sb.append("modules");
//		sb.append(separator);
//		sb.append("PMDColoring");
//		sb.append(separator);
//		sb.append("libs");
//		sb.append(separator);
//		sb.append("pmd");
//		sb.append(separator);
//		sb.append("lib");
//		sb.append(separator);
//		sb.append("* net.sourceforge.pmd.PMD -d ");
//		sb.append(sourcePath);
//		sb.append(" -f ");
//		sb.append(outPutType);
//		sb.append(" -R rulesets/java/");
//		sb.append(rules);
//		sb.append(".xml");
//		
//		return sb.toString();
	}
}

class ThreadCoberturaExecution implements Runnable
{
	String inputString;
	Console console;
	public ThreadCoberturaExecution(String inputString, Console console) {
		this.inputString = inputString;
		this.console = console;
	}

	@Override
	public void run() 
	{		
		try {
			Process process;
			process = Runtime.getRuntime().exec(inputString);
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
				
			String line;
			while ((line = br.readLine()) != null) {
				 console.addMessage(line);	
			}
			console.addMessage("Fin del Analisis");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
