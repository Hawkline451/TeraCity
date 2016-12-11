package commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;

import coberturaRunners.*;
@RegisterSystem
public class CoberturaCommand extends BaseComponentSystem{
    
	public static HashMap<String, DataNode> classData;
	private Thread thread;
	
    @Command(shortDescription = "Analyse using Cobertura",
            helpText = "Run color of Cobertura, with input in the specified format, "
            		+ "on specified files\n"
                    + "type: input's format to use: \n"
                    + " - \"-t\": test and tested files in two separate folders\n"
                    + " - \"-s\": All the files in only one folder (Help to do not process "
                    + "in files where it is not necessary).\n"
                    + " - \"-r\": A path is delivered directly to a Cobertura report to be analyzed ("
                    + "to the report itself not only to the folder containing it).\n"
                    + "firstArg: first argument. If 1 is choosen, it concerns the files to be tested.\n"
                    + "secondArg: Second argument . If 2 is choosen, it concerns the test files,"
                    + "not used for the other two types.",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String CoberturaAnalysis(
    		@CommandParam(value="type",required=true) String type,
            @CommandParam(value="firstArg",required=true) String firstArg,
            @CommandParam(value="secondArg",required=false) String secondArg) throws IOException{
        
    	analyze(type, firstArg, secondArg);
        return "Waiting the results of the analysis...";
    }
    public void waitForAnalysis(){
    	try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    public void analyze(String type, String s1, String s2){
    	classData = new HashMap<String, DataNode>();
    	thread = new Thread(new ThreadCoberturaExecution(getRunner(type, s1, s2)));
    	thread.start();
    }
    
    private Runner getRunner(String type, String s1, String s2){
    	Runner ret = new NullRunner();
    	if (type.equals("-s")){
    		ret = new CLSingleFolderRunner(s1);
    	}
    	else if (type.equals("-t")){
    		if (s1 == null){System.out.println("Missing second argument!");}
    		else{ret = new CLTwoFoldersRunner(s1, s2);}
    	}
    	else if (type.equals("-r")){ret = new ReportRunner(s1);}
    	return ret;
    }
    
    public static double getMetricValue(String classpath){
    	
    	File fi = new File(classpath);
    	DataNode data = classData.get(fi.getName());
    	
    	if (data == null) {
    		return -1;
    	}
    	return data.getLineRate();
    }
    
}

class ThreadCoberturaExecution implements Runnable {

	private Runner runner;
    
    public ThreadCoberturaExecution(Runner r) {
    	this.runner = r;
    }
    
    @Override
    public void run(){
    	runner.runCobertura();
    	System.out.println(XMLParser.getResults(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml"));
    	CoberturaCommand.classData = XMLParser.getDataNodes(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml");
    	System.out.println("Fin del Analisis:\n");
    	runner.cleanEverythingUp();
    }
}
