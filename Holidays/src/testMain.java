import java.time.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.*;

public class testMain {

	static NodeList dates;
	static NodeList weekdays;
	static int years;
	static File file = null;
	static LocalDate start;
	static LocalDate end;
	static String country;
	static LocalDate everyday = LocalDate.now();
	static Scanner scan = new Scanner(System.in);
	ArrayList<LocalDate> datesL = new ArrayList<LocalDate>();
	ArrayList<LocalDate> finalDates = new ArrayList<LocalDate>();
	ArrayList<String> weekdaysL = new ArrayList<String>();
	

	public static void main(String[] args) {
		testMain a = new testMain();
		a.whichCountry();
		a.daysToNodeList();
		a.startEnd();
		a.elementsToArrayList();
		a.testIfEqual();
	}
	
	void whichCountry() {
		boolean found = false;
		System.out.println("Länder-Kürzel: ");
		do {
			country = scan.next();
			try {
				if (country.equals("at")) {
					file = new File("austria.xml");
					found = true;
				} else if (country.equals("pl")) {
					file = new File("poland.xml");
					found = true;
				} else {
					System.out.println("Land nicht gefunden");
				}
			} catch (Exception e) {
				System.out.println("Fehler");
			}
		} while (!found);
	}

	void daysToNodeList() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			dates = doc.getElementsByTagName("Holiday");
			weekdays = doc.getElementsByTagName("EasterDay");
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
		}
		catch(Exception e) {
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
		for(int y = 0; y < years; y++) {
			for (int i = 0; i < datesL.size(); i++) {
				finalDates.add(datesL.get(i).plusYears(y));
			}
		}

		for (int i = 0; i < weekdays.getLength(); i++) {
			Node node = weekdays.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) node;
				weekdaysL.add(eElement.getElementsByTagName("Weekday").item(0).getTextContent());
			}
		}
	}

	void testIfEqual() {
		int mon = 0;
		int tue = 0;
		int wed = 0;
		int thu = 0;
		int fri = 0;
		int monx = 0;
		int tuex = 0;
		int wedx = 0;
		int thux = 0;
		int frix = 0;
		DayOfWeek weekDay;
		String weekday;

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
			for (int i = 0; i < weekdaysL.size(); i++) {
				weekday = weekdaysL.get(i);
				if (weekday.equals("MONDAY")) {
					monx++;
				}
				if (weekday.equals("TUESDAY")) {
					tuex++;
				}
				if (weekday.equals("WEDNESDAY")) {
					wedx++;
				}
				if (weekday.equals("THURSDAY")) {
					thux++;
				}
				if (weekday.equals("FRIDAY")) {
					frix++;
				}
			}
		}
		catch(Exception e) {
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
		System.out.println("Holidays on a monday: " + mon);
		System.out.println("Holidays on a tuesday: " + tue);
		System.out.println("Holidays on a wednesday: " + wed);
		System.out.println("Holidays on a thursday: " + thu);
		System.out.println("Holidays on a friday: " + fri);
	}
}