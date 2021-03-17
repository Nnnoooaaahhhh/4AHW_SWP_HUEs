import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javafx.scene.image.WritableImage;
import org.json.JSONException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;

public class testiMain extends Application{

	public static void main(String[] args) throws JSONException, MalformedURLException, IOException, SQLException {
		methods a = new methods();
		a.connect(a.decide());
		a.database();
		launch(args);
	}

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void start(Stage stage) throws Exception {
		ArrayList<Float> amounts = new ArrayList<Float>();
		stage.setTitle("Stocks of " + methods.symbol);
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		
		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		series1.setName("Close-Values");
		series2.setName("AVG-Values");
		Connection conn = DriverManager.getConnection(methods.DBurl);
		String sql = "SELECT * from " + methods.symbol +"Adj"+ " order by date asc";
		String sql2 = "SELECT * from " + methods.symbol+"avgAdj" + " order by date asc";
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
		sql = "SELECT * from " + methods.symbol +"Adj" + " order by date desc limit 1";
		sql2 = "SELECT * from " + methods.symbol+"avgAdj" + " order by date desc limit 1";
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
		
		sql = "SELECT * from " + methods.symbol + "Adj"+" order by amount desc";
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
		stage.show();
		
		saveAsPng(scene, methods.symbol+ LocalDate.now().toString()+".png");
		
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


