package org.terasology.teracity.gitApi;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {
	
	public JSONArray getJsonArray(String url) {
 
		String jsonString = callURL(url);
		//System.out.println("\n\njsonString: " + jsonString);		
		try {  
			JSONArray jsonArray = new JSONArray(jsonString);
			//System.out.println("\n\njsonArray: " + jsonArray);
			return jsonArray;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;	
		}			
	}
	
	public JSONObject getJsonObject(String url) {
		String jsonString = callURL(url);
		//System.out.println("\n\njsonString: " + jsonString);		
		try {  
			JSONObject jsonObject = new JSONObject(jsonString);
			//System.out.println("\n\njsonArray: " + jsonArray);
			return jsonObject;
		}
		catch (JSONException e) {
			e.printStackTrace();
			return null;	
		}	
	}
	
	public ArrayList<String> getJsonList(JSONArray jsonArray, String key){
		ArrayList<String> arrayOut = new ArrayList<String>();
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject objects = jsonArray.getJSONObject(i);
			Object value = objects.get(key);
			/*for (Iterator<String> it = jsonArray.getJSONObject(i).keys();  it.hasNext(); ){
				String asd = it.next();
				System.out.println("\n" + asd );
			}*/			
			//arrayOut.add("\n\njsonArray[" + i +"]: " + objects.toString()+"\n" + value);
			arrayOut.add(value.toString());
		}
		return arrayOut;
	}
 
	public String callURL(String myURL) {
		System.out.println("Requested URL:" + myURL);
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),
						Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		}  
		return sb.toString();
	} 
}
 