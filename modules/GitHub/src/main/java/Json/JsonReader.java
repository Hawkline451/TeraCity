package Json;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {
	 
	public JSONArray getJson(String url) {
 
		String jsonString = callURL(url);
		//System.out.println("\n\njsonString: " + jsonString);
		
		try {  
			JSONArray jsonArray = new JSONArray(jsonString);
			//System.out.println("\n\njsonArray: " + jsonArray);
			return jsonArray;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;	
		}
			
	}
	
	public void getJsonObject(JSONArray jsonArray, String key){
		
		for(int i = 0; i < jsonArray.length(); i++){
			JSONObject objects = jsonArray.getJSONObject(i);
			Object sha = objects.get(key);
			/*for (Iterator<String> it = jsonArray.getJSONObject(i).keys();  it.hasNext(); ){
				String asd = it.next();
				System.out.println("\n" + asd );
			}*/				
			System.out.println("\n\njsonArray[" + i +"]: " + objects.toString()+"\n" + sha);
		}
	}
 
	public static String callURL(String myURL) {
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
 