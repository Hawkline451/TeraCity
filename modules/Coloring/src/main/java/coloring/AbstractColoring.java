package coloring;

import java.io.IOException;

public abstract class AbstractColoring implements IColoring, Runnable{
	String[] params;
	
	@Override
	public abstract void executeColoring(); 

	@Override
	public abstract String getColor();
	
	@Override
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
