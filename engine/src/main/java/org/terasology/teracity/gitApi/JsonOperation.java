package org.terasology.teracity.gitApi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonOperation {
	
	private String path;
	private JsonReader reader;
	private Hashtable<String, Integer> metricsTable; 
	
	public JsonOperation (String path){
		this.path = path;
		this.reader = new JsonReader();
		this.metricsTable = new Hashtable<String, Integer>();
	}
	
	public ArrayList<String> getCommit(String date, String branch){		
    	JSONArray array = reader.getJsonArray(path+ "/commits?since=" + date + "&sha=" + branch);
    	ArrayList<String> shaList = reader.getJsonList(array, "sha");
		return shaList;
	}

	public void getFiles(String sha) {
		String url = path + "/commits/" + sha;
		JSONObject json = reader.getJsonObject(url);
		JSONArray filesArray = (JSONArray) json.get("files");
		ArrayList<String> filesNames =reader.getJsonList(filesArray, "filename");
		saveCommits(filesNames);
		//System.out.println(metricsTable.toString());		
	}
	
	public void saveCommits (ArrayList<String> keys){
		for (String str:keys){
			try{
				metricsTable.put(str, metricsTable.get(str)+1);
			}
			catch(Exception e){
				metricsTable.put(str, 1);
			}
		}
	}
	
	public Hashtable<String, Integer> getTable(){
		return metricsTable;
	}
}
