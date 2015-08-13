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
import org.terasology.registry.In;


/**
 * @author mperalta92, kidonkey
 */
@RegisterSystem
public class GitCommand extends BaseComponentSystem{
	public static Hashtable<String, Boolean> data;
	public static Hashtable<String, Integer> data2;
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
    		data=new Hashtable<String,Boolean>();
    		data2=new Hashtable<String,Integer>();
    		Thread t = new Thread(new ThreadGithubExecution(remotePath,projectName,metrica, console));
    		t.start();
    		
    		return "Esperando por resultados del analisis... ";
    }
    
    public void apply(){
    	//Hacer coloreo
    }
    public static String getColor(String classpath){
    	if(metrica.equals("bugs")){
	    	Boolean d=data.get(classpath);
	    	if(d==null){return "Core:stone";}
	    	
	    	else if(d==true){return "Coloring:rojo";}
	    	else { return "Coloring:verde";}
	    }else{
	    	Integer d=data2.get(classpath);
	    	if(d==0){ return "Coloring:rojo";}
	    	else if(d==1){return "Coloring:verde";}	    		
	    	else{ return "Core:stone";}
	    	
	    }
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
					 
					if(metric.equals("bugs")){
						// calcula la metrica bugs
						BugMetric.getBugs(git, localRepo, GitCommand.data);
					
					}else if(metric.equals("versions")){
						//calcula la metrica versions
						VersionMetric.getVersions(git, localRepo, GitCommand.data2);
						
					}else{
						System.err.println("parametro metric invalido");
					}
					 
					git.close();
					console.addMessage("Fin del Analisis.\n");
					 				
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
