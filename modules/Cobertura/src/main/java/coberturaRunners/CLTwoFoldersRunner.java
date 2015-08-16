package coberturaRunners;

import org.terasology.logic.console.Console;
/**
 * Esta clase asume que los archivos de tests y los archivos testeados
 * se encuentran en carpetas separadas.
 * 
 * Se asume que los tests funcionan con JUnit 4
 *
 */
public class CLTwoFoldersRunner extends CommandLineRunner {
	private String testedFolder;
	private String testsFolder;
	public CLTwoFoldersRunner(Console con, String testedFolder, String testsFolder){
		super(con);
		this.testedFolder = testedFolder;
		this.testsFolder = testsFolder;
	}
	protected String buildCompileTestedCmd(){
		this.setFiles(testedFolder);
		String toCompile = this.allFilesPaths("java");
		String res;
		res = "javac -g -d "
			+ BASE + CLASSES_PATH
			+ " " + toCompile;
		return res;
	}
	protected String buildCompileTestsCmd(){
		this.setFiles(testsFolder);
		String toCompile = this.allFilesPaths("java");
		String res;
		res = "javac -g "
	          + "-d " + BASE + TEST_CLASSES_PATH +" " 
	          + "-cp " + BASE + CLASSES_PATH + pathSep
	          + BASE + "/lib/junit-4.11.jar "
	          + toCompile;
		return res;
	}
	@Override
	protected void compile() {
		String commands;
		commands = buildCompileTestedCmd();
		executeCommand(commands);
		console.addMessage("Done compiling tested files \n");
		commands = buildCompileTestsCmd();
		executeCommand(commands);
		console.addMessage("Done compiling tests\n");
	}
	@Override
	protected void runTests() {
		String testClasses = BASE + TEST_CLASSES_PATH;
		String testList = getAllTestNames(testClasses);
		String libAssets = getLibFiles();
		String res;
		res = "java -cp " + BASE + "/cobertura-2.1.1.jar" + pathSep
				+ BASE + INSTRUMENTED_PATH + pathSep
				+ BASE + CLASSES_PATH + pathSep
				+ BASE + TEST_CLASSES_PATH + pathSep
				+ libAssets
				+ " -Dnet.sourceforge.cobertura.datafile="
				+ BASE + "/analysis/datafile.ser "
				+ "org.junit.runner.JUnitCore " + testList;
		executeCommand(res);
		console.addMessage("Done running tests\n");
	}

	@Override
	protected void makeReport() {
    	String command = BASE + "/cobertura-report" + progExtension + " "
		+ "--format xml "
		+ "--datafile " + BASE + "/analysis/datafile.ser "
		+ "--destination " + BASE + REPORTS_PATH + " "
		+ testedFolder;
    	executeCommand(command);
    	console.addMessage("Done building report\n");
	}
}
