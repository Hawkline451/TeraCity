/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package console;


import gitMetrics.GitMetric;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.ConsoleCommand;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.exceptions.CommandExecutionException;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.naming.Name;
import org.terasology.registry.In;

/**
 * @author mperalta92, kidonkey
 */
@RegisterSystem
public class GitCommand extends BaseComponentSystem{
	//private final CodeScale scale = new SquareRootCodeScale();
    //private final CodeMapFactory factory = new CodeMapFactory(scale);
    
	public static Hashtable<String, Integer> data;
	public static HashMap<String, Integer> gitCommits;
	public static HashMap<String, ArrayList<Integer>> dataPMD;
	public static Hashtable<String, ArrayList<Integer>> datafindBugs;

	public static String metrica;
	@In
	private Console console;
	
    @Command(shortDescription = "Activate the GitHub metrics",
    		helpText = "Calcula la cantidad de Bugs o versiones de cada clase de un proyecto\n"
    				+ "<remotePath>: URL del repositorio remoto del proyecto\n"
    				+"<project Name>: Nombre del folder que contiene al proyecto\n"
    				+"<metric>: metrica que desea implementar: puede ser 'bugs' o 'versions'",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String github(
    		@CommandParam(value= "remotePath", required=true) String remotePath,
    		@CommandParam(value= "projectName", required=true) String projectName,
    		@CommandParam(value= "metric", required=true) String metric
    		) throws IOException {
    		String tempFolderPath="modules/GitHub/tempRepo/";
    	GitMetric git = new GitMetric(metric, remotePath, projectName,tempFolderPath ,console);
    		git.execute();
    		git.setData();
    		data= git.getData();
    		
    		System.out.println(data.toString());
    		System.out.println("Prueba!!!!!");
    		System.out.println("\n\n\n\n");
    		
    		String result;
    		if (data!=null) {
				StringBuilder r = new StringBuilder();
				r.append("resultado de las metrica " + metric + "...\n");
				Enumeration<String> c = data.keys();
				Enumeration<Integer> v = data.elements();
				while (v.hasMoreElements()) {
					r.append(c.nextElement() + "  " + v.nextElement() + "\n");
				}
				r.append("fin\n");
				result = r.toString();
			}
    		else{
    			result="algo fallo en el analisis";
    		}
			return result;
    }
    
    @Command(shortDescription = "Activate the GitHub metrics",
    		helpText = "Read a TSV with the number of commits per file\n",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public void githubColoring(
    		@CommandParam(value= "tsvName", required=true) String tsvName
    		) throws IOException, CommandExecutionException {
    	
    		ConsoleCommand colorFace = console.getCommand(new Name("colorBuilding"));
    		ConsoleCommand colorRow = console.getCommand(new Name("colorBuildingLine"));
    		EntityRef ent = null;
    		
    		//int maxCommit = Collections.max(gitCommits.values());
     		
    		readTsvFile(tsvName);
    		Set<String> keys = gitCommits.keySet();
    		int maxCommit = Collections.max((gitCommits.values()));
    		
    		for (String key:keys){
        		ArrayList<String> parameters = new ArrayList<String>();
        		ArrayList<String> parameters2 = new ArrayList<String>();
        		
        		parameters.add(key);
        		
        		if (gitCommits.get(key)<=(maxCommit*0.3)){
        			parameters.add("transparentBlue");  
        		}
        		else if (gitCommits.get(key)>(maxCommit*0.3) && gitCommits.get(key)<=(maxCommit*0.6)){
        			parameters.add("transparentYellow");  
        		}
        		else if (gitCommits.get(key)>(maxCommit*0.6) && gitCommits.get(key)<=(maxCommit)){
        			parameters.add("transparentRed");  
        		}
        		else{
        			parameters.add("transparentGreen"); 
        		}
        		//parameters.add("transparentGreen"); 
        		parameters.add("W");
        		
        		parameters2.add(key);
        		parameters2.add("transparentGreen");
        		parameters2.add("S");
        		parameters2.add("4");
        		try{
        			colorFace.execute(parameters, ent);
        			//githubColoring TeraCity_master
        			//colorRow.execute(parameters2, ent);
        		}
        		catch(Exception e){
        			continue;
        		}
        		gitCommits.get(key);
    		}    		
    		
    }
    private static void readTsvFile(String name){
 		// This method read a Tsv File and Set data hashtable...
     	String tsvPath = "modules/GitHub/Metrics/" + name + ".tsv";
 		String linea;
 		BufferedReader br = null;
 		
 		gitCommits = new HashMap<String, Integer>();
 		try {
 			br = new BufferedReader(new FileReader(tsvPath));
 		} catch (FileNotFoundException e) {
 			e.printStackTrace();
 		}
 		try {
 			while((linea=br.readLine())!=null){
 				String [] args = linea.split("\t");
 				gitCommits.put(args[0], Integer.parseInt(args[1])); 				
 			}
 			br.close();
 		} catch (NumberFormatException | IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
 	}
    
    @Command(shortDescription = "Activate the findBugs metrics",
    		helpText = "Read findBugsAnalsis.tsv TSV with the line that presents bug on each file\n"
    				+ " so it uses the last findBugs analysis launch by command findBugsAnalysis",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public void findBugsColoring(
    		) throws IOException, CommandExecutionException {
    	
    		ConsoleCommand colorRow = console.getCommand(new Name("colorBuildingLine"));
    		EntityRef ent = null;
     		
    		readFindBugsTsvFile();
    		Set<String> keys = datafindBugs.keySet();
    		
    		for (String key:keys){
    			ArrayList<String> parameters = null;
        		console.addMessage("Coloring on " + key + ", south face");
        		for (int i : datafindBugs.get(key)) {
        			
        			parameters = new ArrayList<String>();        		
            		parameters.add(key);        		
            		parameters.add("transparentRed");        		
            		parameters.add("S");
        			// Don not color the general problems proper to one class at now
        			if (i != -1) {
        				// Math.round(i/2.0) to higlight the good one
        				parameters.add(String.valueOf(Math.round(i/2.0)));
        				console.addMessage("Coloring line " + Math.round(i/2.0));
        				try{
                			colorRow.execute(parameters, ent);
                		}
                		catch(Exception e){
                			continue;
                		}
        			}        			 
        		}        		
    		}	
    }
    
    private static void readFindBugsTsvFile(){
		// This method read a Tsv File and Set data hashtable...
    	String tsvPath = "modules/GitHub/Metrics/findBugsAnalysis.tsv";
		String linea;
		BufferedReader br = null;
		
		gitCommits = new HashMap<String, Integer>();
		datafindBugs = new Hashtable<String, ArrayList<Integer>>();

		try {
			br = new BufferedReader(new FileReader(tsvPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((linea=br.readLine())!=null){
				String [] args = linea.split("\t");

				ArrayList<Integer> temp = new ArrayList<Integer>();
				for (int i = 1; i < args.length; i++) {
					temp.add(Integer.parseInt(args[i]));
					datafindBugs.put(args[0],temp);
				}
				
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    @Command(shortDescription = "Activate the PMD metrics",
    		helpText = "Read (metric).tsv TSV with the line that presents bug on each file\n"
    				+ " so it uses the last PMD analysis with a selected metric",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public void pmdColoring( @CommandParam(value="metric", required=true) String metric
    		) throws IOException, CommandExecutionException {
    	
    		ConsoleCommand colorRow = console.getCommand(new Name("colorBuildingLine"));
    		EntityRef ent = null;
     		
    		readPMDTsvFile(metric);
    		Set<String> keys = dataPMD.keySet();
    		
    		for (String key:keys){        	        		
        		console.addMessage("Coloring on " + key + ", east Face");
    			ArrayList<String> parameters = null;

        		for (int i : dataPMD.get(key)) {        			
        			parameters = new ArrayList<String>();        		
            		parameters.add(key);        		
            		parameters.add("transparentYellow");        		
            		parameters.add("E");
        			if (i != -1) {
        				// Math.round(i/2.0) to higlight the good one
        				parameters.add(String.valueOf(Math.round(i/2.0)));
        				console.addMessage("Coloring line " + Math.round(i/2.0));
        				try{
                			colorRow.execute(parameters, ent);
                		}
                		catch(Exception e){
                			continue;
                		}
        			}        			 
        		}        		
    		}	
    }
    
    private static void readPMDTsvFile(String type){
		// This method read a Tsv File and Set data hashtable...
    	String tsvPath = "modules/PMDColoring/results/"+type+".tsv";
		String linea;
		BufferedReader br = null;
		
		dataPMD = new HashMap<String, ArrayList<Integer>>();

		try {
			br = new BufferedReader(new FileReader(tsvPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			while((linea=br.readLine())!=null){
				String [] args = linea.split("\t");
				ArrayList<Integer> temp = new ArrayList<Integer>();
				for (int i = 1; i < args.length; i++) {
					temp.add(Integer.parseInt(args[i]));
					dataPMD.put(args[0],temp);
				}
				
			}
			br.close();
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}


