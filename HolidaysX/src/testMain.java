import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.xml.parsers.*;
import org.apache.commons.io.IOUtils;
import org.json.*;
import org.w3c.dom.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.sql.*;

public class testMain extends Application {

	static String URLdates;
	static String free = "https://ferien-api.de/api/v1/holidays/BY";
	static ArrayList<LocalDate> datesL = new ArrayList<LocalDate>();
	static ArrayList<LocalDate> finalDates = new ArrayList<LocalDate>();
	static ArrayList<LocalDate> frees = new ArrayList<LocalDate>();
	static NodeList dates;
	static int years;
	static LocalDate start;
	static LocalDate end;
	static Scanner scan = new Scanner(System.in);
	static int yearURLdates;
	static int yearURLfree;
	static int mon = 0;
	static int tue = 0;
	static int wed = 0;
	static int thu = 0;
	static int fri = 0;
	static int lifetimeMondays = 0;
	static int lifetimeTuesdays = 0;
	static int lifetimeWednesdays = 0;
	static int lifetimeThursdays = 0;
	static int lifetimeFridays = 0;
	
	static boolean freeDay = false;

	public static void main(String args[]) throws MalformedURLException, JSONException, IOException {
		testMain a = new testMain();
		a.startEnd();
		a.daysToNodeList();
		a.elementsToArrayList();
		a.datesInAL();
		a.getFrees();
		a.testIfEqual();
		a.database("databaseDates.db");
		a.ausgabe();
		launch(args);

	}

	static String getWert(JSONObject json, String key) throws JSONException {
		JSONObject x = (JSONObject) json.get(key);
		String date = x.getString("datum");
		return date;
	}

