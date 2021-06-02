import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
	static String stock;
	
	void buyAndHoldEx() {
		System.out.println("Buy-And-Hold");
		testMain t = new testMain();
		Budget = Float.parseFloat(t.Data.get(0));
		startDate = t.Data.get(1);
		startBudget = Budget;
		stock = t.Data.get(2);
		try {
			buyAndHoldDatabase();
			buyAndHoldMethod();
			budgetChange();
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
		
		void buyAndHoldDatabase() {
			try {
				Connection conn = DriverManager.getConnection(DBUrl);
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
				conn.close();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		void buyAndHoldMethod() {
			stocksAmount = (int) (Budget/firstAmount);
			Budget = Budget - (stocksAmount*firstAmount);
			System.out.println("Bought-Stocks: " + stocksAmount + " at price of " + firstAmount);
			splitCor();
			Budget = Budget + (stocksAmount*lastAmount);
			System.out.println("Sold-Stocks: " + stocksAmount + " at price of " + lastAmount);
		}
		
		void splitCor() {
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
}
