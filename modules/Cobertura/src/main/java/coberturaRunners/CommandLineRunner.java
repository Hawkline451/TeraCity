package coberturaRunners;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.terasology.logic.console.Console;

public abstract class CommandLineRunner extends AbstractRunner{
	/**
	 * Utility variable.
	 */
    protected ArrayList<File> files;
    protected Console console;
    
    public CommandLineRunner(Console console){
    	this.console = console;
    }
    public void setFiles(String folder){
    	files = new ArrayList<File>();
    	findFiles(new File(folder));
    }
	protected void findFiles(File currentFile){
		if (currentFile.isFile()){
			files.add(currentFile);
		}
		else {
			for (File f : currentFile.listFiles()){
				findFiles(f);
			}
		}
	}
	protected boolean hasExtension(File f, String extension){
		String name = f.getName();
		int index = name.lastIndexOf(".");
		if (index > 0) { return name.substring(index).equals("."+extension); }
		return false;
	}
	
	public String allFilesPaths(String wantedExtension){
		StringBuilder fileList = new StringBuilder("");
		for (File current : files){
			if(hasExtension(current, wantedExtension)){
				fileList.append(current.getAbsolutePath());
			}
			fileList.append(" ");
		}
		return fileList.toString();
	}
    protected String getAllTestNames(String path){
        String classesPath = path;
        File folder = new File(classesPath);
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
        return sList;
    }
    protected void executeCommand(String command){
    	console.addMessage(command+"\n");
        try
        {            
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ( (line = br.readLine()) != null)
                System.out.println(line);
            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
        } catch (Throwable t){
            t.printStackTrace();
        }
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
    public void cleanEverythingUp(){
        cleanFolderUp(BASE+CLASSES_PATH);
        cleanFolderUp(BASE+TEST_CLASSES_PATH);
        cleanFolderUp(BASE+INSTRUMENTED_PATH);
        cleanFolderUp(BASE+REPORTS_PATH);
        File datafile = new File(BASE + "/analysis/datafile.ser");
        datafile.delete();
    }
}
