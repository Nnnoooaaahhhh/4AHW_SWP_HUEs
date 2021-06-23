import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import javafx.scene.image.WritableImage;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;


public class testMain extends Application{	

	static String fileSave;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";
	float Budget;
	String startDate;
	String endDate;
	static ArrayList <String> Data = new ArrayList<String>();
	float startBudget;
	
	public static void main(String[] args) throws Exception {
		
		testMain t = new testMain();
		t.GetData();
		if(!t.validateData()) {
			System.out.println("Fehler in der Textdatei!");
			System.exit(0);
		}
		buyNHold z = new buyNHold();
		strategyAVG y = new strategyAVG();
		strategyAVG3Percent x = new strategyAVG3Percent();
		getEndings g = new getEndings();
		for(int i = 3; i < Data.size(); i++) {
			z.buyNHoldEx(t.Data.get(i));
			y.strategy200Exx(t.Data.get(i));
			x.strategy2003PercentEx(t.Data.get(i));
			g.getEndingsEx(t.Data.get(i));
		}
		
		g.print();
		
	
		
		
		
		
		

		
		

		
		
		
		launch(args);
		
	}
	
	void GetData() {
		try {
			FileReader fileReader = new FileReader("data.txt");
			BufferedReader bReader = new BufferedReader(fileReader);
			while(bReader.ready()) {
				Data.add(bReader.readLine());
			}
			
			bReader.close();
			Budget = Float.parseFloat(Data.get(0));
			startDate = Data.get(1);
			endDate = Data.get(2);
		}
		catch(Exception e) {
			System.out.println("Fehler in der Textdatei");
		}
		startBudget = Budget;
	}
	
	boolean validateData() {
		LocalDate temp;
		if(Budget < 0) {
			return false;
		}
		try {
			temp = LocalDate.parse(startDate);
			temp = LocalDate.parse(endDate);
		}
		catch(Exception e) {
			return false;
		}
		for(int i = 3; i < Data.size(); i++) {
			if(Data.get(i).length()>5) {
				return false;
			}
		}
		if(Data.contains("")) {
			return false;
		}
		return true;
	}

	public void start(Stage stage) throws Exception {
		getEndings a = new getEndings();
		testMain t = new testMain();
		String start = "Start-Budget";
		String buyNHold = "Buy-And-Hold";
		String x200er = "200er";
		String x200per3 = "200er-3%";
		ArrayList <String> Budgets = new ArrayList<String>();
		
		Connection conn = DriverManager.getConnection(DBUrl);
		
		
		for(int i = 3; i < t.Data.size(); i++) {
			String sql = "select budget from " + t.Data.get(i) +  "buyNHoldx order by date desc limit 1";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while(rs.next()) {
				Budgets.add(rs.getString("budget"));
			}
			sql = "select budget from " + t.Data.get(i) +  "200x order by date desc limit 1";
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql);
			while(rs2.next()) {
				Budgets.add(rs2.getString("budget"));
			}
			sql = "select budget from " + t.Data.get(i) +  "2003Percentx order by date desc limit 1";
			Statement stmt3 = conn.createStatement();
			ResultSet rs3 = stmt3.executeQuery(sql);
			while(rs3.next()) {
				Budgets.add(rs3.getString("budget"));
			}
		}
		
		
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		final StackedBarChart<String, Number> sbc = new StackedBarChart<String, Number>(xAxis, yAxis);
		int ticksAmount = t.Data.size()-3;
		
		
		XYChart.Series series1 = new XYChart.Series();
		series1.setName("Startbudget");
		series1.getData().add(new XYChart.Data(start, Float.parseFloat(t.Data.get(0))));
		XYChart.Series series2 = new XYChart.Series();
		XYChart.Series series3 = new XYChart.Series();
		XYChart.Series series4 = new XYChart.Series();
		XYChart.Series series5 = new XYChart.Series();
		XYChart.Series series6 = new XYChart.Series();
		try {
			series2.setName(t.Data.get(3));
			series2.getData().add(new XYChart.Data<>("buy-and-hold", Float.parseFloat(Budgets.get(0))));
			series2.getData().add(new XYChart.Data<>("200er", Float.parseFloat(Budgets.get(1))));
			series2.getData().add(new XYChart.Data<>("200er-3%", Float.parseFloat(Budgets.get(2))));
			
			series3.setName(t.Data.get(4));
			series3.getData().add(new XYChart.Data<>("buy-and-hold", Float.parseFloat(Budgets.get(3))));
			series3.getData().add(new XYChart.Data<>("200er", Float.parseFloat(Budgets.get(4))));
			series3.getData().add(new XYChart.Data<>("200er-3%", Float.parseFloat(Budgets.get(5))));
			
			series4.setName(t.Data.get(5));
			series4.getData().add(new XYChart.Data<>("buy-and-hold", Float.parseFloat(Budgets.get(6))));
			series4.getData().add(new XYChart.Data<>("200er", Float.parseFloat(Budgets.get(7))));
			series4.getData().add(new XYChart.Data<>("200er-3%", Float.parseFloat(Budgets.get(8))));
		
			series5.setName(t.Data.get(6));
			series5.getData().add(new XYChart.Data<>("buy-and-hold", Float.parseFloat(Budgets.get(9))));
			series5.getData().add(new XYChart.Data<>("200er", Float.parseFloat(Budgets.get(10))));
			series5.getData().add(new XYChart.Data<>("200er-3%", Float.parseFloat(Budgets.get(11))));
			
			series6.setName(t.Data.get(7));
			series6.getData().add(new XYChart.Data<>("buy-and-hold", Float.parseFloat(Budgets.get(12))));
			series6.getData().add(new XYChart.Data<>("200er", Float.parseFloat(Budgets.get(13))));
			series6.getData().add(new XYChart.Data<>("200er-3%", Float.parseFloat(Budgets.get(14))));
		}
		catch(Exception e) {
			System.out.println("Weniger als 5 Aktien angegeben...");
		}
		stage.setTitle("Vergleich vom " + LocalDate.now().toString());
		sbc.setTitle("Vergleich vom " + LocalDate.now().toString());
		xAxis.setLabel("Strategy");
		yAxis.setLabel("Budget");
		Scene scene = new Scene(sbc, 800,600);
		sbc.getData().addAll(series1, series2, series3, series4, series5, series6);
		stage.setScene(scene);
		stage.show();
		}
	}
