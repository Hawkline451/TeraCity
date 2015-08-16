package commands;

import java.io.IOException;
import java.util.HashMap;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;

import coberturaRunners.*;
@RegisterSystem
public class CoberturaCommand extends BaseComponentSystem{
    
	public static HashMap<String, DataNode> classData;
    @In
    private Console console;
    @Command(shortDescription = "Analisis usando Cobertura",
            helpText = "Ejecuta el analisis de Cobertura sobre los archivos especificados\n"
                    + "<filesFolder>: Archivos que son testeados\n"
                    + "<testsFolder>: Archivos de test\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String CoberturaAnalysis(
            @CommandParam(value = "filesFolder",required = true) String filesFolder,
            @CommandParam(value="testsFolder",required=true) String testsFolder) throws IOException{
        
    	analyze(filesFolder, testsFolder);
        return "Esperando por resultados del analisis ...";
    }
    
    public void analyze(String filesFolder, String testsFolder){
    	classData = new HashMap<String, DataNode>();
        Thread t = new Thread(new ThreadCoberturaExecution(filesFolder, testsFolder, console));
        t.start();
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
    private Console console;
    /* TODO: Move some of these to Runners?
     *       Or maybe turn them into a List, since different runners
     *       might need different number of arguments? 
     */
    private String filesFolder;
    private String testsFolder;
    
    public ThreadCoberturaExecution(String files, String tests, Console console) {
        this.console = console;
        this.filesFolder = files;
        this.testsFolder = tests;
    }
    @Override
    public void run(){
    	CLSingleFolderRunner foldy = new CLSingleFolderRunner(console, filesFolder);
    	foldy.runCobertura();
    	console.addMessage("Fin del Analisis:\n");
    	console.addMessage(XMLParser.getResults(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml"));
    	CoberturaCommand.classData = XMLParser.getDataNodes(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml");
    	foldy.cleanEverythingUp();
    }
}
