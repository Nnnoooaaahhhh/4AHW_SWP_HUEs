import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.*;


public class methods {

	static String symbol;
	static String DBurl = "jdbc:sqlite:infos.db";
	static ArrayList<String> dates = new ArrayList<String>();
	static ArrayList<Float> values = new ArrayList<Float>();
	static JSONObject b;
	static ArrayList<String> ticks = new ArrayList<String>();
	static String link;
	static String startDate;
	static String endDate;
	static String linkSize;
	String firstDate;
	static String key;
	static Connection conn;

	void connect(String link) throws JSONException, MalformedURLException, IOException {
		JSONObject ob = new JSONObject(link);
		b = ob.getJSONObject("Time Series (Daily)");
		for (int i = 0; i < b.names().length(); i++) {
			if (!dates.contains(b.names().get(i))) {
				dates.add(b.names().get(i).toString());
			}
		}
		dates.sort(null);
	}
	

	void decide() throws IOException {
		Scanner a = new Scanner(System.in);
		FileReader Freader = new FileReader("ticks.txt");
		BufferedReader BReader = new BufferedReader(Freader);
		while(BReader.ready()) {
			ticks.add(BReader.readLine());
		}
		key = ticks.get(0);
		startDate = ticks.get(1);
		endDate = ticks.get(2);
		linkSize = ticks.get(3);
		BReader.close();
		a.close();
	}
	 

	void database() throws SQLException {
		String sql;
		Connection conn = DriverManager.getConnection(DBurl);

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
		sql = "select date from " +symbol+"Adj";
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
			  conn.close();
		}
}
