package searchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCodeClass;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.DrawableCodeVisitor;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.HalfLinearCodeScale;
import org.terasology.math.geom.Vector3i;

public class DrawableCodeSearchTextVisitor implements DrawableCodeVisitor {
	private int Y;
	private int X;
	private int Z;
	private List<Integer> Ys;
	private List<Integer> Xs;
	private List<Integer> Zs;
	private CodeMapFactory codeMapFactory;
	private String query;
	private boolean resultReady;
	
	public DrawableCodeSearchTextVisitor(String text) {
		codeMapFactory = new CodeMapFactory();
		query = text;
		resultReady = false;
		Y = X = Z = 0;
		Ys = new ArrayList<Integer>();
		Xs = new ArrayList<Integer>();
		Zs = new ArrayList<Integer>();
	}
	

	@Override
	public void visit(DrawableCodeClass code) {
		//intentionally blank

	}

	@Override
	public void visit(DrawableCodePackage code) {
		CodeMap map = code.getSubmap(codeMapFactory);
		Set<MapObject> mapObjects = map.getPosMapObjects();
		for(MapObject object : mapObjects){
			if(object.hasText(query)){
				Ys.add(Y + object.getHeight(codeMapFactory));
				
				Zs.add(Z + (object.getPositionZ() + 1));
				
				Xs.add(X + (object.getPositionX() + 1));
			}
			else if(object.containsText(query)){
				int auxY, auxZ, auxX;
				auxY = Y;
				auxZ = Z;
				auxX = X;
				Y += object.getHeight(codeMapFactory);
				Z += (object.getPositionZ() + 1);
				X += (object.getPositionX() + 1);
				object.getObject().accept(this);
				Y = auxY;
				Z = auxZ;
				X = auxX;
			}
			else continue;
		}
		resultReady = true;
	}
	
	/**
	 * @return true if the result is ready, false otherwise.
	 */
	public boolean resultReady(){
		return resultReady;
	}
	
	/**
	 * @return List of vectors with all the positions of classes 
	 * that have the text <code>query</code> in their file.
	 */
	public List<Vector3i> getVectors(){
		if (!resultReady)
			return null;
		List<Vector3i> result = new ArrayList<Vector3i>();
		for (int i = 0; i < Ys.size(); i++){
			result.add(new Vector3i(Xs.get(i), Ys.get(i), Zs.get(i)));
		}
		return result;
	}
}
