import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class buyAndHold {
	
	ArrayList <String> Data = new ArrayList<String>();
	static ArrayList<String> dates = new ArrayList<String>();
	float Budget;
	String startDate;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";
	float firstAmount;
	String firstDate;
	float lastAmount;
	String lastDate;
	int stocksAmount;
	float splitFac = 1;
	float startBudget;
	float budgetPercent;
	static Connection conn = null;
	
	void buyAndHoldEx(String stock) {
		//System.out.println("Buy-And-Hold");
		testMain t = new testMain();
		Budget = Float.parseFloat(t.Data.get(0));
		Budget = Budget/(t.Data.size()-2);
		startDate = t.Data.get(1);
		startBudget = Budget;
		try {
			openConnection();
			buyNHoldTableDrop(stock);
			buyNHoldCreateTable(stock);
			buyAndHoldDatabase(stock);
			buyAndHoldMethod(stock);
			//budgetChange();
			closeConnection();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	void buyNHoldTableDrop(String stock) {
		String sql = "DROP TABLE if exists " + stock + "buyNHold";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
		catch(Exception e) {
			
		}
	}
	
	void buyNHoldCreateTable(String stock) {
		try {
			String sql = "CREATE TABLE if not exists " + stock + "buyNHold (date text PRIMARY KEY, ticker text, budget text, stocksAmount text, flag text, amount text)";
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

		void buyAndHoldGetData() {
			try {
				FileReader fileReader = new FileReader("data.txt");
				BufferedReader bReader = new BufferedReader(fileReader);
				while(bReader.ready()) {
					Data.add(bReader.readLine());
				}
				
				bReader.close();
				Budget = Float.parseFloat(Data.get(0));
				startDate = Data.get(1);
			}
			catch(Exception e) {
				System.out.println("Fehler in der Textdatei");
			}
			startBudget = Budget;
		}
		
		void buyAndHoldDatabase(String stock) {
			try {
				String sql = "select amount, date from " + stock + " where date >= '" + startDate + "' order by date asc limit 1";
				String sql2 = "select amount, date from " + stock + " order by date desc limit 1";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
				while(rs.next()) {
					firstAmount = Float.parseFloat(rs.getString("amount"));
					firstDate = rs.getString("date");
				}
				while(rs2.next()) {
					lastAmount = Float.parseFloat(rs2.getString("amount"));
					lastDate = rs2.getString("date");
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		void buyAndHoldMethod(String stock) {
			testMain t = new testMain();
			String sql = "INSERT OR REPLACE INTO " + stock + "buyNHold (date, ticker, budget, stocksAmount, flag, amount) values (?,?,?,?,?,?)";
			try {
				PreparedStatement pstmt = conn.prepareStatement(sql);
				stocksAmount = (int) (Budget/firstAmount);
				Budget = Budget - (stocksAmount*firstAmount);
				pstmt.setString(1, t.Data.get(1));
				pstmt.setString(2, stock);
				pstmt.setString(3, String.valueOf(Budget));
				pstmt.setString(4, String.valueOf(stocksAmount));
				pstmt.setString(5, "BUY");
				pstmt.setString(6, String.valueOf(firstAmount));
				pstmt.executeUpdate();
				splitCor(stock);
				Budget = Budget + (stocksAmount*lastAmount);
				pstmt.setString(1, LocalDate.now().toString());
				pstmt.setString(2, stock);
				pstmt.setString(3, String.valueOf(Budget));
				pstmt.setString(4, String.valueOf(stocksAmount));
				pstmt.setString(5, "SELL");
				pstmt.setString(6, String.valueOf(lastAmount));
				pstmt.executeUpdate();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		void splitCor(String stock) {
			try {
				Connection conn = DriverManager.getConnection(DBUrl);
				String sql = "select split from " + stock + " where date >= '" + startDate + "' order by date asc";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while(rs.next()) {
					splitFac = splitFac * Float.parseFloat(rs.getString("split"));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			stocksAmount = (int) (stocksAmount * splitFac);
		}
		
		void budgetChange() {
			System.out.println("Startbudget: " + startBudget);
			System.out.println("Endbudget: " + Budget);
			float temp;
			temp = Budget - startBudget;
			if(temp > 0) {
				budgetPercent = Math.abs(temp/startBudget) + 1;
				System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
			}
			else {
				budgetPercent = Budget/startBudget;
				System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
			}
			
		}
		
		void openConnection() {
			
			try {
				conn = DriverManager.getConnection(DBUrl);
			}
			catch(Exception e) {
				e.printStackTrace();
			}	
		}
		
		void closeConnection() {
			try {
				conn.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
}
