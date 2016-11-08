import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;

import com.mysql.jdbc.Statement;

public class LevelController {

	public static String create(String postBody) throws SQLException {
		JSONObject obj = new JSONObject();
		
		String insertQuery = "INSERT INTO markers VALUES (null,?,?,?,?)";
		PreparedStatement stat = DBHelper.getConn().prepareStatement(insertQuery);
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(postBody, params, "UTF-8");
		stat.setString(1, params.getString("lat"));
		stat.setString(2, params.getString("lng"));
		stat.setString(3, "test");
		stat.setString(4, params.getString("Level"));
		if(stat.executeUpdate() == 1) {
			obj.put("success", true);
		} else {
			obj.put("success", false);
		}
		return obj.toString();
	}

	public static String get(String id) throws SQLException {
		String getQuery = "SELECT * FROM markers WHERE id=?";
		JSONObject obj = new JSONObject();
		PreparedStatement stat = DBHelper.getConn().prepareStatement(getQuery);
		stat.setString(1, id);
		ResultSet rs = stat.executeQuery();
		if(rs.next()) {
			obj.put("success", true);
			String level = rs.getString("level");
			obj.put("level", level);
		} else {
			obj.put("success", false);
			obj.put("message", "Username does not exist");
		}
		return obj.toString();
	}
	
}
