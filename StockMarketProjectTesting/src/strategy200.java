import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class strategy200 {
	
	ArrayList <String> Data = new ArrayList<String>();
	static ArrayList<String> dates = new ArrayList<String>();
	float Budget;
	float startBudget;
	float budgetPercent;
	String startDate;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";
	int stocksAmount = 0;
	
	
	void strategy200Ex() {
		System.out.println();
		System.out.println("200-Strategy");
		strategy200GetData();
		strategy200CreateTable();
		strategy200Method();
		budgetChange();
	}

	void strategy200GetData() {
		try {
			FileReader fileReader = new FileReader("data.txt");
			BufferedReader bReader = new BufferedReader(fileReader);
			while(bReader.ready()) {
				Data.add(bReader.readLine());
			}
			Budget = Float.parseFloat(Data.get(0));
			startDate = Data.get(1);
		}
		catch(Exception e) {
			System.out.println("Fehler in der Textdatei");
		}
		startBudget = Budget;
	}
	
	void strategy200CreateTable() {
		try {
			Connection conn = DriverManager.getConnection(DBUrl);
			String sql = "CREATE TABLE if not exists " + Data.get(2) + "200 (date text PRIMARY KEY, ticker text, budget text, stocksAmount text, flag text, amount text, avg text)";
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
			conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	void strategy200Method() {
		LocalDate testDate = LocalDate.parse(startDate);
		String sql, sql2;
		float closeNow = 0;
		float avgNow = 0;
		boolean lastBuy = false;
		boolean flag = false;
		float splitFac = 1;
		boolean lastSplit = false;
		float lastSplitx = 0;
		float tempSplit = 0;
		String sqlBuySell = "INSERT OR REPLACE INTO " + Data.get(2) + "200 (date, ticker, budget, stocksAmount, flag, amount, avg) values (?,?,?,?,?,?,?)";
		
		try {
			Connection conn = DriverManager.getConnection(DBUrl);
			for(int i = 0; i < i+1; i++) {
				sql = "select * from " + Data.get(2) + " where date >= '" + testDate.toString() + "' order by date asc limit 1";
				
				sql2 = "select avg from " + Data.get(2) + "avg where date >= '" + testDate.toString() + "' order by date asc limit 1";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Statement stmt2 = conn.createStatement();
				ResultSet rs2= stmt2.executeQuery(sql2);
				while(rs2.next()) {
					avgNow = Float.parseFloat(rs2.getString("avg"));
				}
				while(rs.next()) {
					closeNow = Float.parseFloat(rs.getString("amount"));
					System.out.println(testDate + " asdsa " + closeNow);
					tempSplit = Float.parseFloat(rs.getString("split"));
				}
				
				if(splitFac!=tempSplit) {
					splitFac = splitFac * tempSplit;
				}
				if(lastBuy) {

					if((closeNow/splitFac) < (avgNow/splitFac)) {
						flag = false;
						Budget = Budget + (stocksAmount*(closeNow/splitFac));
						stocksAmount = 0;
						PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
						pstmt.setString(1, testDate.toString());
						pstmt.setString(2, Data.get(2));
						pstmt.setString(3, String.valueOf(Budget));
						pstmt.setString(4, String.valueOf(stocksAmount));
						pstmt.setString(5, "SELL");
						pstmt.setString(6, String.valueOf(closeNow));
						pstmt.setString(7, String.valueOf(avgNow));
						pstmt.executeUpdate();
						lastSplit = true;
					}
				}
				if(!lastBuy) {
					
					if((closeNow/splitFac) > (avgNow/splitFac)) {
						flag = true;
						stocksAmount = stocksAmount + (int) (Budget/closeNow);
						Budget = Budget - (stocksAmount*(closeNow/splitFac));
						if(lastSplitx!=splitFac) {
							stocksAmount = (int) (stocksAmount * splitFac);
							lastSplitx = splitFac;

						}
						PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
						pstmt.setString(1, testDate.toString());
						pstmt.setString(2, Data.get(2));
						pstmt.setString(3, String.valueOf(Budget));
						pstmt.setString(4, String.valueOf(stocksAmount));
						pstmt.setString(5, "BUY");
						pstmt.setString(6, String.valueOf(closeNow));
						pstmt.setString(7, String.valueOf(avgNow));
						pstmt.executeUpdate();
						lastSplit = true;
						}
				}
				
				if(flag) {
					lastBuy = true;
				}
				else {
					lastBuy = false;
				}
				
				if(testDate.equals(LocalDate.now())) {
					if(lastSplit) {
						Budget = Budget + (stocksAmount*closeNow);
						stocksAmount = 0;
					}
					else {
						Budget = Budget + ((stocksAmount*splitFac)*(closeNow/splitFac));
						stocksAmount = 0;
					}
					PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
					pstmt.setString(1, testDate.toString());
					pstmt.setString(2, Data.get(2));
					pstmt.setString(3, String.valueOf(Budget));
					pstmt.setString(4, String.valueOf(stocksAmount));
					pstmt.setString(5, "SELL");
					pstmt.executeUpdate();
					break;
				}
				testDate = testDate.plusDays(1);
			}
	
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	void budgetChange() {
		System.out.println("Startbudget: " + startBudget);
		System.out.println("Endbudget: " + Budget);
		float temp;
		temp = startBudget - Budget;
		if(temp < 0) {
			budgetPercent = Math.abs(temp/startBudget);
			System.out.println("Budgetveränderung: " + budgetPercent * 100 + "%");
		}
		else {
			budgetPercent = temp/startBudget;
			System.out.println("Budgetveränderung: -" + budgetPercent * 100 + "%");
		}
		
	}
	
}
