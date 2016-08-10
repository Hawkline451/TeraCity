package coberturaRunners;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * El subgrupo de Runners representado por esta clase corren
 * Cobertura en la consola de comandos del sistema.
 * 
 * Se proveen varias metodos utiles para correr Cobertura de esta forma.
 * 
 * TODO extender utilidad cobertura
 * Este metodo al parecer no funciona del todo. Solamente va servir para proyectos pequeños (ya que compila todos los archivos con 
 * un comando y los comandos tienen limite de caracteres) ademas como copia todos los compilados a una carpeta local (ver el path de BASE)
 * no soporta proyectos que referencien archivos de forma local (ejemplo pedir un archivo en la carpeta maps relativo al directorio del proyecto)
 * lo cual limita mucho la utilidad y el funcionamiento de la cobertura. Es necesario poder correr los test relativos a la carpeta del proyecto no 
 * copiarlos otra vez
 */
public abstract class CommandLineRunner extends Runner{
	/**
	 * Utility variable.
	 */
    protected ArrayList<File> files;
    protected String progExtension;
    
    
    public CommandLineRunner(){
    	this.chooseExtension();
    }
    
	@Override
	protected void instrument() {
		String command = BASE + "/cobertura-instrument" + progExtension + " "
    			+ "--datafile "+ BASE + "/analysis/datafile.ser "
    			+ "--destination " + BASE + INSTRUMENTED_PATH + " "
    			+ BASE + CLASSES_PATH;
    	executeCommand(command);
    	System.out.println("Done Instrumenting");
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
				fileList.append(" ");
			}
			
		}
		return fileList.toString();
	}
	protected static String getLibFiles(){
    	File[] libFiles = new File(BASE+"/lib").listFiles();
    	StringBuilder libAssetsBuilder = new StringBuilder();
    	for (File file : libFiles) {
			libAssetsBuilder.append(file.getAbsolutePath());
			libAssetsBuilder.append(File.pathSeparator);
		}
    	return libAssetsBuilder.toString();
	}
    protected String getAllTestNames(String path){
    	String testSub = "Test";
        String classesPath = path;
        File folder = new File(classesPath);
//        File[] files = folder.listFiles();
        StringBuilder testList = new StringBuilder();
        for (int i = 0; i < files.size(); i++){
        	String fileName = files.get(i).getName();
            if ((files.get(i).isFile()) && (!fileName.equals(".gitignore"))
            		&& (fileName.toLowerCase().contains(testSub.toLowerCase()))){
                
                String pathT = files.get(i).getPath().replace("/", ".").replace("\\", ".");
                String path2 = path.replace("/", ".").replace("\\", ".");
                //Quitar / y \ por puntos para simular path estilo package
                //eliminar partes comunes en ambos path lo que queda son los packages
                String res = pathT.replace(path2, "").substring(1); //Quitar primer caracter punto

                testList.append(" ");
                testList.append(res);
                
            }
        }
        String sList = testList.toString();
        sList = sList.replace(".class", "");
        return sList;
    }
    
    
    protected void executeCommand(String command){
    	System.out.println(command);
    	try{
	    	Process p = Runtime.getRuntime().exec(command) ;  
	    	ReadStream s1 = new ReadStream("stdin", p.getInputStream ());
	    	ReadStream s2 = new ReadStream("stderr", p.getErrorStream ());
	    	s1.start ();
	    	s2.start ();
	    	p.waitFor();        
	    	} catch (Exception e) {  
	    	e.printStackTrace();  
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
    protected void chooseExtension(){
        String OS = System.getProperty("os.name");
        if (OS.startsWith("Windows")){
            progExtension = ".bat";
        }
        else{
            progExtension = ".sh";
        }
    }
    /*
     * Quizas se deben mover los metodos que manejan Files a su propia clase?
     * Tambien, quizas encontrar una manera de abstraerse de los metodos parecidos
     * como "getLibFiles" y "getAllFilePaths"?
     */
}
