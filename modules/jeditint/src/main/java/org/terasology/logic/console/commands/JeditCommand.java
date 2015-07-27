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
		 String baseFolder = "~/Escritorio/Terasology/CodeCity/";
		 String osName = System.getProperty("os.name" );
		 Process p;
		 try{
			 if (osName.contains("Windows")){
				 String[] cmd = new String[3];
				 cmd[0] ="cmd.exe";
				 cmd[1] = "/C";
				 cmd[2] = "java -jar C:/\"Program Files\"/jedit/jedit.jar "+baseFolder+className;
				 p=Runtime.getRuntime().exec(cmd);
			 }
			 else{
				 String cmd = "jedit "+baseFolder+className;
				 p=Runtime.getRuntime().exec(cmd);
			 }
		     return "Work";
		        
		 }
		 catch(IOException e1) {
			 return "You must install jedit first";
		 }
	 }
}
