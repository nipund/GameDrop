import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {
	private static volatile DBHelper instance = new DBHelper();
	private Connection conn;
	private long lastConnectionTime;

	private DBHelper() {
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
				lastConnectionTime = System.currentTimeMillis();
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

	public static DBHelper getInstance() {
		if(System.currentTimeMillis() - instance.lastConnectionTime > 3600000) {
			try {
				instance.conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			instance = new DBHelper();
			System.out.println("Reconnected to MySQL");
		}
		return instance;
	}
	
	public static Connection getConn() {
		return getInstance().conn;
	}
}