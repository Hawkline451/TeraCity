package coloring;

import java.io.IOException;
import java.util.ArrayList;

import coloringCommands.PlaceBlockCommand;

public abstract class AbstractColoring implements IColoring, Runnable{
	String[] params;
	
	private ArrayList<String> getPaths() {
		ArrayList<String> paths = new ArrayList<>();
		paths.add("CodeMap");
		paths.add("CodeCityBuildingProvider");
		paths.add("CodeCityBuildingRasterizer");
		paths.add("CodeCityGroundProvider");
		paths.add("CodeCityGroundRasterizer");
		paths.add("CodeCityWorldGenerator");
		paths.add("CodeMap");
		paths.add("CodeMapFactory");
		paths.add("CodeMapHash");
		paths.add("CodeMapNull");
		paths.add("CodePackage");
		paths.add("MapObject");
		paths.add("DrawableCodePackage");
		paths.add("DrawableCodeFactory");
		paths.add("DrawableCodeClass");
		paths.add("DrawableCode");
		paths.add("CodeClass");
		paths.add("CodePackage");
		paths.add("CodeRepresentation");
		paths.add("CodeScale");
		paths.add("LinearCodeScale");
		paths.add("SquareRootCodeSca");
		paths.add("");
		paths.add("");
		paths.add("");
		paths.add("");
		return paths;
	}
	
	@Override
	public void executeColoring() {
		ArrayList<String> paths = getPaths();
		PlaceBlockCommand pbc = new PlaceBlockCommand();
		for (String path : paths) {
			System.out.println("clase: |" + path + "|\n");
			System.out.println(pbc.ColorBuild(path, getColor(path)));
		}
	}

	@Override
	public abstract String getColor(String path);
	
	public abstract void getDataColoring()  throws IOException;
	
	@Override
	public void run() {
		try {
			getDataColoring();
			executeColoring();
		} catch (IOException e) {
			System.err.println("Falló el coloreo");
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(String[] params) {
		this.params = params;
		Thread thread = new Thread(this);
		thread.start();
	}
}
