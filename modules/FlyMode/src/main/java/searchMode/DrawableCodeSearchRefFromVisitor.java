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
import org.terasology.codecity.world.metrics.AST;
import org.terasology.math.geom.Vector3i;

import com.github.javaparser.ast.ImportDeclaration;

public class DrawableCodeSearchRefFromVisitor implements DrawableCodeVisitor {
	private int Y;
	private int X;
	private int Z;
	private List<Integer> Ys;
	private List<Integer> Xs;
	private List<Integer> Zs;
	private CodeMapFactory codeMapFactory;
	private AST myAst;
	private List<String> directImports;
	private List<String> asteriskImports;
	private String astPackage;
	private boolean resultReady;
	
	public DrawableCodeSearchRefFromVisitor(AST ast){
		myAst = ast;
		String pack = ast.getPackage().toString().trim().split(" ")[1].trim();
		astPackage = pack.substring(0, pack.length() - 1);
		directImports = new ArrayList<String>();
		asteriskImports = new ArrayList<String>();
		parseImports(ast.getImports());
		codeMapFactory = new CodeMapFactory();
		Y = X = Z = 0;
		Ys = new ArrayList<Integer>();
		Xs = new ArrayList<Integer>();
		Zs = new ArrayList<Integer>();
		resultReady = false;
	}
	
	private boolean isSameProject(String pack){
		String[] paths = astPackage.split("\\.");
		String project = paths[0] + "." + paths[1];
		return pack.contains(project);
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
	
	private boolean isReferenced(String className) {
		return myAst.contains(className);
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
	
	public boolean resultReady(){
		return resultReady;
	}
	
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
