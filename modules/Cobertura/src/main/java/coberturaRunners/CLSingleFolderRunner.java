package coberturaRunners;

import java.io.File;

import org.terasology.logic.console.Console;

public class CLSingleFolderRunner extends CommandLineRunner{
	private String srcFolder;
	public CLSingleFolderRunner(Console console, String folder) {
		super(console);
		srcFolder = folder;
		this.setFiles(folder);
	}
//    public void execAllInOne(String src){
//    	String commands;
//    	commands = "javac -g -d "+ BASE + CLASSES_PATH + " "
//    			+ "-cp "+ BASE + "/lib/junit-4.11.jar "
//    			+ src + "/*.java";
//    	// En vez de wildcard, obtener todos los archivos .java a partir de src.
//    	executeCommand(commands);
//    	commands = BASE + "/cobertura-instrument.bat "
//    			+ "--datafile "+ BASE + "/analysis/datafile.ser "
//    			+ "--destination " + BASE + INSTRUMENTED_PATH + " "
//    			+ BASE + CLASSES_PATH;
//    	executeCommand(commands);
//    	console.addMessage("Done Instrumenting\n");
//    	String testsList = getAllTestNames(BASE + CLASSES_PATH);
//    	commands = "java -cp "+ BASE + "/cobertura-2.1.1.jar" + pathSep
//    			+ BASE + INSTRUMENTED_PATH + pathSep
//    			+ BASE + CLASSES_PATH + pathSep
//    			+ BASE + "/lib/*" + pathSep // ATTN FUTURE US: WILDCARD USE.
//    			+ " -Dnet.sourceforge.cobertura.datafile="
//    			+ BASE + "/analysis/datafile.ser "
//    			+ "org.junit.runner.JUnitCore " + testsList;
//    	executeCommand(commands);
//    	console.addMessage("Done with JUnit\n");
//    	commands = BASE + "/cobertura-report.bat "
//    			+ "--format xml "
//    			+ "--datafile " + BASE + "/analysis/datafile.ser "
//    			+ "--destination " + BASE + REPORTS_PATH + " "
//    			+ src;
//    	executeCommand(commands);
//    	console.addMessage("Done with reporting\n");
//    }
	@Override
	protected void compile() {
		String toCompile = this.allFilesPaths("java");
    	String command;
    	command = "javac -g -d "+ BASE + CLASSES_PATH + " "
    			+ "-cp "+ BASE + "/lib/junit-4.11.jar "
    			+ toCompile;
    	executeCommand(command);
    	console.addMessage("Done compiling\n");
	}
	@Override
	protected void instrument() {
		String command = BASE + "/cobertura-instrument.bat "
    			+ "--datafile "+ BASE + "/analysis/datafile.ser "
    			+ "--destination " + BASE + INSTRUMENTED_PATH + " "
    			+ BASE + CLASSES_PATH;
    	executeCommand(command);
    	console.addMessage("Done Instrumenting\n");
	}
	@Override
	protected void runTests() {
		this.setFiles(BASE+CLASSES_PATH);
    	String testsList = getAllTestNames(BASE+CLASSES_PATH);
    	File[] libFiles = new File(BASE+"/lib").listFiles();
    	StringBuilder libAssetsBuilder = new StringBuilder();
    	for (File file : libFiles) {
			libAssetsBuilder.append(file.getAbsolutePath());
			libAssetsBuilder.append(pathSep);
		}
    	String libAssets = libAssetsBuilder.toString();
    	String commands = "java -cp "+ BASE + "/cobertura-2.1.1.jar" + pathSep
    			+ BASE + INSTRUMENTED_PATH + pathSep
    			+ BASE + CLASSES_PATH + pathSep
    			+ libAssets
    			+ " -Dnet.sourceforge.cobertura.datafile="
    			+ BASE + "/analysis/datafile.ser "
    			+ "org.junit.runner.JUnitCore " + testsList;
    	executeCommand(commands);
    	console.addMessage("Done running tests\n");
	}
	@Override
	protected void makeReport() {
    	String command = BASE + "/cobertura-report.bat "
		+ "--format xml "
		+ "--datafile " + BASE + "/analysis/datafile.ser "
		+ "--destination " + BASE + REPORTS_PATH + " "
		+ srcFolder;
    	executeCommand(command);
    	console.addMessage("Done with reporting\n");
	}
}
