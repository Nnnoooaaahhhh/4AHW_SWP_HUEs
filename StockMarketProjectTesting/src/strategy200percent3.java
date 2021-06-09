import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class strategy200percent3 {

	ArrayList <String> Data = new ArrayList<String>();
	static ArrayList<String> dates = new ArrayList<String>();
	float Budget;
	String startDate;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";
	int stocksAmount = 0;
	float startBudget;
	float budgetPercent;
	static Connection conn = null;
	
	
	void strategy200percent3Ex(String stock) {
		testMain t = new testMain();
		Budget = Float.parseFloat(t.Data.get(0));
		Budget = Budget/(t.Data.size()-2);
		startDate = t.Data.get(1);
		startBudget = Budget;
	//	System.out.println("200-Strategy 3%-Strategy with " + stock);
		openConnection();
		strategy200percent3TableDrop(stock);
		getDates(stock);
		strategy200percent3CreateTable(stock);
		writeDummy(stock);
		strategy200Methodpercent3(stock);
		//budgetChange();
		closeConnection();
	}

	void strategy200percent3GetData() {
		try {
			FileReader fileReader = new FileReader("data.txt");
			BufferedReader bReader = new BufferedReader(fileReader);
			while(bReader.ready()) {
				Data.add(bReader.readLine());
			}
			Budget = Float.parseFloat(Data.get(0));
			startDate = Data.get(1);
			bReader.close();
		}
		catch(Exception e) {
			System.out.println("Fehler in der Textdatei");
		}
		startBudget = Budget;
	}
	
	void strategy200percent3TableDrop(String stock) {
		String sql = "DROP TABLE if exists " + stock + "200percent3";
		try {
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
		catch(Exception e) {
			
		}
	}
	
	void strategy200percent3CreateTable(String stock) {
		try {
			String sql = "CREATE TABLE if not exists " + stock + "200percent3 (date text PRIMARY KEY, ticker text, budget text, stocksAmount text, flag text, amount text, avg text)";
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}
	
	void strategy200Methodpercent3(String stock) {
		String sql, sql2;
		float closeNow = 0;
		float avgNow = 0;
		boolean lastBuy = false;
		boolean flag = false;
		float splitFac = 1;
		String sqlBuySell = "INSERT OR REPLACE INTO " + stock + "200percent3 (date, ticker, budget, stocksAmount, flag, amount, avg) values (?,?,?,?,?,?,?)";
		
		try {
			for(int i = 0; i < dates.size(); i++) {
				sql = "select * from " + stock + " where date = '" + dates.get(i) + "' order by date asc limit 1";
				
				sql2 = "select avg from " + stock + "avg where date = '" + dates.get(i) + "' order by date asc limit 1";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Statement stmt2 = conn.createStatement();
				ResultSet rs2= stmt2.executeQuery(sql2);
				while(rs2.next()) {
					avgNow = Float.parseFloat(rs2.getString("avg"));
				}
				while(rs.next()) {
					closeNow = Float.parseFloat(rs.getString("amount"));
					splitFac = Float.parseFloat(rs.getString("split"));
				}
				
				if(splitFac!=1) {
					stocksAmount = (int) (stocksAmount * splitFac);
					splitFac = 1;
				}
				if(lastBuy) {

					if((closeNow*0.97 ) < (avgNow)) {
						flag = false;
						Budget = Budget + (stocksAmount*(closeNow));
						stocksAmount = 0;
						PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
						pstmt.setString(1, dates.get(i));
						pstmt.setString(2, stock);
						pstmt.setString(3, String.valueOf(Budget));
						pstmt.setString(4, String.valueOf(stocksAmount));
						pstmt.setString(5, "SELL");
						pstmt.setString(6, String.valueOf(closeNow ));
						pstmt.setString(7, String.valueOf(avgNow ));
						pstmt.executeUpdate();
					}
				}
	
				if(!lastBuy) {
					
					if((closeNow*1.03) > (avgNow )) {
						flag = true;
						stocksAmount = stocksAmount + (int) (Budget/(closeNow ));
						Budget = Budget - (stocksAmount*(closeNow ));
						PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
						pstmt.setString(1, dates.get(i));
						pstmt.setString(2, stock);
						pstmt.setString(3, String.valueOf(Budget));
						pstmt.setString(4, String.valueOf(stocksAmount));
						pstmt.setString(5, "BUY");
						pstmt.setString(6, String.valueOf(closeNow ));
						pstmt.setString(7, String.valueOf(avgNow ));
						pstmt.executeUpdate();
						}
				}
				
				if(flag) {
					lastBuy = true;
				}
				else {
					lastBuy = false;
				}
				
			}
			
			if(stocksAmount != 0) {
				Budget = Budget + (stocksAmount*(closeNow));
				stocksAmount = 0;
				PreparedStatement pstmt = conn.prepareStatement(sqlBuySell);
				pstmt.setString(1, dates.get(dates.size()-1));
				pstmt.setString(2, stock);
				pstmt.setString(3, String.valueOf(Budget));
				pstmt.setString(4, String.valueOf(stocksAmount));
				pstmt.setString(5, "SELL");
				pstmt.executeUpdate();
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
	
	
	void getDates(String stock) {
		try {

			String sql = "select date from " + stock + " where date >= '" + startDate + "' order by date asc";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				dates.add(rs.getString("date"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
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
	
	void writeDummy(String stock) {
		String sql = "INSERT OR REPLACE INTO " + stock + "200percent3 (date, ticker, budget, stocksAmount, flag, amount, avg) values (?,?,?,?,?,?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, LocalDate.parse(dates.get(0)).minusDays(1).toString());
			pstmt.setString(2, stock);
			pstmt.setString(3, String.valueOf(startBudget));
			pstmt.setString(4, String.valueOf(0));
			pstmt.setString(5, null);
			pstmt.setString(6, null);
			pstmt.setString(7, null);
			pstmt.executeUpdate();
		}
		catch(Exception e) {
			
		}
	}
	
	
	
}
