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
	
    @Command(shortDescription = "Analisis usando Cobertura",
            helpText = "Ejecuta coloreo de Cobertura, con el input de la forma especificada, "
            		+ "sobre los archivos especificados\n"
                    + "type: Formato de input a utilizar: \n"
                    + " - \"-t\": Archivos testeados y de tests en dos carpetas separadas\n"
                    + " - \"-s\": Todos los archivos en una sola carpeta (ayuda a no hacer procesos "
                    + "en archivos donde no sean necesarios).\n"
                    + " - \"-r\": Se entrega un path directamente a un reporte de Cobertura a analizar ("
                    + "al reporte en si mismo no solo a la carpeta que lo contiene).\n"
                    + "firstArg: Primer argumento. Si se elige el tipo 1, son los archivos a testear.\n"
                    + "secondArg: Segundo argumento. Si se elige el tipo 2, son los archivos de tests,"
                    + "no se usa para los otros dos tipos.",
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
    	Thread t = new Thread(new ThreadCoberturaExecution(getRunner(type, s1, s2)));
    	t.start();
    }
    
    private Runner getRunner(String type, String s1, String s2){
    	Runner ret = new NullRunner();
    	String message = "NullRunner =I";
    	if (type.equals("-s")){
    		message = "SingleFolder woo!";
    		ret = new CLSingleFolderRunner(s1);
    	}
    	else if (type.equals("-t")){
    		if (s1 == null){
    			message = "Missing second argument :C";
    		}
    		else{
    			message = "TwoFolder says hi~.";
    			ret = new CLTwoFoldersRunner(s1, s2);
    		}
    	}
    	else if (type.equals("-r")){
    		message = "ReportRunner is the bestest!";
    		ret = new ReportRunner(s1);
    	}
    	System.out.println(message);
    	return ret;
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
    	System.out.println(XMLParser.getResults(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml"));
    	CoberturaCommand.classData = XMLParser.getDataNodes(CLSingleFolderRunner.BASE + 
    			CLSingleFolderRunner.REPORTS_PATH + "/coverage.xml");
    	System.out.println("Fin del Analisis:\n");
    	runner.cleanEverythingUp();
    }
}
