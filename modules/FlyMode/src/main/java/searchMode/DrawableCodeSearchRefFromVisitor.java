package searchMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.metrics.AST;

import com.github.javaparser.ast.ImportDeclaration;

public class DrawableCodeSearchRefFromVisitor extends AbstractDrawableCodeSearchRefVisitor {
	protected List<String> directImports;
	protected List<String> asteriskImports;
	
	public DrawableCodeSearchRefFromVisitor(AST ast){
		super(ast);
		directImports = new ArrayList<String>();
		asteriskImports = new ArrayList<String>();
		parseImports(ast.getImports());
	}
	
	private boolean isSameProject(String pack){
		String[] paths = astPackage.split("\\.");
		String project = paths[0] + "." + paths[1];
		return pack.contains(project);
	}
	
	private boolean isReferenced(String className) {
		return myAst.contains(className);
	}
	
	private void parseImports(List<ImportDeclaration> impts){
		for(ImportDeclaration imp : impts){
			String impt = imp.toString().trim();
			if (!isSameProject(impt))
				continue;
			String[] path = impt.split("\\.");
			if (path[path.length-1].trim().equals("*;")){
				String pack = imp.toString().trim().split(" ")[1];
				asteriskImports.add(pack.trim().substring(0, pack.length() - 3));
				continue;
			}
			String[] name = path[path.length - 1].split(";");
			directImports.add(name[name.length - 1].trim() + ".java");
		}
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
			else if(object.isInPackage(asteriskImports) && isReferenced(object.toString())){ //Check if is in a package with asterisk

				Ys.add(Y + object.getHeight(codeMapFactory));
				
				Zs.add(Z + (object.getPositionZ() + 1));
				
				Xs.add(X + (object.getPositionX() + 1));

				continue;
			}
			else if(object.containsPackage(asteriskImports)){
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
			else if(object.isDirectedImported(directImports)){		//Check the reference by the imports
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
