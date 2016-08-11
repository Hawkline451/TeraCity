package searchMode;

import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.metrics.AST;

public class DrawableCodeSearchRefFromVisitor extends AbstractDrawableCodeSearchRefVisitor {
	
	public DrawableCodeSearchRefFromVisitor(AST ast){
		super(ast);
	}

	@Override
	public void visit(DrawableCodePackage code) {
		CodeMap map = code.getSubmap(codeMapFactory);
		Set<MapObject> mapObjects = map.getPosMapObjects();
		for(MapObject object : mapObjects){
			if (object.isInPackage(astPackage) && isReferenced(object.toString())){	//If the .java is in the same package of astPackage
				Ys.add(Y + object.getHeight(codeMapFactory));
				
				Zs.add(Z + (object.getPositionZ() + 1));
				
				Xs.add(X + (object.getPositionX() + 1));
				
				continue;
			}
			for (String asterix: asteriskImports){      							//Check if is in a package with asterisk
				if (object.isInPackage(asterix) && isReferenced(object.toString())){
					Ys.add(Y + object.getHeight(codeMapFactory));
					
					Zs.add(Z + (object.getPositionZ() + 1));
					
					Xs.add(X + (object.getPositionX() + 1));

					continue;
				}
				else if(object.containsPackage(asterix)){
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
			}									
			if(object.isDirectedImported(directImports)){		//Check the reference by the imports
				Ys.add(Y + object.getHeight(codeMapFactory));
				
				Zs.add(Z + (object.getPositionZ() + 1));
				
				Xs.add(X + (object.getPositionX() + 1));

				continue;
			}
			else if(object.containsImport(directImports)){
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
		}
		resultReady = true;
	}
}
