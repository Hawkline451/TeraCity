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
import searchMode.CodeBuildingUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
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
    		ConsoleCommand classSearchCommand = console.getCommand(new Name("findBuilding"));
    		EntityRef ent = null;
     		
    		readTsvFile(tsvName);
    		Set<String> keys = data.keySet();
    		
    		for (String key:keys){
        		ArrayList<String> parameters = new ArrayList<String>();
        		parameters.add(key);
        		parameters.add("red");
        		try{
        			classSearchCommand.execute(parameters, ent);
        		}
        		catch(Exception e){
        			continue;
        		}
    			data.get(key);
    		}
    		
    		
    		
    }
    private static void readTsvFile(String name){
		// This method read a Tsv File and Set data hashtable...
    	String tsvPath = "modules/GitHub/Metrics/" + name + ".tsv";
		String linea;
		BufferedReader br = null;
		
		data = new Hashtable<String, Integer>();
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
    
}


