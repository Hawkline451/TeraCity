package searchMode;

import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCodeClass;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.DrawableCodeVisitor;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.In;

@RegisterSystem
public class DrawableCodeSearchVisitor implements DrawableCodeVisitor{
	private int totalY;
	private int totalX;
	private int totalZ;
	private CodeScale codeScale;
	private CodeMapFactory codeMapFactory;
	private String query;
	private boolean resultReady;
	
	public DrawableCodeSearchVisitor(String className){
		codeScale = new HalfLinearCodeScale();                                                  
		codeMapFactory = new CodeMapFactory(codeScale);
		query = className;
		totalX = 0;
		totalY = 0;
		totalZ = 0;
		resultReady = false;
	}
	
	@Override
	public void visit(DrawableCodeClass code) {
		//intentionally blank.
	}

	@Override
	public void visit(DrawableCodePackage code) {
		CodeMap map = code.getSubmap(codeScale, codeMapFactory);
		Set<MapObject> mapObjects = map.getPosMapObjects();
		for(MapObject object : mapObjects){
			if(object.toString().equals(query)){
				resultReady = true;
				totalY += object.getHeight(codeScale, codeMapFactory);
				totalZ += (object.getPositionZ() + 1);
				totalX += (object.getPositionX() + 1);
				break;
			}
			else if(object.containsClass(query)){
				totalY += object.getHeight(codeScale, codeMapFactory);
				totalZ += (object.getPositionZ() + 1);
				totalX += (object.getPositionX() + 1);
				object.getObject().accept(this);
				break;
			}
			else continue;
		}
	}
	
	/**
	 * Returns whether the result is ready.
	 * @return
	 */
	public boolean resultReady(){
		return resultReady;
	}
	
	/**
	 * Returns the total position.
	 * @return
	 */
	public Vector3i getPosition(){
		return new Vector3i(totalX, totalY, totalZ);
	}
}
