package angrycars.engine;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
			if (obj.isNull("recorded_at") || obj.isNull("latitude") || obj.isNull("longitude")) {
				continue;
			}
			String timestamp = obj.getString("recorded_at");
			double lat = obj.getDouble("latitude");
			double longitude = obj.getDouble("longitude");
			result.add(new Entry(lat, longitude));
		}
		Collections.reverse(result);
		return result;
	}
    
	public static class Entry {

		public final double latitude;
		public final double longitude;

		public Entry(double lat, double longitude) {
			this.latitude = lat;
			this.longitude = longitude;
		}
	}
	
	private static final String PATH = "C:/Users/gintas/Documents/AS24hackathon/inputs/car1.json";
	
	public static void main(String[] args) throws Exception {
		String data = new String(Files.readAllBytes(Paths.get(PATH)));
		VehicleDataReader r = new VehicleDataReader(data);
		List<Entry> result = r.parse();
		System.err.println(result.size());
		System.err.println(result.get(0).latitude);
		System.err.println(result.get(0).longitude);
    }
}
