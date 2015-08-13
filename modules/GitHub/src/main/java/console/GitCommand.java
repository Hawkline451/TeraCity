/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package console;


import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.util.FS;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.Console;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.math.Vector2i;
import org.terasology.math.geom.Vector3i;
import org.terasology.registry.CoreRegistry;
import org.terasology.registry.In;
import org.terasology.world.WorldProvider;
import org.terasology.world.block.BlockManager;
import org.terasology.world.block.BlockUri;
import org.terasology.world.block.family.BlockFamily;
import org.terasology.codecity.world.map.CodeMap;
import org.terasology.codecity.world.map.CodeMapFactory;
import org.terasology.codecity.world.map.MapObject;
import org.terasology.codecity.world.structure.scale.CodeScale;
import org.terasology.codecity.world.structure.scale.SquareRootCodeScale;


/**
 * @author mperalta92, kidonkey
 */
@RegisterSystem
public class GitCommand extends BaseComponentSystem{
	private final CodeScale scale = new SquareRootCodeScale();
    private final CodeMapFactory factory = new CodeMapFactory(scale);
    
	public static Hashtable<String, Integer> data;
	public static String metrica;
	@In
	private Console console;
	
    @Command(shortDescription = "Activate the GitHub metrics",
    		helpText = "Calcula la cantidad de Bugs o versiones de cada clase de un proyecto\n"
    				+ "<remotePath>: URL del repositorio remoto del proyecto\n"
    				+"<project Name>: Nombre del folder que contiene al proyecto\n"
    				+"<metric>: metrica que desea implementar: puede ser 'bugs' o 'versions'",
            requiredPermission = PermissionManager.NO_PERMISSION)
    public String github(
    		@CommandParam(value= "remotePath", required=true) String remotePath,
    		@CommandParam(value= "projectName", required=true) String projectName,
    		@CommandParam(value= "metric", required=true) String metric
    		) throws IOException, NoHeadException, GitAPIException {
    		metrica=metric;
    		data=new Hashtable<String,Integer>();
    		Thread t = new Thread(new ThreadGithubExecution(remotePath,projectName,metrica, console));
    		t.start();
    		
    		return "Esperando por resultados del analisis... ";
    }
    
    @Command(shortDescription = "Colors the city based on the result of the metric")
    public String applyColoring() {
    	String color=null;
    	Set set = data.entrySet();
    	Iterator it = set.iterator();
    	Map.Entry entry=(Map.Entry) it.next();
    	if(entry.getKey().toString()!=null){
    		color=getColor((String)entry.getKey());
    	}
		if(color==null) return "Color no seteado";
    	BlockFamily blockFamily = getBlockFamily(color);
        WorldProvider world = CoreRegistry.get(WorldProvider.class);
        if (world != null) {
        	CodeMap map = CoreRegistry.get(CodeMap.class);
        	processMap(map, Vector2i.zero(), 10, world, blockFamily);//10 default ground level
            return "Success";
        }
        throw new IllegalArgumentException("Sorry, something went wrong!");
    }
    private void processMap(CodeMap map, Vector2i offset, int level, WorldProvider world, BlockFamily blockFamily) {
        for (MapObject obj : map.getMapObjects()) {
            int x = obj.getPositionX() + offset.getX();
            int y = obj.getPositionZ() + offset.getY();
            int height = obj.getHeight(scale, factory) + level;

            for (int z = level; z < height; z++)
            	world.setBlock(new Vector3i(x, z, y), blockFamily.getArchetypeBlock());
            if (obj.isOrigin())
                processMap(obj.getObject().getSubmap(scale, factory), new Vector2i(x+1, y+1), height, world, blockFamily);
        }
    }
    private BlockFamily getBlockFamily(String colorBlock) {
		BlockManager blockManager = CoreRegistry.get(BlockManager.class);
        List<BlockUri> matchingUris = blockManager.resolveAllBlockFamilyUri(colorBlock);
        BlockFamily blockFamily = blockManager.getBlockFamily(matchingUris.get(0));
        return blockFamily;
	}
    public static String getColor(String classpath){   	
	    Integer d=data.get(classpath);
	    if(d==0){ return "Coloring:rojo";}
	    else if(d==1){return "Coloring:verde";}	    		
	    else{ return "Core:stone";}
    
}

class ThreadGithubExecution implements Runnable{
    		
