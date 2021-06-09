import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class getEndings {
	
	static Connection conn = null;
	static float buyNHoldSum = 0;
	static float x200Sum = 0;
	static float x200percent3Sum = 0;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";

	void getEndingsEx(String stock) {
		openConnection();
		buyNHoldData(stock);
		strategy200Data(stock);
		strategy200percent3Data(stock);
		closeConnection();
	}
	
	
	void buyNHoldData(String stock) {
		String sql = "Select budget from " + stock + "buyNHold order by date desc limit 1";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				buyNHoldSum = buyNHoldSum + Float.parseFloat(rs.getString("budget"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void strategy200Data(String stock) {
		String sql = "Select budget from " + stock + "200 order by date desc limit 1";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				x200Sum = x200Sum + Float.parseFloat(rs.getString("budget"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void strategy200percent3Data(String stock) {
		String sql = "Select budget from " + stock + "200percent3 order by date desc limit 1";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				x200percent3Sum = x200percent3Sum + Float.parseFloat(rs.getString("budget"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	void print() {
		testMain t = new testMain();
		float startBudget = Float.parseFloat(t.Data.get(0));
		startBudget = startBudget/(t.Data.size()-2);
		float temp;
		float budgetPercent;
		temp = buyNHoldSum - startBudget;
		System.out.println("Buy-And-Hold mit Start-Budget = " + startBudget);
		System.out.println("Endbudget = " + buyNHoldSum);
		if(temp > 0) {
			budgetPercent = Math.abs(temp/startBudget) + 1;
			System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
		}
		else {
			budgetPercent = buyNHoldSum/startBudget;
			System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
		}
		
		temp = x200Sum - startBudget;
		System.out.println("200er mit Start-Budget = " + startBudget);
		System.out.println("Endbudget = " + x200Sum);
		if(temp > 0) {
			budgetPercent = Math.abs(temp/startBudget) + 1;
			System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
		}
		else {
			budgetPercent = x200Sum/startBudget;
			System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
		}
		
		temp = x200percent3Sum - startBudget;
		System.out.println("200er 3% mit Start-Budget = " + startBudget);
		System.out.println("Endbudget = " + x200percent3Sum);
		if(temp > 0) {
			budgetPercent = Math.abs(temp/startBudget) + 1;
			System.out.println("Budget in % vom Startbudget: " + budgetPercent * 100 + "%");
		}
		else {
			budgetPercent = x200percent3Sum/startBudget;
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
