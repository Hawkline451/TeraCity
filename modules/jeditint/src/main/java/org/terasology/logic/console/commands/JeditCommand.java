package org.terasology.logic.console.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;

@RegisterSystem
public class JeditCommand  extends BaseComponentSystem {
	 @Command( shortDescription = "Open jedit", helpText = "Open jedit in the class of the selected structure" )
     public String jedit(@CommandParam("Class") String className) {
		 String osName = System.getProperty("os.name" );
		 String baseFolder = System.getProperty("user.dir");
		 String dir = baseFolder+"/CodeCity/"+className;
		 Process p;
		 
		 try{
			 if (osName.contains("Windows")){
				 String[] cmd = new String[4];
				 cmd[0] ="cmd.exe";
				 cmd[1] = "/C";
				 cmd[2] = "\"C:/Program Files/jedit/jedit.exe\"";
				 cmd[3] = dir;
				 p=Runtime.getRuntime().exec(cmd);
			 }
			 else{
				 String cmd = "jedit "+dir;
				 p=Runtime.getRuntime().exec(cmd);
			 }
		     return "Work";
		        
		 }
		 catch(IOException e1) {
			 return "You must install jedit first";
		 }
	 }
}
