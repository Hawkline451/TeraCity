
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

import org.terasology.teracity.gitApi.JsonOperation;

import console.GitMetricsConsole;



public class Test {
	
	String outPath = "modules/GitHub/Metrics/";
	public static Hashtable<String, Integer> data;
	
	public static void main(String[] args) throws IOException {
		 
    	String ownerName="Hawkline451";
		String date="2016-08-10";
		String branch="2016_2_R006";
		String projectName= "TeraCity";
		String path = "https://api.github.com/repos/" + ownerName + 
				"/" + projectName;
    	//System.out.println(path);
    	/*JsonReader reader = new JsonReader();
    	JSONArray array = 
				reader.getJson(path);
		System.out.println(array);*/

		//reader.getJsonObject(array, "sha");
		//reader.getJson("https://api.github.com/repos/Hawkline451/TeraCity/issues");
		
		//Path path = Paths.get("modules/jeditint/src/main/java/org/terasology/rendering/nui/layers/ingame/EditClassScreen.java");
		//System.out.println(path.getFileName());
    	
    	//GitMetricsConsole console = new GitMetricsConsole();
    	//console.githubCommits(ownerName, projectName, date, branch);
		
		/*JsonOperation opCommits = new JsonOperation(path);
		ArrayList<String> shaList = opCommits.getCommit(date, branch); 
    	System.out.println(shaList.toString());
    	//Por ahora solo lee el 1er elemento.
    	String sha = shaList.get(0);
    	opCommits.getFiles(sha);
    	
    	saveTSV(opCommits.getTable());
		*/
		readTsvFile("TeraCity_master");
	}
	private static void readTsvFile(String name){
		// This method read a Tsv File and Set data hashtable...
    	String tsvPath = "modules/GitHub/Metrics/" + name + ".tsv";
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
				System.out.println(args[0]+"\n");
				data.put(args[0], Integer.parseInt(args[1]));
				
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void saveTSV(Hashtable<String, Integer> table) throws IOException{
	    
	        Writer output = null;
	        File file = new File("./asdasd.tsv");
	        output = new BufferedWriter(new FileWriter(file));
       	 	BufferedWriter bw = new BufferedWriter(new FileWriter(file));
	        for(String key:table.keySet()){
	        	//Path path = Paths.get(key);
	    		 //bw.write(path.getFileName().toString());
	        	bw.write(key);
	        	bw.write("\t");
	        	bw.write(table.get(key).toString());
	        	bw.write("\n");
	        }
	        bw.close();
	        output.close();
	        System.out.println("File has been written");	   
	}
}