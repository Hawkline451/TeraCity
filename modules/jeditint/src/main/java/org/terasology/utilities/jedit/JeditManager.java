package org.terasology.utilities.jedit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.IMapObject;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.map.NullMapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.math.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;

public class JeditManager {
	
	private static CameraTargetSystem cameraTarget;
	
	private static CodeMap map = CoreRegistry.get(CodeMap.class); ;
	 
    private static CodeScale scale = new HalfLinearCodeScale();
    private  static CodeMapFactory factory = new CodeMapFactory(scale);
    
    /**
     * Send the command console to open jEdit.
     * @param classesPath
     * @return
     */
	public static String openClasses(String[] classesPath){
		
		String osName = System.getProperty("os.name" );
		int largo = classesPath.length + 3;
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
	 * Open jEdit of the classes of the targetBlock
	 * @param camera
	 * @param codemap
	 */
	public static void openJeditWhenPressed(CameraTargetSystem camera, CodeMap codemap){
		
		cameraTarget = camera;
		map = codemap;
		
		CodeRepresentation code = getCodeRepresentation();
		ClassPathVisitor visitor = new ClassPathVisitor();
		code.accept(visitor);
		
		ArrayList<String> paths = visitor.getPaths();
		String [] pathsS = new String[paths.size()];
		paths.toArray(pathsS);
		
		openClasses(pathsS);
	}
	
	
	private static IMapObject getMapObject(CodeMap map, Vector2i offset, int bottom, int x1, int y1, int z1) {

		for (MapObject obj : map.getMapObjects()) {
	            int x = obj.getPositionX() + offset.getX();
	            int y = obj.getPositionZ() + offset.getY();
	            int top = obj.getHeight(scale, factory) + bottom;
	            
	            if(x1==x && y1==y && z1>bottom && z1<=top) return obj;
	            
	            if (obj.isOrigin()){
	                IMapObject mo = getMapObject(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), top, x1, y1, z1);
	                if (mo.getHeight(scale, factory) != 0) return mo;
	            }
	        }
	        return new NullMapObject();
	 }
	
	/**
	 * Get CodeRepresentation of the mapObject targeted by the camera;
	 * @return
	 */
	private static CodeRepresentation getCodeRepresentation(){
		
		 int x = cameraTarget.getTargetBlockPosition().getX();
	     int y = cameraTarget.getTargetBlockPosition().getZ();
	     int z= cameraTarget.getTargetBlockPosition().getY();
	     int base = 9;
	     
		 IMapObject obj = getMapObject(map, Vector2i.zero(),base,x,y,z);
		 CodeRepresentation code = obj.getObject().getBase();   
		 return code;
	}
	
	/**
	 * Get path of the mapObject targeted by the camera;
	 * @param camera
	 * @param codemap
	 * @return
	 */
	public static String getPath(CameraTargetSystem camera,CodeMap codemap) {
		cameraTarget=camera;
		map = codemap;
		CodeRepresentation code = getCodeRepresentation();
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
