package org.terasology.utilities.jedit;

import java.io.IOException;

public class JeditManager {
	public static String openJedit(String className){
		String osName = System.getProperty("os.name" );
		String baseFolder = System.getProperty("user.dir");
		String dir = baseFolder+"/CodeCity/"+className;
		 
		try{
			 if (osName.contains("Windows")){
				 String[] cmd = new String[4];
				 cmd[0] ="cmd.exe";
				 cmd[1] = "/C";
				 cmd[2] = "jedit";
				 cmd[3] = dir;
				 Runtime.getRuntime().exec(cmd);
			 }
			 else{
				 String cmd = "jedit "+dir;
				 Runtime.getRuntime().exec(cmd);
			 }
		     return "jEdit "+className;
		        
		 }
		 catch(IOException e1) {
			 return "You must install jedit first";
		 }
	 }		
}
