package console;

import java.util.Hashtable;

import java.io.IOException;

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
public class GitMetricsConsole extends BaseComponentSystem{
    
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
	
	public void githubCommits(
    		@CommandParam(value= "ownerName", required=true) String ownerName,
    		@CommandParam(value= "projectName", required=true) String projectName,
    		@CommandParam(value= "date", required=true) String date,
    		@CommandParam(value= "branch", required=true) String branch
    		) throws IOException {
    	
    	String path = "https://api.github.com/repos/" + ownerName + 
				"/" + projectName + "/commits?since=" + date + "&sha=" + branch;
    	//System.out.println(path);
    	//githubCommits Hawkline451 TeraCity 2016-10-10 2016_2_R001  	

    	JsonOperation ops = new JsonOperation(path);
    	//ops.getCommit();
    	console.addMessage("fin");
    }
}


