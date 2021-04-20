import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javafx.scene.image.WritableImage;
import org.apache.commons.io.IOUtils;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class testiMain extends Application{
	
	static String fileSave;
	static int counter;

	@SuppressWarnings("static-access")
	public static void main(String[] args) throws Exception {
		
		try {
			methods b = new methods();
			b.decide();
			for(counter = 3; counter < b.ticks.size(); counter++) {
				System.out.println("Current Stock: " + b.ticks.get(counter));
				b.dates.clear();
				b.symbol = b.ticks.get(counter);
				b.link = IOUtils.toString(new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED&symbol="
						+ b.ticks.get(counter) + "&outputsize=" + b.getLinkSize() +"&apikey="+b.key), Charset.forName("UTF-8"));
				b.connect(b.link);
				b.database();
				System.out.println(b.ticks.get(counter)+ " finished;" + "\n");
			}
			launch(args);
		}
		catch(Exception e) {
			System.out.println("Ein Fehler ist aufgetreten!");
		}
		finally {
			System.exit(0);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access" })
	public void start(Stage stage) throws Exception {
		methods a = new methods();
		Connection conn = DriverManager.getConnection(methods.DBurl);
		
		for(int i = 3; i < a.ticks.size(); i++) {
			XYChart.Series series1 = new XYChart.Series();
			XYChart.Series series2 = new XYChart.Series();
			ArrayList<Float> amounts = new ArrayList<Float>();
			CategoryAxis xAxis = new CategoryAxis();
			NumberAxis yAxis = new NumberAxis();
			LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);	
			stage.setTitle("Stocks of " + a.ticks.get(i));
			series1.setName("Close-Values");
			series2.setName("AVG-Values");
			String sql = "SELECT * from " + a.ticks.get(i)+"Adj"+ " where date > '"+a.startDate +"' and date < '"+ a.endDate+"' order by date asc";
			String sql2 = "SELECT * from " + a.ticks.get(i)+"avgAdj" + " where date > '" + a.startDate + "' and date < '" + a.endDate +"' order by date asc";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while (rs.next()) {
					series1.getData().add(new XYChart.Data(rs.getString("date"), Float.parseFloat(rs.getString("amount"))));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			try {
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
			while (rs2.next()) {
				series2.getData().add(new XYChart.Data(rs2.getString("date"), Float.parseFloat(rs2.getString("avg"))));
			}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
			lineChart.setCreateSymbols(false);
			Scene scene = new Scene(lineChart,800,600);
			lineChart.getData().addAll(series1, series2);
			sql = "SELECT * from " + a.ticks.get(i)+"Adj" + " where date > '" + a.startDate + "' and date < '" + a.endDate +"' order by date desc limit 1";
			sql2 = "SELECT * from " + a.ticks.get(i)+"avgAdj" + " where date > '" + a.startDate + "' and date < '" + a.endDate +"' order by date desc limit 1";
			float lastClose = 0;
			float lastAVG = 0;
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				Statement stmt2 = conn.createStatement();
				ResultSet rs2 = stmt2.executeQuery(sql2);
				while (rs.next()) {
					lastClose = Float.parseFloat(rs.getString("amount"));
					lastAVG = Float.parseFloat(rs2.getString("avg"));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			if(lastClose > lastAVG) {
				scene.getStylesheets().add("green.css");
			}else {
				scene.getStylesheets().add("red.css");
			}
			series2.getNode().setStyle("-fx-stroke: black; ");
			
			sql = "SELECT * from " + a.ticks.get(i) + "Adj"+" where date > '" + a.startDate + "' and date < '" + a.endDate +"' order by amount desc";
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				while (rs.next()) {
					amounts.add(Float.parseFloat(rs.getString("amount")));
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			amounts.sort(null);
			yAxis.setAutoRanging(false);
			yAxis.setLowerBound(amounts.get(0)*0.9);
			yAxis.setUpperBound(amounts.get(amounts.size()-1)*1.1);
			stage.setScene(scene);
			Parent root = scene.getRoot();
			scene.setRoot(root);
			fileSave = LocalDate.now().toString()+a.ticks.get(i);
			File theDir = new File("stocks/"+a.ticks.get(i)+"/");
			if (!theDir.exists()){
			    theDir.mkdirs();
			}
			saveAsPng(scene, "stocks/"+a.ticks.get(i)+"/" +fileSave+".png");
			series1.getData().removeAll();
			series2.getData().removeAll();
			amounts.clear();
		}
		conn.close();
		stage.close();
		System.exit(0);	
		}
	
		void saveAsPng(Scene scene, String path){
			WritableImage image = scene.snapshot(null);
			File file = new File(path);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                e.printStackTrace();
            }
		}
}


