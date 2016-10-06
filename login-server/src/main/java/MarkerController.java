import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class MarkerController {
	
	static String within(float lat, float lng, float dist) throws SQLException {
		String getQuery = "SELECT *,"
						+ "SQRT(POW(69.1 * (lat - ?), 2)"
						+ "+POW(69.1 * (? - lng)"
						+ "* COS(lat / 57.3), 2)) AS distance "
						+ "FROM markers HAVING distance < ? ORDER BY distance;";
		PreparedStatement stat = DBHelper.getConn().prepareStatement(getQuery);
		JSONObject obj = new JSONObject();
		JSONArray ary = new JSONArray();
		stat.setFloat(1, lat); //42.051571
		stat.setFloat(2, lng); //-93.624983
		stat.setFloat(3, dist);
		ResultSet rs = stat.executeQuery();
		obj.put("success", true);
		while(rs.next()) {
			JSONObject innerObj = new JSONObject();
			innerObj.put("name", rs.getString("name"));
			innerObj.put("lat", rs.getFloat("lat"));
			innerObj.put("lng", rs.getFloat("lng"));
			innerObj.put("distance", rs.getFloat("distance"));
			ary.put(innerObj);
		}
		obj.put("markers", ary);
		return obj.toString();
	}
	
	static String within(String lat, String lng, String dist) throws SQLException {
		return within(Float.parseFloat(lat), Float.parseFloat(lng), Float.parseFloat(dist));
	}
	
}
