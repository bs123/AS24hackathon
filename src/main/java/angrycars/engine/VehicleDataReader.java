package angrycars.engine;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VehicleDataReader {
    private final JSONArray json;

	public VehicleDataReader(String jsonData) throws JSONException {
    	this.json = new JSONArray(jsonData);
    }
	
	public List<Entry> parse() throws JSONException {
		int len = json.length();
		List<Entry> result = new ArrayList<Entry>();
		for (int i = 0; i < len; i++) {
			JSONObject obj = json.getJSONObject(i);
		}
		return result;
	}
    
	public static class Entry {
		
	}
	
	private static final String PATH = "C:/Users/gintas/Documents/AS24hackathon/inputs/car1.json";
	
	public static void main(String[] args) throws Exception {
		String data = new String(Files.readAllBytes(Paths.get(PATH)));
		VehicleDataReader r = new VehicleDataReader(data);
		List<Entry> result = r.parse();
		System.err.println(result.size());
//		FileInputStream jsonFile = new FileInputStream();
//	    new BufferedInputStream(jsonFile).r
//		jsonFile.
    }
}