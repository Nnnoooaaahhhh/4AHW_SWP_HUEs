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


public class testMain /*extends Application*/{	

	static String fileSave;
	static String DBUrl = "jdbc:sqlite:../StockMarketProject/infos.db";
	float Budget;
	String startDate;
	static ArrayList <String> Data = new ArrayList<String>();
	float startBudget;
	
	public static void main(String[] args) throws Exception {
		
		testMain t = new testMain();
		t.GetData();
		buyAndHold a = new buyAndHold();
		strategy200 b = new strategy200();
		strategy200percent3 c = new strategy200percent3();
		getEndings g = new getEndings();
		for(int i = 2; i < Data.size(); i++) {
			a.buyAndHoldEx(t.Data.get(i));
			b.strategy200Ex(t.Data.get(i));
			c.strategy200percent3Ex(t.Data.get(i));
			g.getEndingsEx(t.Data.get(i));
		}
		
		g.print();
		
	
		
		
		
		
		

		
		

		
		
		
		//launch(args);
		
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
		}
		catch(Exception e) {
			System.out.println("Fehler in der Textdatei");
		}
		startBudget = Budget;
	}

	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage stage) throws Exception {
		testMain t = new testMain();
		t.GetData();
		Connection conn = DriverManager.getConnection(DBUrl);
		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		stage.setTitle("Stocks of " + t.Data.get(2));
		series1.setName("200er");
		series2.setName("200er 3%");
		String sql = "SELECT * from " + t.Data.get(2) + "200 where date >= '" + t.Data.get(1) + "' order by date asc";
		String sql2 = "SELECT * from " + t.Data.get(2) + "200percent3 where date >= '" + t.Data.get(1) +"' order by date asc";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				series1.getData().add(new XYChart.Data(rs.getString("date"), Float.parseFloat(rs.getString("budget"))));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		try {
			Statement stmt2 = conn.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql2);
			while (rs2.next()) {
				series2.getData()
						.add(new XYChart.Data(rs2.getString("date"), Float.parseFloat(rs2.getString("budget"))));
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		lineChart.setCreateSymbols(false);
		Scene scene = new Scene(lineChart,800,600);
		lineChart.getData().addAll(series1);
		lineChart.getData().addAll(series2);
		fileSave = LocalDate.now().toString()+t.Data.get(2);
		File theDir = new File("stocks/"+t.Data.get(2)+"/");
		if (!theDir.exists()){
		    theDir.mkdirs();
		}
		saveAsPng(scene, "stocks/"+t.Data.get(2)+"/" +fileSave+".png");
		conn.close();
		stage.close();
		System.exit(0);
	}

	void saveAsPng(Scene scene, String path) {
		WritableImage image = scene.snapshot(null);
		File file = new File(path);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}*/
}
