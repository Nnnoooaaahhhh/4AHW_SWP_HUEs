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
	static ArrayList<Float> values = new ArrayList<Float>();
	static JSONObject b;
	String firstDate;
	static String key;

	void connect(String abbr) throws JSONException, MalformedURLException, IOException {
		String link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="
				+ abbr + "&outputsize=full&apikey="+key), Charset.forName("UTF-8"));
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
		System.out.println("API-KEY: ");
		key = a.next();
		a.close();
		return symbol;
	}

	void database() throws SQLException {
		String sql;
		Connection conn = DriverManager.getConnection(DBurl);
		sql = "CREATE TABLE if not exists " + symbol + " (date text PRIMARY KEY, amount text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "CREATE TABLE if not exists " + symbol+"avg" + " (date text PRIMARY KEY, avg text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "CREATE TABLE if not exists " + symbol+"avgAdj" + " (date text PRIMARY KEY, avg text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "CREATE TABLE if not exists " + symbol+"Adj" + " (date text PRIMARY KEY, amount text)";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		sql = "INSERT OR REPLACE INTO " + symbol + " (date, amount) values (?,?)";
		for (int i = 0; i < dates.size(); i++) {
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dates.get(i));
				pstmt.setString(2, b.getJSONObject(dates.get(i)).get("4. close").toString());
				pstmt.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
	}
		sql = "INSERT OR REPLACE INTO " + symbol +"Adj" + " (date, amount) values (?,?)";
		for (int i = 0; i < dates.size(); i++) {
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, dates.get(i));
				pstmt.setString(2, b.getJSONObject(dates.get(i)).get("5. adjusted close").toString());
				pstmt.executeUpdate();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			
	}
		
		dates.clear();
		sql = "select date from " +symbol;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				dates.add(rs.getString("date"));
				
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		String sql2;
		String test;
		 sql = "INSERT OR REPLACE INTO " + symbol+"avg" + " (date, avg) values (?,?)";
		  for (int i = 0; i < dates.size(); i++) {
			  sql2 = "select avg(amount) as temp from (select amount from "+symbol+" where date <= '"+ dates.get(i)+ "' order by date desc limit 200)";
				Statement stmt = conn.createStatement();
				ResultSet rs2 = stmt.executeQuery(sql2);
				rs2.next();
				try {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, dates.get(i));
					test = rs2.getString("temp");
					pstmt.setString(2, test);
					pstmt.executeUpdate();
				}
				catch(Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
			}
			 sql = "INSERT OR REPLACE INTO " + symbol+"avgAdj" + " (date, avg) values (?,?)";
			  for (int i = 0; i < dates.size(); i++) {
				  sql2 = "select avg(amount) as temp from (select amount from "+symbol+ "Adj"+" where date <= '"+ dates.get(i)+ "' order by date desc limit 200)";
					Statement stmt = conn.createStatement();
					ResultSet rs2 = stmt.executeQuery(sql2);
					rs2.next();
					try {
						PreparedStatement pstmt = conn.prepareStatement(sql);
						pstmt.setString(1, dates.get(i));
						test = rs2.getString("temp");
						pstmt.setString(2, test);
						pstmt.executeUpdate();
					}
					catch(Exception e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}
				}
		}
}
