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
			// TODO: parse timestamp properly
			String timestamp = obj.getString("recorded_at");
			double lat = obj.getDouble("latitude");
			double longitude = obj.getDouble("longitude");
			result.add(new Entry(lat, longitude));
		}
		Collections.reverse(result);
		return result;
	}
	
	public static Rectangle findBounds(Iterable<Entry> entries) { 
		Rectangle r = new Rectangle();
		r.minX = Double.MAX_VALUE;
		r.minY = Double.MAX_VALUE;
		r.maxX = Double.MIN_VALUE;
		r.maxY = Double.MIN_VALUE;
		
		for (Entry entry : entries) {
			double y = entry.latitude;
			double x = entry.longitude;
			if (y > r.maxY) r.maxY = y;
			if (x > r.maxX) r.maxX = x;
			if (y < r.minY) r.minY = y;
			if (x < r.minX) r.minX = x;
		}
		return r;
	}
	
	public static List<GridPoint> normalize(Iterable<Entry> entries, Rectangle bounds,
			int ny, int nx) {
		double yRange = bounds.maxY - bounds.minY;
		double xRange = bounds.maxX - bounds.minX;
		
		double yCellSize = yRange / ny;
		double xCellSize = xRange / nx;
		
		if (yCellSize > xCellSize) {
			xCellSize = yCellSize;
		} else {
			yCellSize = xCellSize;
		}
	
		List<GridPoint> result = new ArrayList<GridPoint>();
		for (Entry entry : entries) {
			GridPoint p = new GridPoint();
			double y = (entry.latitude - bounds.minY) / yCellSize;
			p.y =(int) Math.round(y);
			double x = (entry.longitude - bounds.minX) / xCellSize;
			p.x = (int) Math.round(x);
			result.add(p);
		}
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
		List<Entry> entries = r.parse();
		Rectangle bounds = r.findBounds(entries);
		System.err.println(bounds.minX + " " + bounds.minY + " " + bounds.maxX + " " + bounds.maxY);
		List<GridPoint> normalizedEntries = normalize(entries, bounds, 50, 50);
		for (int i = 0; i < normalizedEntries.size(); i++) {
			System.err.println(normalizedEntries.get(i).y + " " + normalizedEntries.get(i).x);
		}
    }
}
