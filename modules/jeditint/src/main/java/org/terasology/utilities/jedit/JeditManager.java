package org.terasology.utilities.jedit;

import java.io.IOException;
import java.util.ArrayList;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.input.cameraTarget.CameraTargetSystem;
import org.terasology.math.Vector2i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;

public class JeditManager {
	
	@In
	private CameraTargetSystem cameraTarget;
	
	private CodeMap map = CoreRegistry.get(CodeMap.class); ;
	 
    private CodeScale scale = new SquareRootCodeScale();
    private  CodeMapFactory factory = new CodeMapFactory(scale);
    
	public String openClass(String classPath){
		String osName = System.getProperty("os.name" );
		 
		try{
			 if (osName.contains("Windows")){
				 String[] cmd = new String[4];
				 cmd[0] ="cmd.exe";
				 cmd[1] = "/C";
				 cmd[2] = "jedit";
				 cmd[3] = classPath;
				 Runtime.getRuntime().exec(cmd);
			 }
			 else{
				 String cmd = "jedit "+classPath;
				 Runtime.getRuntime().exec(cmd);
			 }
		     return "Opening jEdit";
		        
		 }
		 catch(IOException e1) {
			 return "You must install jEdit first";
		 }
	}
	
	public void openJedit(CameraTargetSystem camera, CodeMap codemap){
		
		cameraTarget = camera;
		map = codemap;
		
		CodeRepresentation code = getCodeRepresentation();
		ClassPathVisitor visitor = new ClassPathVisitor();
		code.accept(visitor);
		
		ArrayList<String> paths = visitor.getPaths();
		
		for(String path:paths) openClass(path);
		
	}
	
	
	private MapObject getMapObject(CodeMap map, Vector2i offset, int bottom, int x1, int y1, int z1) {

		for (MapObject obj : map.getMapObjects()) {
	            int x = obj.getPositionX() + offset.getX();
	            int y = obj.getPositionZ() + offset.getY();
	            System.out.println(x+","+y+","+bottom);
	            int top = obj.getHeight(scale, factory) + bottom;
	            if(x1==x && y1==y && z1>bottom && z1<=top) return obj;
	            if (obj.isOrigin()){
	                MapObject mo = getMapObject(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), top, x1, y1, z1);
	                if (mo!=null) return mo;
	            }
	        }
	        return null;
	 }
	private CodeRepresentation getCodeRepresentation(){
		 int x = cameraTarget.getTargetBlockPosition().getX();
	     int y = cameraTarget.getTargetBlockPosition().getZ();
	     int z= cameraTarget.getTargetBlockPosition().getY();
	     int base = 9;
		 MapObject obj = getMapObject(map, Vector2i.zero(),base,x,y,z);
		 CodeRepresentation code = obj.getObject().getBase();   
		 return code;
	}
	 
	public String getPath(CameraTargetSystem camera,CodeMap codemap) {
		cameraTarget=camera;
		map = codemap;
		CodeRepresentation code = getCodeRepresentation();
		return code.getPath();
	}
}
