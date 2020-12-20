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
	static String DBurl = "jdbc:sqlite:infos.db";;
	static ArrayList<String> dates = new ArrayList<String>();
	static JSONObject b;

	void connect(String abbr) throws JSONException, MalformedURLException, IOException {
		String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="
				+ abbr + "&outputsize=full&apikey=U6CC2C8C3M2IVA5G"), Charset.forName("UTF-8"));
		JSONObject ob = new JSONObject(link);
		b = ob.getJSONObject("Time Series (Daily)");
		for (int i = 0; i < b.names().length(); i++) {
			if (!dates.contains(b.names().get(i))) {
				dates.add(b.names().get(i).toString());
			}
		}
		dates.sort(null);
	}

	String decide() {
		Scanner a = new Scanner(System.in);
		System.out.println("Abbreviation (e.g.: AAPL, TSLA, IBM, ...): ");
		symbol = a.next();
		a.close();
		return symbol;
	}

	void database() throws SQLException {
		String sql;
		float sum = 0;
		float avg = 0;
		int count = 1;
		Connection conn = DriverManager.getConnection(DBurl);
		sql = "CREATE TABLE if not exists " + symbol + " (date text PRIMARY KEY, amount text, avg text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "REPLACE INTO " + symbol + " (date, amount, avg) values (?,?,?)";
		for (int i = 0; i < dates.size(); i++) {
			try {
				if (count == 201) {
					sum = sum - Float.parseFloat(b.getJSONObject(dates.get(i - 200)).get("4. close").toString())
							+ Float.parseFloat(b.getJSONObject(dates.get(i)).get("4. close").toString());
					avg = sum / 200;
				}
				if (count <= 200) {
					sum = sum + Float.parseFloat(b.getJSONObject(dates.get(i)).get("4. close").toString());
					avg = sum / count;
					count++;
				}
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dates.get(i));
				pstmt.setString(2, b.getJSONObject(dates.get(i)).get("4. close").toString());
				pstmt.setString(3, String.valueOf(avg));
				pstmt.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