    		private String localPath = "modules/GitHub/tempRepo/";
    		private Repository localRepo;
    		private Git git;
    		    
     
    		    private Console console;
    		    private String remotePath;
    		    private String projectName;
    		    private String metric;
  
    		    public ThreadGithubExecution(String remotePath, String projectName, String metric, Console console){
    		    	this.console=console;
    		    	this.remotePath=remotePath;
    		    	this.projectName=projectName;
    		    	this.metric=metric;
    		    }
    		    
    		    private void buildLocalProjectPath(){
    		    	//TODO: falta verificar que se retorne un path valido
    		    	localPath= localPath.concat(projectName);
    		    }
    		    
    		    @Override
    		    public void run(){
    		    	
    		    try {
    		    	buildLocalProjectPath();
					localRepo=new FileRepository(localPath+"/.git");
					git = new Git(localRepo);
					/*---------clone remote repository---------------------*/
					//preguntar si existe el repositorio en el folder del localpath, si existe hacer pull , si no clone
					 if( RepositoryCache.FileKey.isGitRepository(new File(localPath), FS.DETECTED)){
						 if( hasAtLeastOneReference(localRepo)){
			    	        	gitPull(git);
						 }
					 }else{
			    	        	gitGlone(localPath, remotePath);
			         }
					 GitHubMetric metricObject = null;
					if(metric.equals("bugs")){
						// calcula la metrica bugs
						metricObject = new BugMetric();					
					}else if(metric.equals("versions")){
						//calcula la metrica versions
						metricObject = new VersionMetric();
						
					}else{
						System.err.println("parametro metric invalido");
					}
					try{metricObject.getData(git, localRepo, GitCommand.data);}
					finally{
					git.close();
					console.addMessage("Fin del Analisis.\n");
					}
					 				
				} catch (IOException e) {
					// It can´t create a Local repository
					System.err.println("problema al crear repositorio local");
					e.printStackTrace();
				} catch (WrongRepositoryStateException e) {					
					e.printStackTrace();
				} catch (InvalidConfigurationException e) {					
					e.printStackTrace();
				} catch (DetachedHeadException e) {					
					e.printStackTrace();
				} catch (InvalidRemoteException e) {					
					e.printStackTrace();
				} catch (CanceledException e) {					
					e.printStackTrace();
				} catch (RefNotFoundException e) {					
					e.printStackTrace();
				} catch (RefNotAdvertisedException e) {				
					e.printStackTrace();
				} catch (NoHeadException e) {					
					e.printStackTrace();
				} catch (TransportException e) {					
					e.printStackTrace();
				} catch (GitAPIException e) {					
					e.printStackTrace();
				}
    		    
    			

    	        
    		    }
    private  boolean hasAtLeastOneReference(Repository repo) {

	    for (Ref ref : repo.getAllRefs().values()) {
	        if (ref.getObjectId() == null) {
	        	continue;
	        }
	        return true;
	    }

	    return false;
	}

	private  void gitPull(Git git) throws WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, InvalidRemoteException, CanceledException, RefNotFoundException, RefNotAdvertisedException, NoHeadException, TransportException, GitAPIException {
		// This method execute a pull from remote repository
		git.pull().call();
	}


	private void gitGlone(String localPath, String remotePath)
			throws GitAPIException, InvalidRemoteException, TransportException {
		CloneCommand clone =Git.cloneRepository().setURI(remotePath);        
        clone.setDirectory(new File(localPath)).call();
	}
	

}
}
