package commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;

@RegisterSystem
public class CoberturaCommand extends BaseComponentSystem{
	
	@In
    private Console console;
	@Command(shortDescription = "Analisis usando Cobertura",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String CoberturaAnalysis(
    		@CommandParam(value = "filesFolder",required = true) String filesFolder,
    		@CommandParam(value="testsFolder",required=true) String testsFolder) throws IOException{
		    	
    	Thread t = new Thread(new ThreadCoberturaExecution(filesFolder, testsFolder, console));
		t.start();
		
		return "Esperando por resultados del analisis ...";
    }
	
}

class ThreadCoberturaExecution implements Runnable {
	
	private Console console;
	private String filesFolder;
	private String testsFolder;
	private String pathSep = File.pathSeparator;
	public String extension;
	private String base = "./modules/Cobertura/Cobertura-2.1.1";
	
	public ThreadCoberturaExecution(String files, String tests, Console console) {
		this.console = console;
		this.filesFolder = files;
		this.testsFolder = tests;
		
		String OS = System.getProperty("os.name");
		if (OS.startsWith("Linux")){
			extension = ".sh";
		}
		else if (OS.startsWith("Windows")){
			extension = ".bat";
		}
	}
	
	private String buildCompileTesteeCommand(String src){
		String res;
		res = "javac -g -d " + base + "/analysis/classes " + src + "/*.java";
		return res;
	}
	
	private String buildCompileTestsCommand(String testSrc){
		String res;
		res = "javac -g "
				+ "-d " + base + "/analysis/testClasses " 
				+ "-cp " + base + "/analysis/classes" + pathSep
				+ base + "/lib/* "
				+ testSrc+ "/*.java ";
		return res;
	}

	private String buildInstrumentCommand(){
		String res;
		res = "\"" + base + "/cobertura-instrument" + extension + "\" "
				+ "--datafile " + base + "/analysis/datafile.ser " 
				+ "--destination "+ base + "/analysis/instrumented "
				+ base + "/analysis/classes";
		return res;
	}

	private String buildRunTestCommand(){
		String testClasses = base + "/analysis/testClasses";
		File folder = new File(testClasses);
		File[] files = folder.listFiles();
		String testList = "";
		for (int i = 0; i < files.length; i++){
			if (files[i].isFile()){
				testList += " "+files[i].getName();
			}
		}
		
		testList = testList.replace(".class", "");
		String res;
		res = "java -cp " + base + "/cobertura-2.1.1.jar" + pathSep
				+ base + "/analysis/instrumented" + pathSep
				+ base + "/analysis/classes" + pathSep
				+ base + "/analysis/testClasses" + pathSep
				+ base + "/lib/*" + pathSep + " "
				+ "-Dnet.sourceforge.cobertura.datafile=" + base + "/analysis/datafile.ser "
				+ "org.junit.runner.JUnitCore " + testList;
		return res;
	}
			
	private String buildReportingCommand(){
		String res;
		res = "\"" + base + "/cobertura-report" + extension + "\" --format xml "
				+ "--datafile " + base + "/analysis/datafile.ser "
				+ "--destination " + base + "/analysis/reports " + filesFolder;
		return res;
	}
	
	@Override
	public void run(){		
		try {
			Process process;
//			String line;
			String commands = buildCompileTesteeCommand(filesFolder) + "&&"
		    		+ buildCompileTestsCommand(testsFolder) + "&&"
		    		+ buildInstrumentCommand();
			
			
			for (String input : commands.split("&&")){
				console.addMessage(input+"\n");
				process = Runtime.getRuntime().exec(input);
//				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process.waitFor();
				
//				while ((line = br.readLine()) != null) {
//					 console.addMessage(line);	
//				}
			}
			
			commands = buildRunTestCommand() + "&&" + buildReportingCommand();
			for (String input : commands.split("&&")){
				console.addMessage(input+"\n");
				process = Runtime.getRuntime().exec(input);
				// BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process.waitFor();
			}
			console.addMessage("Fin del Analisis:\n");
			console.addMessage(XMLParser.parse(base + "/analysis/reports/coverage.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
