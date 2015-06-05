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
	
	@Command(shortDescription = "Cobertura Analysis.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String CoberturaAnalysis(
    		@CommandParam(value = "filesFolder",required = true) String filesFolder,
    		@CommandParam(value="testsFolder",required=true) String testsFolder) throws IOException{
		    	
    	Thread t = new Thread(new ThreadCoberturaExecution(filesFolder, testsFolder, console));
		t.start();
		
		return "Esperando por resultados del analisis...";
    }
	
}

class ThreadCoberturaExecution implements Runnable {
	
	Console console;
	String filesFolder;
	String testsFolder;
	private String WINbase = "./modules/Cobertura/Cobertura-2.1.1";
	
	public ThreadCoberturaExecution(String files, String tests, Console console) {
		this.console = console;
		this.filesFolder = files;
		this.testsFolder = tests;
	}
	
	private String buildCompileTesteeCommand(String src){
		String res;
		res = "javac -g -d " + WINbase + "/analysis/classes " + src + "/*.java";
		return res;
	}
	
	private String buildCompileTestsCommand(String testSrc){
		String res;
		res = "javac -g "
				+ "-d " + WINbase + "/analysis/testClasses " 
				+ "-cp " + WINbase + "/analysis/classes;"
				+ WINbase + "/lib/* "
				+ testSrc+ "/*.java ";
		return res;
	}

	private String buildInstrumentCommand(){
		String res;
		res = "\"" + WINbase + "/cobertura-instrument.bat\" "
				+ "--datafile " + WINbase + "/analysis/datafile.ser " 
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
		
		testList = testList.replace(".class", "");
		String res;
		res = "java -cp " + WINbase + "/cobertura-2.1.1.jar;"
				+ WINbase + "/analysis/instrumented;"
				+ WINbase + "/analysis/classes;"
				+ WINbase + "/analysis/testClasses;"
				+ WINbase + "/lib/*; "
				+ "-Dnet.sourceforge.cobertura.datafile=" + WINbase + "/analysis/datafile.ser "
				+ "org.junit.runner.JUnitCore " + testList;
		return res;
	}
			
	private String buildReportingCommand(){
		String res;
		res = "\"" + WINbase + "/cobertura-report.bat\" --format html "
				+ "--datafile " + WINbase + "/analysis/datafile.ser "
				+ "--destination " + WINbase + "/analysis/reports " + filesFolder;
		return res;
	}
	
	@Override
	public void run(){		
		try {
			Process process;
			String line;
			String commands = buildCompileTesteeCommand(filesFolder) + "&&"
		    		+ buildCompileTestsCommand(testsFolder) + "&&"
		    		+ buildInstrumentCommand();
			
			
			for (String input : commands.split("&&")){
				console.addMessage(input+"\n");
				process = Runtime.getRuntime().exec(input);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process.waitFor();
				
//				while ((line = br.readLine()) != null) {
//					 console.addMessage(line);	
//				}
			}
			
			commands = buildRunTestCommand() + "&&" + buildReportingCommand();
			for (String input : commands.split("&&")){
				console.addMessage(input+"\n");
				process = Runtime.getRuntime().exec(input);
				BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
				process.waitFor();
			}
			console.addMessage("Fin del Analisis");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
