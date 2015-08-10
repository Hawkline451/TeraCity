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
            helpText = "Ejecuta el análisis de Cobertura sobre los archivos especificados\n"
                    + "<filesFolder>: Archivos que son testeados\n"
                    + "<testsFolder>: Archivos de test\n",
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
    private static final String BASE = "modules/Cobertura/cobertura-2.1.1";
    private static final String CLASSES_PATH = "/analysis/classes";
    private static final String TEST_CLASSES_PATH = "/analysis/testClasses";
    private static final String INSTRUMENTED_PATH = "/analysis/instrumented";
    private static final String REPORTS_PATH = "/analysis/reports";
    
    
    private Console console;
    private String filesFolder;
    private String testsFolder;
    private String pathSep = File.pathSeparator;
    public String extension;
    
    public ThreadCoberturaExecution(String files, String tests, Console console) {
        this.console = console;
        this.filesFolder = files;
        this.testsFolder = tests;
        chooseExtension();
    }
    
    private void chooseExtension(){
        String OS = System.getProperty("os.name");
        if (OS.startsWith("Windows")){
            extension = ".bat";
        }
        else{
            extension = ".sh";
        }
    }
    
    private String buildCompileTesteeCommand(String src){
        String res;
        res = "javac -g -d " + BASE + "/analysis/classes " + src + "/*.java";
        return res;
    }
    
    private String buildCompileTestsCommand(String testSrc){
        String res;
        res = "javac -g "
                + "-d " + BASE + "/analysis/testClasses " 
                + "-cp " + BASE + "/analysis/classes" + pathSep
                + BASE + "/lib/* "
                + testSrc+ "/*.java ";
        return res;
    }

    private String buildInstrumentCommand(){
        String res;
        res = BASE + "/cobertura-instrument" + extension + " "
                + "--datafile " + BASE + "/analysis/datafile.ser " 
                + "--destination "+ BASE + "/analysis/instrumented "
                + BASE + "/analysis/classes";
        return res;
    }

    private String buildRunTestCommand(){
        String testClasses = BASE + "/analysis/testClasses";
        File folder = new File(testClasses);
        File[] files = folder.listFiles();
        StringBuilder testList = new StringBuilder();
        for (int i = 0; i < files.length; i++){
            if (files[i].isFile() && !files[i].getName().equals(".gitignore")){
                testList.append(" ");
                testList.append(files[i].getName());
            }
        }
        String sList = testList.toString();
        sList = sList.replace(".class", "");
        String res;
        res = "java -cp " + BASE + "/cobertura-2.1.1.jar" + pathSep
                + BASE + "/analysis/instrumented" + pathSep
                + BASE + "/analysis/classes" + pathSep
                + BASE + "/analysis/testClasses" + pathSep
                + BASE + "/lib/*" + pathSep + " "
                + "-Dnet.sourceforge.cobertura.datafile=" + BASE + "/analysis/datafile.ser "
                + "org.junit.runner.JUnitCore " + sList;
        return res;
    }
            
    private String buildReportingCommand(){
        String res;
        res = BASE + "/cobertura-report" + extension + " --format xml "
                + "--datafile " + BASE + "/analysis/datafile.ser "
                + "--destination " + BASE + "/analysis/reports " + filesFolder;
        return res;
    }
    private void cleanFolderUp(String path){
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if(files[i].isFile() && !files[i].getName().equals(".gitignore")){
                files[i].delete();
            }
        }
    }
    private void cleanEverythingUp(){
        cleanFolderUp(BASE+CLASSES_PATH);
        cleanFolderUp(BASE+TEST_CLASSES_PATH);
        cleanFolderUp(BASE+INSTRUMENTED_PATH);
        cleanFolderUp(BASE+REPORTS_PATH);
        File datafile = new File(BASE + "/analysis/datafile.ser");
        datafile.delete();
        
    }
    @Override
    public void run(){      
        try {
            Process process;
            String commands = buildCompileTesteeCommand(filesFolder) + "&&"
                    + buildCompileTestsCommand(testsFolder) + "&&"
                    + buildInstrumentCommand();
            
            for (String input : commands.split("&&")){
                console.addMessage(input+"\n");
                process = Runtime.getRuntime().exec(input);
                process.waitFor();
            }
            
            commands = buildRunTestCommand() + "&&" + buildReportingCommand();
            for (String input : commands.split("&&")){
                console.addMessage(input+"\n");
                process = Runtime.getRuntime().exec(input);
                process.waitFor();
            }
            console.addMessage("Fin del Analisis:\n");
            console.addMessage(XMLParser.parse(BASE + "/analysis/reports/coverage.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            cleanEverythingUp();
        }
    }
}
