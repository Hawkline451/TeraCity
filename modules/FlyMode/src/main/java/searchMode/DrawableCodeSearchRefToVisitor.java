package searchMode;

import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;

public class DrawableCodeSearchRefToVisitor extends AbstractDrawableCodeSearchRefVisitor {
	String className;
	
	public DrawableCodeSearchRefToVisitor(CodeRepresentation code){
		super(code.getAst());
		className = code.getName().trim().split("\\.")[0];
	}

	@Override
	public void visit(DrawableCodePackage code) {
		CodeMap map = code.getSubmap(codeMapFactory);
		Set<MapObject> mapObjects = map.getPosMapObjects();
		for(MapObject object : mapObjects){
			if(object.hasText(className)){
				Ys.add(Y + object.getHeight(codeMapFactory));
				
				Zs.add(Z + (object.getPositionZ() + 1));
				
				Xs.add(X + (object.getPositionX() + 1));
				
				widths.add(object.getWidth(codeMapFactory));
				names.add(object.toString());

				continue;
			}
			else if(object.containsText(className)){
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
				
				continue;
			}
			else continue;
		}
		resultReady = true;
	}
}
