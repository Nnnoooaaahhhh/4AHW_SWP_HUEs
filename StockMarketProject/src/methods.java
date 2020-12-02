import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.json.*;

public class methods {
	
	static String symbol;
	static String DBurl;
	static ArrayList<String> dates = new ArrayList<String>();
	static JSONObject b;

	void connect(String abbr) throws JSONException, MalformedURLException, IOException {
		String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
				+ abbr + "&apikey=U6CC2C8C3M2IVA5G"), Charset.forName("UTF-8"));
		JSONObject ob = new JSONObject(link);
		b = ob.getJSONObject("Time Series (Daily)");
		for (int i = 0; i < 100; i++) {
			dates.add(b.names().get(i).toString());
		}
		dates.sort(null);
		for(int i = 0; i < dates.size(); i++) {
		}
	}

	String decide() {
		Scanner a = new Scanner(System.in);
		System.out.println("Abkürzung eingeben: ");
		symbol= a.next();
		a.close();
		return symbol;
	}



	void database() throws SQLException {
		DBurl = "jdbc:sqlite:infos.db";
		String sql;
		Connection conn = DriverManager.getConnection(DBurl);
		sql = "CREATE TABLE if not exists " + symbol +" (date text PRIMARY KEY, amount text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "INSERT OR REPLACE INTO " + symbol + " (date, amount) values (?,?)";
		for(int i = 0; i < dates.size(); i++) {
			try {	
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dates.get(i));
				pstmt.setString(2, b.getJSONObject(dates.get(i)).get("4. close").toString());
				pstmt.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		sql = "SELECT * from " + symbol;
		try {
			Statement stmt  = conn.createStatement();  
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()) {
            	System.out.print(rs.getString("date") + ": ");
            	System.out.println(rs.getString("amount"));
            }
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}	
	}
}
