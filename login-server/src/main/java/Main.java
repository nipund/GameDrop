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

	public static void main(String[] args) {
		port(Integer.parseInt(args[0]));

		before((req, resp) -> {
			resp.type("application/json");
		});

		post("/create", (req, resp) -> UserController.create(req.body()));
		get("/login/:name/:pass", (req, resp) -> UserController.login(req.params(":name"), req.params(":pass")));

		exception(SQLException.class, (ex, req, resp) -> {
			JSONObject obj = new JSONObject();
			obj.put("success", false);
			obj.put("message", "Exception thrown");
			obj.put("debug", ex.getMessage());
			resp.type("application/json");
			resp.body(obj.toString());
		});
	}
}