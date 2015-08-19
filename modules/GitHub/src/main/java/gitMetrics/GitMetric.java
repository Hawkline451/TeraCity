package gitMetrics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;








import org.terasology.logic.console.Console;
import org.terasology.registry.In;

public class GitMetric {

	String outPath = "modules/GitHub/Metrics/";
	String jarPath = "modules/GitHub/libs/GitMetrics.jar";
	
	Thread thread;
	Hashtable<String, Integer> data;
	String tipo_metrica;
	String Url;
	String projectName;
	String tempFolderPath;
	GitExecution gitExecution;
	
	@In
	Console console;
	
	public GitMetric (String tipo, String Url, String projectName,String tempFolderPath , Console console){
		this.tipo_metrica=tipo;
		this.Url=Url;
		this.projectName=projectName;
		this.console=console;
		this.tempFolderPath = tempFolderPath;
		
	}

	private String createJarCommand(){
		String command="java -jar "+ jarPath
				+" -n "+ projectName
				+" -r "+ Url
				+" -o "+ outPath
				+" -t "+ tempFolderPath;
		return command;
	}
	public void execute(){
		String commandJar = createJarCommand();
		gitExecution = new GitExecution(commandJar, console);
		thread = new Thread(gitExecution);
		if (console != null) console.addMessage("Comenzó el análisis....");
		else System.out.println("Comenzó el análisis....");
		thread.start();
	}
	
	public void setData(){
		if(console!=null) console.addMessage("Esperando termino del análisis...");
		else System.out.println("Esperando termino del análisis...");
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		data=new Hashtable<String,Integer>();
		if(tipo_metrica.equals("bugs")){ readTsvFile(outPath.concat(projectName.concat("_Bugs.tsv")));}
		else if(tipo_metrica.equals("versions")){readTsvFile(outPath.concat(projectName.concat("_Versions.tsv")));}
		else{System.err.println("it has'nt seted data of GitMetric's object, due to param named 'tipo_metrica' is wrong...");}
	}
	private void readTsvFile(String tsvPath){
		// This method read a Tsv File and Set data hashtable...
		String linea;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(tsvPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((linea=br.readLine())!=null){
				String [] args = linea.split("\t");
				data.put(args[0], Integer.parseInt(args[1]));
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Hashtable<String,Integer> getData(){
		return data;
	}
	
	class GitExecution implements Runnable{
		String commandJar;		
		
		@In
		Console console;
		public GitExecution(String commandJar, Console console) {
			// constructor
			this.commandJar= commandJar;
			this.console=console;
		}

		@Override
		public void run(){
			// this method execute the jar with the correct params
			try{
				executeJar();
				if (console != null) console.addMessage("Terminó el análisis");
				else System.out.println("Terminó el análisis");
			}catch(IOException e){
				if (console != null) console.addMessage("Falló el analisis");
				else System.err.println("Falló el analisis");
				e.printStackTrace();
			}
						
		}
		private void executeJar()throws IOException {
			Process process = null;
			process = Runtime.getRuntime().exec(commandJar);			
			InputStream is = process.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);		
			while (br.readLine()!=null);
		}
		
	}
	
	
	
}
