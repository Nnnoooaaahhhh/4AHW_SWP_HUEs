import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	
	void buyAndHoldEx() {
		System.out.println("Buy-And-Hold");
		try {
			buyAndHoldGetData();
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
				String sql = "select amount, date from " + Data.get(2) + " where date >= '" + startDate + "' order by date asc limit 1";
				String sql2 = "select amount, date from " + Data.get(2) + " order by date desc limit 1";
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
			System.out.println("End-Split: " + splitFac);
			Budget = Budget + (stocksAmount*lastAmount);
			System.out.println("Sold-Stocks: " + stocksAmount + " at price of " + lastAmount);
		}
		
		void splitCor() {
			try {
				Connection conn = DriverManager.getConnection(DBUrl);
				String sql = "select split from " + Data.get(2) + " where date >= '" + startDate + "' order by date asc";
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
