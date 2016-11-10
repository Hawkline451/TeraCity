import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;

import Json.JsonReader;



public class Test {
	 
	public static void main(String[] args) {
		 
		JsonReader reader = new JsonReader();
		JSONArray array = reader.getJson("https://api.github.com/repos/Hawkline451/TeraCity/commits");
		reader.getJsonObject(array, "sha");
		//reader.getJson("https://api.github.com/repos/Hawkline451/TeraCity/issues");
		
		Path path = Paths.get("modules/jeditint/src/main/java/org/terasology/rendering/nui/layers/ingame/EditClassScreen.java");
		System.out.println(path.getFileName());
		
		
	}
}