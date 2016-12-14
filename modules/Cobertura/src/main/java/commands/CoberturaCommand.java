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
