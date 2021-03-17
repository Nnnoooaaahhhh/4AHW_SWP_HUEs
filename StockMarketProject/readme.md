<h1>Aktien-Auswerter</h1>

Die für die Ausführung nötigen Libraries, können hier heruntergeladen werden:

[Link 1] https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

[Link 2] https://mvnrepository.com/artifact/commons-io/commons-io

[Link 3] https://mvnrepository.com/artifact/org.json/json/20140107

Die heruntergeladenen Libraries müssen dann zum Classpath hinzugefügt werden, dies ist aber je nach IDE unterschiedlich

Das Programm dient dazu, Aktienwerte auszulesen und auszuwerten;

Bei diesem Programm findet ein API-Aufruf statt, der "close-Werte", also die jeweiligen Werte bei der eine Aktie am Tagesende geschlossen hat, zurückgibt.
Diese Werte werden mithilfe des Datum, das eindeutig ist, sortiert und in eine Datenbank abgespeichert. 

Zum Ausführen des Programms muss am Anfang, nach der ersten Abfrage, die Abkürzung einer Aktie angegeben werden; z.B.: AAPL (für Apple); <br>
Als nächstes muss noch der API-Key angegeben werden, denn man von [Link] https://www.alphavantage.co/ gratis anfragen kann; <br>
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

Beispiel für eine "schlechte" Aktie:<br>
<img src = "https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/StockMarketProject/RedExample.PNG">
<br><br>
Beispiel für eine "gute" Aktie:<br>
<img src = "https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/StockMarketProject/GreenExample.png">
