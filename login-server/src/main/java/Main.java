import static spark.Spark.*;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;
import org.json.JSONObject;

public class Main {

	static Connection conn = null;
	private static SecureRandom random = new SecureRandom();

	public static void main(String[] args) {
		makeJDBCConnection();
		port(Integer.parseInt(args[0]));

		before((req, resp) -> {
			resp.type("application/json");
		});

		post("/create", (req, resp) -> createHandler(req.body()));
		get("/login/:name/:pass", (req, resp) -> loginHandler(req.params(":name"), req.params(":pass")));

		exception(SQLException.class, (ex, req, resp) -> {
			JSONObject obj = new JSONObject();
			obj.put("success", false);
			obj.put("message", "Exception thrown");
			obj.put("debug", ex.getMessage());
			resp.type("application/json");
			resp.body(obj.toString());
		});
	}

	private static String loginHandler(String user, String pass) throws SQLException {
		String getQuery = "SELECT * FROM users WHERE `name`= ?";
		JSONObject obj = new JSONObject();
		PreparedStatement stat = conn.prepareStatement(getQuery);
		stat.setString(1, user);
		ResultSet rs = stat.executeQuery();
		if(rs.next()) {
			if(DigestUtils.sha1Hex(pass).equals(rs.getString("passhash"))) {
				String session_id = new BigInteger(120, random).toString(32);
				String insertQuery = "INSERT INTO sessions VALUES (?,?,NOW())";
				insertQuery += "ON DUPLICATE KEY UPDATE id=?";
				PreparedStatement stat2 = conn.prepareStatement(insertQuery);
				stat2.setString(1, session_id);
				stat2.setInt(2, rs.getInt("id"));
				stat2.setString(3, session_id);
				if(stat2.executeUpdate() == 1) {
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

	private static String createHandler(String postBody) throws SQLException {
		JSONObject obj = new JSONObject();
		String insertQuery = "INSERT INTO users VALUES (null,?,?,?,NOW())";
		PreparedStatement stat = conn.prepareStatement(insertQuery);
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

	private static void makeJDBCConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Couldn't find JDBC driver.");
			return;
		}

		try {
			conn = DriverManager.getConnection("jdbc:mysql://mysql.cs.iastate.edu/db309gp06", "dbu309gp06",
					"REZJvNLvPS4");
			if(conn != null) {
				System.out.println("Connection Successful.");
			} else {
				System.err.println("Failed to make connection.");
			}
		} catch (SQLException e) {
			System.err.println("MySQL Connection Failed!");
			e.printStackTrace();
			return;
		}

	}
}