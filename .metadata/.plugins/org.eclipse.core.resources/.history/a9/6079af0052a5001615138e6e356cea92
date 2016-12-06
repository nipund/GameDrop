import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;

import com.mysql.jdbc.Statement;

public class LevelController {

	public static String create(String postBody) throws SQLException {
		JSONObject obj = new JSONObject();
		
		String insertQuery = "INSERT INTO markers VALUES (null,?,?,?)";
		PreparedStatement stat = DBHelper.getConn().prepareStatement(insertQuery);
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(postBody, params, "UTF-8");
		stat.setString(1, params.getString("lat"));
		stat.setString(2, params.getString("lng"));
		stat.setString(3, "test");
		stat.executeUpdate();
		ResultSet rs = stat.getGeneratedKeys();
		int id = rs.getInt(1);
		
		insertQuery = "INSERT INTO levels VALUES (null,?,?)";
		stat = DBHelper.getConn().prepareStatement(insertQuery);
		stat.setString(1, params.getString("Level"));
		stat.setInt(2, id);
		if(stat.executeUpdate() == 1) {
			obj.put("success", true);
		} else {
			obj.put("success", false);
		}
		return obj.toString();
	}
	
}
