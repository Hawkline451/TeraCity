package org.terasology.logic.console.commands;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.registry.In;

import org.terasology.teracity.gitApi.*;

/**
 * @author Hawkline451
 */
@RegisterSystem
public class ExtensionCommands extends BaseComponentSystem{
    
	public static Hashtable<String, Integer> data;
	@In
	private Console console;
	
    @Command(shortDescription = "Return the GitHub metrics (Commits in specific branch)",
    		helpText = "Get git metrics from GitHub API\n"
    				+ "<owner>: Owner name\n"
    				+"<project Name>: Project name\n"
    				+"<date>: Date commits (since YYYY-MM-DD)\n"
    				+"<branch>: branch name or branch SHA",
            requiredPermission = PermissionManager.NO_PERMISSION)
	
	public void githubCommitsTest(
    		@CommandParam(value= "ownerName", required=true) String ownerName,
    		@CommandParam(value= "projectName", required=true) String projectName,
    		@CommandParam(value= "date", required=true) String date,
    		@CommandParam(value= "branch", required=true) String branch
    		) throws IOException {
    	
    	String path = "https://api.github.com/repos/" + ownerName + 
				"/" + projectName;
    	//System.out.println(path);
    	//githubCommitsTest Hawkline451 TeraCity 2016-10-10 2016_2_R001 
    	
		JsonOperation opCommits = new JsonOperation(path);
		ArrayList<String> shaList = opCommits.getCommit(date, branch); 
    	System.out.println(shaList.toString());
    	//por el momento solo entrega el 1er commit
    	String sha = shaList.get(0);
    	opCommits.getFiles(sha);
    	
    	saveTSV(opCommits.getTable(), (projectName+"_"+branch));
    	JsonOperation op = new JsonOperation(path);
    	//console.addMessage(op.getCommit(date, branch).toString());
    	console.addMessage("File saved");
    }
	public static void saveTSV(Hashtable<String, Integer> table, String name) throws IOException{
	    
        Writer output = null;
        File file = new File("./modules/GitHub/Metrics/" + name + ".tsv");
        output = new BufferedWriter(new FileWriter(file));
   	 	BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        for(String key:table.keySet()){
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

