package coberturaRunners;


import org.terasology.logic.console.Console;
/**
 * Esta clase asume que todos los archivos (tanto de test como los testeados)
 * estan en una misma carpeta.
 * 
 * Se asume que los tests usan JUnit 4.
 *
 */
public class CLSingleFolderRunner extends CommandLineRunner{
	private String srcFolder;
	public CLSingleFolderRunner(Console console, String folder) {
		super(console);
		srcFolder = folder;
	}
	@Override
	protected void compile() {
		this.setFiles(srcFolder);
		String toCompile = this.allFilesPaths("java");
    	String command;
    	command = "javac -g -d "+ BASE + CLASSES_PATH + " "
    			+ "-cp "+ BASE + "/lib/junit-4.11.jar "
    			+ toCompile;
    	executeCommand(command);
    	console.addMessage("Done compiling\n");
	}
	@Override
	protected void runTests() {
		this.setFiles(BASE+CLASSES_PATH);
    	String testsList = getAllTestNames(BASE+CLASSES_PATH);
    	String libAssets = getLibFiles();
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
    	String command = BASE + "/cobertura-report" + progExtension + " "
		+ "--format xml "
		+ "--datafile " + BASE + "/analysis/datafile.ser "
		+ "--destination " + BASE + REPORTS_PATH + " "
		+ srcFolder;
    	executeCommand(command);
    	console.addMessage("Done building report\n");
	}
}