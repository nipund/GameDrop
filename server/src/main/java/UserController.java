import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;

public class UserController {
	
	private static SecureRandom random = new SecureRandom();
	
	static String login(String user, String pass) throws SQLException {
		String getQuery = "SELECT * FROM users WHERE name= ?";
		JSONObject obj = new JSONObject();
		PreparedStatement stat = DBHelper.getConn().prepareStatement(getQuery);
		stat.setString(1, user);
		ResultSet rs = stat.executeQuery();
		if(rs.next()) {
			if(DigestUtils.sha1Hex(pass).equals(rs.getString("passhash"))) {
				String session_id = new BigInteger(120, random).toString(32);
				String insertQuery = "INSERT INTO sessions VALUES (?,?,NOW())";
				insertQuery += "ON DUPLICATE KEY UPDATE id=?";
				PreparedStatement stat2 = DBHelper.getConn().prepareStatement(insertQuery);
				stat2.setString(1, session_id);
				stat2.setInt(2, rs.getInt("id"));
				stat2.setString(3, session_id);
				if(stat2.executeUpdate() >= 1) {
					obj.put("success", true);
					obj.put("session_id", session_id);
				} else {
					obj.put("success", false);
				}
			} else {
				obj.put("success", false);
				obj.put("message", "Incorrect password");
			}
		} else {
			obj.put("success", false);
			obj.put("message", "Username does not exist");
		}
		return obj.toString();
	}

	static String create(String postBody) throws SQLException {
		JSONObject obj = new JSONObject();
		String insertQuery = "INSERT INTO users VALUES (null,?,?,?,NOW())";
		PreparedStatement stat = DBHelper.getConn().prepareStatement(insertQuery);
		MultiMap<String> params = new MultiMap<String>();
		UrlEncoded.decodeTo(postBody, params, "UTF-8");
		stat.setString(1, params.getString("name"));
		stat.setString(2, DigestUtils.sha1Hex(params.getString("pass")));
		stat.setString(3, params.getString("email"));
		if(stat.executeUpdate() == 1) {
			obj.put("success", true);
		} else {
			obj.put("success", false);
		}
		return obj.toString();
	}

}
