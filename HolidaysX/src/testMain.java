import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.json.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class testMain extends Application{

	static String URL = "https://feiertage-api.de/api/?jahr=2020&nur_land=BY";
	static ArrayList<LocalDate> datesL = new ArrayList<LocalDate>();
	static ArrayList<LocalDate> finalDates = new ArrayList<LocalDate>();
	static NodeList dates;
	static int years;
	static LocalDate start;
	static LocalDate end;
	static Scanner scan = new Scanner(System.in);
	static int yearURL = LocalDate.now().getYear();
	static int mon = 0;
	static int tue = 0;
	static int wed = 0;
	static int thu = 0;
	static int fri = 0;

	public static void main(String args[]) throws MalformedURLException, JSONException, IOException {
		testMain a = new testMain();
		a.startEnd();
		a.daysToNodeList();
		a.elementsToArrayList();
		a.datesInAL();
		a.testIfEqual();
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
		for (int y = 0; y < years; y++) {
			for (int i = 0; i < datesL.size(); i++) {
				finalDates.add(datesL.get(i).plusYears(y));
			}
		}
	}

	void datesInAL() throws MalformedURLException, JSONException, IOException {
		for (int y = 0; y < years; y++) {
			JSONObject json = new JSONObject(IOUtils.toString(new URL(rightURL()), Charset.forName("UTF-8")));
			String day = null;
			String date;
			int year;
			int month;
			int dayOfMonth;
			String yearx = "";
			String monthx = "";
			String dayOfMonthx = "";
			char oneDate[] = null;

			for (int x = 0; x < 4; x++) {
				yearx = "";
				monthx = "";
				dayOfMonthx = "";
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

	static String rightURL() {
		URL = "https://feiertage-api.de/api/?jahr=" + Integer.toString(yearURL) + "&nur_land=BY";
		yearURL++;
		return URL;
	}

	void testIfEqual() {
		int monx = 0;
		int tuex = 0;
		int wedx = 0;
		int thux = 0;
		int frix = 0;
		DayOfWeek weekDay;

		try {
			for (int i = 0; i < i + 1; i++) {
				if (start.equals(end)) {
					break;
				}
				if (finalDates.contains(start)) {
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
			}
		} catch (Exception e) {
			System.out.println("Something went wrong");
		}

		monx = monx * years;
		tuex = tuex * years;
		wedx = wedx * years;
		thux = thux * years;
		frix = frix * years;
		mon = mon + monx;
		tue = tue + tuex;
		wed = wed + wedx;
		thu = thu + thux;
		fri = fri + frix;
	}
	
	void ausgabe() {
		System.out.println("Holidays on a monday: " + mon);
		System.out.println("Holidays on a tuesday: " + tue);
		System.out.println("Holidays on a wednesday: " + wed);
		System.out.println("Holidays on a thursday: " + thu);
		System.out.println("Holidays on a friday: " + fri);
	}
	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void start(Stage stage) throws Exception {
		NumberAxis yAxis = new NumberAxis();
		CategoryAxis xAxis= new CategoryAxis();
		String mondays = "Mondays";
		String tuesdays = "Tuesdays";
		String wednesdays = "Wedndesday";
		String thursdays = "Thursdays";
		String fridays = "Fridays";
		
		stage.setTitle("Holidays per weekday");
		BarChart<String,Number> bc = new BarChart<String,Number>(xAxis,yAxis);
	    xAxis.setLabel("Weekday");       
	    yAxis.setLabel("Holidays");
	    XYChart.Series series = new XYChart.Series();
	    series.getData().add(new XYChart.Data(mondays, mon));
	    series.getData().add(new XYChart.Data(tuesdays, tue));
	    series.getData().add(new XYChart.Data(wednesdays, wed));
	    series.getData().add(new XYChart.Data(thursdays, thu));
	    series.getData().add(new XYChart.Data(fridays, fri));
	    
	    Scene scene = new Scene (bc, 800, 600);
	    bc.getData().addAll(series);
	    stage.setScene(scene);
	    stage.show();
	}
}