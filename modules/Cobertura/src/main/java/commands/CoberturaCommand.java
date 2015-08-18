package commands;

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
	public static Runner run;
	
    @Command(shortDescription = "Analisis usando Cobertura",
            helpText = "Ejecuta el analisis de Cobertura sobre los archivos especificados\n"
                    + "<filesFolder>: Archivos que son testeados\n"
                    + "<testsFolder>: Archivos de test\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String CoberturaAnalysis(
    		@CommandParam(value="type",required=true) String type,
            @CommandParam(value="firstArg",required=true) String firstArg,
            @CommandParam(value="secondArg",required=false) String secondArg) throws IOException{
        
    	analyze(type, firstArg, secondArg);
        return "Esperando por resultados del analisis ...";
    }
    
    public void analyze(String type, String s1, String s2){
    	classData = new HashMap<String, DataNode>();
    	run = new NullRunner();
    	getRunner(type, s1, s2);
    	Thread t = new Thread(new ThreadCoberturaExecution(run));
    	t.start();
    }
    
    private void getRunner(String type, String s1, String s2){
    	if (type.equals("-s")){ run = new CLSingleFolderRunner(s1); }
    	else if (type.equals("-t")){
    		if (s1 == null){ System.out.println("Missing second argument"); }
    		run = new CLTwoFoldersRunner(s1, s2);
    	}
    	else if (type.equals("-r")){ run = new ReportRunner(s1); }
    }
    
    public static String getColor(String classpath){
    	DataNode d = classData.get(classpath);
    	if (d == null){ return "Core:stone"; }
    	
    	double metric = d.getLineRate();
    	if (metric < 0){ return "Core:stone"; }
    	else if (metric == 0){ return "Coloring:morado"; }
    	else if (metric <= 0.2){ return "Coloring:rojo"; }
    	else if (metric <= 0.4){ return "Coloring:naranjo"; }
    	else if (metric <= 0.6){ return "Coloring:amarillo"; }
    	else if (metric <= 0.8){ return "Coloring:lime"; }
    	else if (metric < 1){ return "Coloring:verde"; }
    	else if (metric == 1){ return "Coloring:azul"; }
    	else { return "Core:stone"; }
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
    	System.out.println("Fin del Analisis:\n");
    	System.out.println(XMLParser.getResults(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml"));
    	CoberturaCommand.classData = XMLParser.getDataNodes(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml");
    	runner.cleanEverythingUp();
    }
}
