<h1>Aktien-Auswerter</h1>

Die für die Ausführung nötigen Libraries, können hier heruntergeladen werden:

[Link 1] https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

[Link 2] https://mvnrepository.com/artifact/commons-io/commons-io

[Link 3] https://mvnrepository.com/artifact/org.json/json/20140107

Die heruntergeladenen Libraries müssen dann zum Classpath hinzugefügt werden, dies ist aber je nach IDE unterschiedlich

Das Programm dient dazu, Aktienwerte auszulesen und auszuwerten;

Bei diesem Programm findet ein API-Aufruf statt, der "close-Werte", also die jeweiligen Werte bei der eine Aktie am Tagesende geschlossen hat, zurückgibt.
Diese Werte werden mithilfe des Datum, das eindeutig ist, sortiert und in eine Datenbank abgespeichert. 

Am Anfang muss in der Textdatei, die sich im gleichen Verzeichnis wie das Programm befindet folgendes enthalten sein:<br>
1. Zeile: API-KEY<br>
2. Zeile: Anfangsdatum für die Zeichnung des Diagramms<br>
3. Zeile: Enddatum für die Zeichnung des Diagramms<br>
4. Zeile: größe des API-Aufrufs, full bzw. compact<br>
5. Zeile: Ab dieser Zeile stehen die Aktien-Abkürzunge, einer pro Zeile<br>

Der API-KEY kann von der Website www.alphavantage.co angefragt werden.<br>

Als nächstes wird die API aufgerufen, und alle verfügbaren close-Werte abgerufen. 
Diese werden mithilfe des Datums sortiert und in einer Tabelle in der zuvor erstellten Datenbank "infos.db" abgespeichert. Wenn der API-Aufruf stattgefunden hat, 
aber noch keine Tabelle in der Datenbank mit dem Namen der Aktie existiert, wird eine neue Tabelle mit den Spalten date, amount und avg angelegt. 
Falls die Tabelle bereits existiert, wird diese verwendet. Es wird pro Aktie auch noch eine zweite Tabelle erzeut, in der im jetzigen Zustand das jeweilige Datum mit dem dazugehörenden gleitenden Durchschnitt der letzten 200 Tage abgespeichert wird -> der 200-er Schnitt wird mit einem weiteren SQL-Befehl aus der Tabelle mit den close-Werten kalkuliert<br>

Mit den in der Datenbank abgespeicherten Daten, wird zum Schluss noch ein JAVAFX-Linechart erstellt;<br>
Dieser stellt die Close-Werte und die AVG-Werte als Linie dar -> Die AVG-Linie ist immer schwarz dargestellt, die Close-Linie wird grau dargestellt. was auch an der Legende zu erkennen ist;<br>
Die End-Werte der Y-Achse orientieren sich am höchsten und niedrigsten close-Wert -> der unterste Punkt der Achse liegt 10% unter dem niedriegsten close-Wert, der oberste Punkt liegt 10% über dem höchsten close-Wert

Falls der letzte verfügbare Close-Wert kleiner als der letzte verfügbare AVG-Wert ist, wird der Hintergrund des Charts rot dargestellt, da diese dann eine "schlechte" Aktie ist;
Ist der letzte verfügbare Close-Wert allerdings größer als der letzte verfügbare AVG-Wert, wird der Hintergrund des Charts grün dargestellt, da dies dann eine "gute" Aktie ist;
Die Einfärbung der Charts erfolgt in beiden Fällen mit einer externen .css-Datei;

Diese Diagramme werden im gleichen Verzeichnis im Ordner Stocks -> Aktienname -> "Datum + Aktienname".png gespeichert

<br><br>
<h4> Tägliches Ausführen: </h4> <br>
Wenn man das Programm täglich ausführen möchte, kann man dazu den "JARFolder" verwenden
In diesem Ordner befindet sich eine .bat Datei, die durch Ausführen die Jar-Datei ausführt; Wenn man das Programm nun täglich Ausführen möchte, muss man die .bat Datei einfach als Aufgabe im Aufgabenplaner des jeweiligen Betriebssystems hinzufügen; Die erzeugten Grafiken werden erneut im gleichen Verzeichnis abgespeichert; Wenn man verschiedene Aktien, oder andere Datums auswählen möchte, muss man dies in der Text-Datei ändern.


<br><br><br>
Beispiel für eine "schlechte" Aktie:<br>
<img src = "https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/StockMarketProject/RedExample.PNG">
<br><br>
Beispiel für eine "gute" Aktie:<br>
<img src = "https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/StockMarketProject/GreenExample.png">
