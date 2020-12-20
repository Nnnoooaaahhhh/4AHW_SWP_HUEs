import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import org.json.JSONException;
import javafx.application.Application;
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
		stage.setTitle("Stocks of " + methods.symbol);
		CategoryAxis xAxis = new CategoryAxis();
		NumberAxis yAxis = new NumberAxis();
		LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
		
		XYChart.Series series1 = new XYChart.Series();
		XYChart.Series series2 = new XYChart.Series();
		series1.setName("Close-Values");
		series2.setName("AVG-Values");
		Connection conn = DriverManager.getConnection(methods.DBurl);
		String sql = "SELECT * from " + methods.symbol + " order by date asc";
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				series1.getData().add(new XYChart.Data(rs.getString("date"), Float.parseFloat(rs.getString("amount"))));
				series2.getData().add(new XYChart.Data(rs.getString("date"), Float.parseFloat(rs.getString("avg"))));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		lineChart.setCreateSymbols(false);
		
		Scene scene = new Scene(lineChart,800,600);
		lineChart.getData().addAll(series1, series2);
		sql = "SELECT * from " + methods.symbol + " order by date desc limit 1";
		float lastClose = 0;
		float lastAVG = 0;
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				lastClose = Float.parseFloat(rs.getString("amount"));
				lastAVG = Float.parseFloat(rs.getString("avg"));
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		if(lastClose > lastAVG) {
			series1.getNode().setStyle("-fx-stroke: #00CB18; ");
		}else {
			series1.getNode().setStyle("-fx-stroke: #FB2C00; ");
		}
		series2.getNode().setStyle("-fx-stroke: black; ");
		stage.setScene(scene);
		stage.show();
		}
}


