package searchMode;

import java.util.ArrayList;
import java.util.List;

import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.DrawableCodeClass;
import org.terasology.codecity.world.map.DrawableCodePackage;
import org.terasology.codecity.world.map.DrawableCodeVisitor;
import org.terasology.codecity.world.metrics.AST;
import org.terasology.math.geom.Vector3i;

import com.github.javaparser.ast.ImportDeclaration;

public abstract class AbstractDrawableCodeSearchRefVisitor implements DrawableCodeVisitor {
	protected int Y;
	protected int X;
	protected int Z;
	protected List<Integer> Ys;
	protected List<Integer> Xs;
	protected List<Integer> Zs;
	protected CodeMapFactory codeMapFactory;
	protected AST myAst;
	protected List<String> directImports;
	protected List<String> asteriskImports;
	protected String astPackage;
	protected boolean resultReady;
	
	protected AbstractDrawableCodeSearchRefVisitor(AST ast) {
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
	
	protected void parseImports(List<ImportDeclaration> impts){
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
	
	
	protected boolean isSameProject(String pack){
		String[] paths = astPackage.split("\\.");
		String project = paths[0] + "." + paths[1];
		return pack.contains(project);
	}
	
	
	protected boolean isReferenced(String className) {
		return myAst.contains(className);
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
	
	@Override
	public void visit(DrawableCodeClass code) {
		// TODO Auto-generated method stub

	}

	@Override
	abstract public void visit(DrawableCodePackage code);

}