	void daysToNodeList() {
		try {
			File file = new File("austria.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			dates = doc.getElementsByTagName("Holiday");
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
	}

	void startEnd() {
		try {
			System.out.println("How many years? ");
			years = scan.nextInt();
			System.out.println("Start-Date: (yyyy/mm/dd)");
			start = LocalDate.of(scan.nextInt(), scan.nextInt(), scan.nextInt());
			end = start.plusYears(years);
			scan.close();
			yearURLdates = start.getYear();
		} catch (Exception e) {
		}
	}

	void elementsToArrayList() {
		for (int i = 0; i < dates.getLength(); i++) {
			Node node = dates.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				datesL.add(LocalDate.of((LocalDate.now().getYear()),
						Integer.parseInt(eElement.getElementsByTagName("Month").item(0).getTextContent()),
						Integer.parseInt(eElement.getElementsByTagName("Day").item(0).getTextContent())));
			}
		}
		for (int y = 0; y <= years; y++) {
			for (int i = 0; i < datesL.size(); i++) {
				finalDates.add(datesL.get(i).plusYears(y));
			}
		}
	}

	void datesInAL() throws MalformedURLException, JSONException, IOException {
		for (int y = 0; y < years; y++) {
			JSONObject json = new JSONObject(IOUtils.toString(new URL(rightURL1()), Charset.forName("UTF-8")));
			String day = null;
			String date;
			for (int x = 0; x < 4; x++) {
				if (x == 0) {
					day = "Ostermontag";
				}
				if (x == 1) {
					day = "Fronleichnam";
				}
				if (x == 2) {
					day = "Christi Himmelfahrt";
				}
				if (x == 3) {
					day = "Pfingstmontag";
				}
				date = getWert(json, day);
				finalDates.add(LocalDate.parse(date));

			}
		}
	}

	static String rightURL1() {
		URLdates = "https://feiertage-api.de/api/?jahr=" + Integer.toString(yearURLdates) + "&nur_land=BY";
		yearURLdates++;
		return URLdates;
	}

	void testIfEqual() {
		DayOfWeek weekDay;
		try {
			for (int i = 0; i < i + 1; i++) {
				if (frees.contains(start)) {
					freeDay = true;
				}
				if (start.equals(end)) {
					break;
				}
				if (finalDates.contains(start) && !freeDay) {
					weekDay = start.getDayOfWeek();
					if (weekDay.equals(DayOfWeek.MONDAY)) {
						mon++;
					}
					if (weekDay.equals(DayOfWeek.TUESDAY)) {
						tue++;
					}
					if (weekDay.equals(DayOfWeek.WEDNESDAY)) {
						wed++;
					}
					if (weekDay.equals(DayOfWeek.THURSDAY)) {
						thu++;
					}
					if (weekDay.equals(DayOfWeek.FRIDAY)) {
						fri++;
					}
				}
				start = start.plusDays(1);
				freeDay = false;
			}
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}
	}

	void ausgabe() {
		System.out.println("\n" + "Holidays on a monday: " + mon);
		System.out.println("Holidays on a tuesday: " + tue);
		System.out.println("Holidays on a wednesday: " + wed);
		System.out.println("Holidays on a thursday: " + thu);
		System.out.println("Holidays on a friday: " + fri);
		 
        System.out.print("\n" + "Lifetime Mondays :" + lifetimeMondays);
        System.out.print("\n" + "Lifetime Tuesdays :" + lifetimeTuesdays);
        System.out.print("\n" + "Lifetime Wednesdays :" + lifetimeWednesdays);
        System.out.print("\n" + "Lifetime Thursdays :" + lifetimeThursdays);
        System.out.print("\n" + "Lifetime Fridays :" + lifetimeFridays);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage) throws Exception {
		NumberAxis yAxis = new NumberAxis();
		CategoryAxis xAxis = new CategoryAxis();
		String mondays = "Mondays";
		String tuesdays = "Tuesdays";
		String wednesdays = "Wedndesday";
		String thursdays = "Thursdays";
		String fridays = "Fridays";
		stage.setTitle("Holidays per weekday during " + (end.minusYears(years)) + " - " + end);
		BarChart<String, Number> bc = new BarChart<String, Number>(xAxis, yAxis);
		xAxis.setLabel("Weekday");
		yAxis.setLabel("Holidays");
		XYChart.Series series = new XYChart.Series();
		series.getData().add(new XYChart.Data(mondays, mon));
		series.getData().add(new XYChart.Data(tuesdays, tue));
		series.getData().add(new XYChart.Data(wednesdays, wed));
		series.getData().add(new XYChart.Data(thursdays, thu));
		series.getData().add(new XYChart.Data(fridays, fri));
		series.setName("Holidays per weekday");
		Scene scene = new Scene(bc, 800, 600);
		bc.getData().addAll(series);
		stage.setScene(scene);
		stage.show();
	}

	void getFrees() throws JSONException, IOException {

		String start;
		String end;
		LocalDate temp;
		JSONArray ob = new JSONArray(
				IOUtils.toString(new URL("https://ferien-api.de/api/v1/holidays/BY"), Charset.forName("UTF-8")));
		for (int i = 0; i < ob.length(); i++) {
			JSONObject job = ob.getJSONObject(i);
			start = "";
			end = "";
			start = (String) job.get("start");
			start = start.split("T")[0];
			end = (String) job.get("end");
			end = end.split("T")[0];
			temp = LocalDate.parse(start);
			for (int y = 0; y < y + 1; y++) {
				frees.add(temp);
				if (temp.equals(LocalDate.parse(end))) {
					break;
				}
				temp = temp.plusDays(1);
			}
		}
	}

	void database(String databasename) {
		String url = "jdbc:sqlite:" + databasename;
		String sql = "CREATE TABLE if not exists testdata ( date text, mondays int, tuesdays int, wednesdays int, thursdays int, fridays int, years int)";
		try {
			Connection conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		sql = "INSERT INTO testdata(date, mondays, tuesdays, wednesdays, thursdays, fridays, years) VALUES(?,?,?,?,?,?,?)";

		try {
			Connection conn = this.connect(databasename);
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
			pstmt.setInt(2, mon);
			pstmt.setInt(3, tue);
			pstmt.setInt(4, wed);
			pstmt.setInt(5, thu);
			pstmt.setInt(6, fri);
			pstmt.setInt(7, years);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
        sql = "SELECT * FROM testdata";  
        
        try {  
            Connection conn = this.connect(databasename);  
            Statement stmt  = conn.createStatement();  
            ResultSet rs    = stmt.executeQuery(sql);  
            int temp;
            while (rs.next()) {  
                System.out.print(rs.getString("date") +  "\t");   
                temp = rs.getInt("mondays");
                System.out.print(temp + "\t");
                lifetimeMondays = lifetimeMondays + temp;
                temp = rs.getInt("tuesdays");
                System.out.print(temp + "\t");
                lifetimeTuesdays = lifetimeTuesdays + temp;
                temp = rs.getInt("wednesdays");
                System.out.print(temp + "\t");
                lifetimeWednesdays = lifetimeWednesdays + temp;
                temp = rs.getInt("thursdays");
                System.out.print(temp + "\t");
                lifetimeThursdays = lifetimeThursdays + temp;
                temp = rs.getInt("fridays");
                System.out.print(temp + "\t");
                System.out.println(rs.getInt("years"));
                lifetimeFridays = lifetimeFridays + temp;
            }
        } catch (SQLException e) {  
            System.out.println(e.getMessage());  
        }  
	}

	private Connection connect(String databasename) {
		String url = "jdbc:sqlite:" + databasename;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return conn;
	}

}