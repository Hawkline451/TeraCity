package coloring;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.Block;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.family.BlockFamily;

import coloringCommands.PlaceBlockCommand;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

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
		paths.add("SquareRootCodeScale");
		return paths;
	}
	
	@Override
	public void executeColoring() {
		ArrayList<String> paths = getPaths();
		PlaceBlockCommand pbc = new PlaceBlockCommand();
		for (String path : paths) {
			System.out.print("clase: |" + path + "| : ");
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
		} catch (IOException e) {
			System.err.println("Falló el coloreo");
			e.printStackTrace();
		}
	}
	
	@Override
	public void execute(String[] params) {
		this.params = params;
		
		ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(1));
		ListenableFuture<AbstractColoring> listenableFuture = executor.submit(this,this);
	    CoreRegistry.put(ListenableFuture.class, listenableFuture);
	    Futures.addCallback(listenableFuture, new FutureCallback<AbstractColoring>() {
	        public void onSuccess(AbstractColoring result) {
	        	System.out.println("Listo para pintar");
	        }

	        public void onFailure(Throwable thrown) {
	            System.out.println("Falla de Analisis");
	        }
	    });
	    executor.shutdown();
	}
}
