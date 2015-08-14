package commands;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;

import coberturaRunners.CLSingleFolderRunner;
import coberturaRunners.CommandLineRunner;
import coberturaRunners.NullRunner;

@RegisterSystem
public class CoberturaCommand extends BaseComponentSystem{
    
	public static HashMap<String, DataNode> classData;
    @In
    private Console console;
    @Command(shortDescription = "Analisis usando Cobertura",
            helpText = "Ejecuta el análisis de Cobertura sobre los archivos especificados\n"
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
    private static final String BASE = "modules/Cobertura/cobertura-2.1.1";
    private static final String CLASSES_PATH = "/analysis/classes";
    private static final String TEST_CLASSES_PATH = "/analysis/testClasses";
    private static final String INSTRUMENTED_PATH = "/analysis/instrumented";
    private static final String REPORTS_PATH = "/analysis/reports";
    
    // TODO: Move some of these to Runners.
    private Console console;
    private String filesFolder;
    private String testsFolder;
    public String extension;
    
    public ThreadCoberturaExecution(String files, String tests, Console console) {
        this.console = console;
        this.filesFolder = files;
        this.testsFolder = tests;
        chooseExtension();
    }
    // TODO: Move to CommandLineRunner
    private void chooseExtension(){
        String OS = System.getProperty("os.name");
        if (OS.startsWith("Windows")){
            extension = ".bat";
        }
        else{
            extension = ".sh";
        }
    }
    
//    private String buildCompileTesteeCommand(String src){
//        String res;
//        res = "javac -g -d " + BASE + CLASSES_PATH + " " + src + "/*.java";
//        return res;
//    }
//    
//    
//    private String buildCompileTestsCommand(String testSrc){
//        String res;
//        res = "javac -g "
//                + "-d " + BASE + TEST_CLASSES_PATH +" " 
//                + "-cp " + BASE + CLASSES_PATH + pathSep
//                + BASE + "/lib/* "
//                + testSrc+ "/*.java ";
//        return res;
//    }
//
//    private String buildInstrumentCommand(){
//        String res;
//        res = BASE + "/cobertura-instrument" + extension + " "
//                + "--datafile " + BASE + "/analysis/datafile.ser " 
//                + "--destination "+ BASE + INSTRUMENTED_PATH + " "
//                + BASE + CLASSES_PATH;
//        return res;
//    }
//
//    private String buildRunTestCommand(){
//        String testClasses = BASE + TEST_CLASSES_PATH;
//        File folder = new File(testClasses);
//        File[] files = folder.listFiles();
//        StringBuilder testList = new StringBuilder();
//        for (int i = 0; i < files.length; i++){
//            if (files[i].isFile() && !files[i].getName().equals(".gitignore")){
//                testList.append(" ");
//                testList.append(files[i].getName());
//            }
//        }
//        String sList = testList.toString();
//        sList = sList.replace(".class", "");
//        String res;
//        res = "java -cp " + BASE + "/cobertura-2.1.1.jar" + pathSep
//                + BASE + INSTRUMENTED_PATH + pathSep
//                + BASE + CLASSES_PATH + pathSep
//                + BASE + TEST_CLASSES_PATH + pathSep
//                + BASE + "/lib/*" + pathSep + " "
//                + "-Dnet.sourceforge.cobertura.datafile=" + BASE + "/analysis/datafile.ser "
//                + "org.junit.runner.JUnitCore " + sList;
//        return res;
//    }
//            
//    private String buildReportingCommand(){
//        String res;
//        res = BASE + "/cobertura-report" + extension + " --format xml "
//                + "--datafile " + BASE + "/analysis/datafile.ser "
//                + "--destination " + BASE + REPORTS_PATH + " " + filesFolder;
//        return res;
//    }
    @Override
    public void run(){
    	CLSingleFolderRunner foldy = new CLSingleFolderRunner(console, filesFolder);
    	foldy.runCobertura();
    	console.addMessage("Fin del Analisis:\n");
    	console.addMessage(XMLParser.getResults(BASE + "/analysis/reports/coverage.xml"));
    	CoberturaCommand.classData = XMLParser.getDataNodes(BASE + "/analysis/reports/coverage.xml");
    	foldy.cleanEverythingUp();
    	
//        try {
//            Process process;
//            String commands = buildCompileTesteeCommand(filesFolder) + "&&"
//                    + buildCompileTestsCommand(testsFolder) + "&&"
//                    + buildInstrumentCommand();
//            
//            for (String input : commands.split("&&")){
//                console.addMessage(input+"\n");
//                process = Runtime.getRuntime().exec(input);
//                process.waitFor();
//            }
//            
//            commands = buildRunTestCommand() + "&&" + buildReportingCommand();
//            for (String input : commands.split("&&")){
//                console.addMessage(input+"\n");
//                process = Runtime.getRuntime().exec(input);
//                process.waitFor();
//            }
//            console.addMessage("Fin del Analisis:\n");
//            console.addMessage(XMLParser.getResults(BASE + "/analysis/reports/coverage.xml"));
//            CoberturaCommand.classData = XMLParser.getDataNodes(BASE + "/analysis/reports/coverage.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("\nError de conexion\n");
//        } finally{
//            cleanEverythingUp();
//        }
    }
}
