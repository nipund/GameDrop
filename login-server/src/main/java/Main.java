import static spark.Spark.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.jetty.util.MultiMap;
import org.eclipse.jetty.util.UrlEncoded;

public class Main {

	static Connection conn = null;

	public static void main(String[] args) {
		makeJDBCConnection();
		post("/create", (req, resp) -> createHandler());
	}

	private static String createHandler() {
		try {
			String insertQuery = "INSERT INTO users VALUES (null,?,?,?,NOW())";
			PreparedStatement stat = conn.prepareStatement(insertQuery);
			MultiMap<String> params = new MultiMap<String>();
			UrlEncoded.decodeTo(req.body(), params, "UTF-8");
			stat.setString(1, params.getString("name"));
			stat.setString(2, DigestUtils.sha1Hex(params.getString("pass")));
			stat.setString(3, params.getString("email"));
			if(stat.executeUpdate() == 1) {
				return "OK";
			} else {
				return "Error";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception";
		}
	}

	private static void makeJDBCConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.err.println("Couldn't found JDBC driver.");
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