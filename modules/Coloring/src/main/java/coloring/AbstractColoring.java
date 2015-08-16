package coloring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;

import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.CodeRepresentation;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import coloringCommands.ColoringCommands;
import coloringCommands.PlaceBlockCommand;

public abstract class AbstractColoring implements IColoring, Runnable{
	String[] params;
	
	
	public ArrayList<String> getClassPaths(){
		WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	CodeScale scale = new SquareRootCodeScale();
        	return getPathInfo(map,scale);
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
	}
	public ArrayList <String> getPathInfo(CodeMap map, CodeScale scale){
		CodeMapFactory factory = new CodeMapFactory(scale);
		ArrayList<String> result = new ArrayList<String>();
		for (MapObject obj: map.getMapObjects()){
			if (obj.isOrigin()){
				result.add(obj.getObject().getBase().getPath());
				CodeMap next = obj.getObject().getSubmap(scale, factory);
				result.addAll(getPathInfo(next, scale));
			}
		}
		return result;
	
	}
	public String getRootPath(){
		CodeRepresentation code =  CoreRegistry.get(CodeRepresentation.class);
		return code.getPath();
	}
	
	@Override
	public void executeColoring() {
		ArrayList<String> paths = getClassPaths();
		PlaceBlockCommand pbc = new PlaceBlockCommand();
		for (String path : paths) {
			System.out.print("clase: |" + path + "| : ");
			System.out.println(pbc.ColorBuild(path, getColor(path)));
		}
		ColoringCommands.STATE = "Esperando Análisis";
	}

	@Override
	public abstract String getColor(String path);
	
	public abstract void getDataColoring()  throws IOException;
	
	@Override
	public void run() {
		try {
			getDataColoring();
		} catch (IOException e) {
			System.err.println("Falla el coloreo");
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(String[] params) {
		this.params = params;
		ColoringCommands.STATE = "Analizando...";
		ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
		ListenableFuture<AbstractColoring> listenableFuture = executor.submit(this,this);
	    CoreRegistry.put(ListenableFuture.class, listenableFuture);
	    Futures.addCallback(listenableFuture, new FutureCallback<AbstractColoring>() {
	        public void onSuccess(AbstractColoring result) {
	        	ColoringCommands.STATE = "Coloreando...";
	        	System.out.println("Listo para pintar");
	        	
	        }

	        public void onFailure(Throwable thrown) {
	        	ColoringCommands.STATE = "Analizando...";
	            System.err.println("Falla de Analisis");
	        }
	    });
	    executor.shutdown();
	}
}
