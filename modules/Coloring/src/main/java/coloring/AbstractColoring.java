package coloring;

import java.io.IOException;
import java.util.ArrayList;

import coloringCommands.PlaceBlockCommand;

public abstract class AbstractColoring implements IColoring, Runnable{
	String[] params;
	
	private ArrayList<String> getPaths() {
		return null;
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
