package org.terasology.utilities.jedit;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.rendering.nui.NUIManager;

public class JeditManager {
  
    /**
     * Send the command console to open jEdit.
     * @param classesPath
     * @return
     */
	public static String openClasses(String[] classesPath){
		
		String osName = System.getProperty("os.name" );
		String[] cmd = new String[4];
		
		try{
			 if (osName.contains("Windows")){
				 cmd[0] ="cmd.exe";
				 cmd[1] = "/C";
				 cmd[2] = "jedit";
				 cmd[3] = "-norestore";
				 cmd = fillCommand(cmd, classesPath, 4);
			 }
			 else{
				 cmd[0] = "jedit";
				 cmd[1] = "-norestore";
				 cmd = fillCommand(cmd, classesPath, 2);
			 }
			 
			 Runtime.getRuntime().exec(cmd);
		     return "Opening jEdit";
		        
		 }
		 catch(IOException e1) {
			 return "You must install jEdit first";
		 }
	}
	
    /**
     * Send path to open the class inside of Minecraft.
     * @param classesPath
     * @param nuiManager
     * @return
     */
	public static String editClassInside(String pathFile, NUIManager nuiManager){
		
		try{
			File f = new File(pathFile);
			if(f.exists() && !f.isDirectory()) { 
				nuiManager.toggleScreen("engine:editClassScreen");
				return "Read "+pathFile;
			}else if(f.isDirectory()){
				return pathFile+" is a directory not a file!";
			}else{
				return "No ClassPath found!";
			}
		 }
		 catch(Exception e1) {
			 return "No ClassPath found!";
		 }
	}		
		
	
	/**
	 * Open jEdit of the classes of the targetBlock
	 * @param camera
	 * @param codemap
	 */
	public static void openJeditWhenPressed(CameraTargetSystem camera){
		
		CodeRepresentation code = CodeRepresentation.getCodeRepresentation(camera);
		ClassPathVisitor visitor = new ClassPathVisitor();
		code.accept(visitor);
		
		ArrayList<String> paths = visitor.getPaths();
		String [] pathsS = new String[paths.size()];
		paths.toArray(pathsS);
		
		openClasses(pathsS);
	}

	/**
	 * Open edit class of the classes of the targetBlock
	 * @param camera
	 * @param nuiManager 
	 * @param codemap
	 */	
	public static void editClassWhenPressed(CameraTargetSystem camera, NUIManager nuiManager){
		String path = getPath(camera);
		editClassInside(path, nuiManager);
	}
	
	/**
	 * Get path of the mapObject targeted by the camera;
	 * @param camera
	 * @param codemap
	 * @return
	 */
	public static String getPath(CameraTargetSystem camera) {
		CodeRepresentation code = CodeRepresentation.getCodeRepresentation(camera);
		return code.getPath();
	}
	
	/**
	 * Fill the command with the classes to open
	 * @param base
	 * @param classesPath
	 * @param commandLength
	 * @return
	 */
	private static String[] fillCommand(String[] base, String[] classesPath, int commandLength){
		String[] command = new String[classesPath.length + commandLength];
		int pos = 0;
		
		for(;pos<commandLength;pos++) command[pos] = base[pos];
				
		for(;pos<classesPath.length+commandLength;pos++) command[pos] = classesPath[pos-commandLength];	
		return command;
	}
	
}
