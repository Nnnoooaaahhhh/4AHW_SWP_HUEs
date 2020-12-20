<h1>Aktien-Auswerter</h1>

Die für die Ausführung nötigen Libraries, können hier heruntergeladen werden:

[Link 1]https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

[Link 2]https://mvnrepository.com/artifact/commons-io/commons-io

[Link 3]https://mvnrepository.com/artifact/org.json/json/20140107

Die heruntergeladenen Libraries müssen dann zum Classpath hinzugefügt werden, dies ist aber je nach IDE unterschiedlich

Das Programm dient dazu, Aktienwerte auszulesen und auszuwerten;

Bei diesem Programm findet ein API-Aufruf statt, der "close-Werte", also die jeweiligen Werte bei der eine Aktie am Tagesende geschlossen hat, zurückgibt.
Diese Werte werden mithilfe des Datum, das eindeutig ist, sortiert und in eine Datenbank abgespeichert.

Zum Ausführen des Programms muss am Anfang, nach der ersten Abfrage, die Abkürzung einer Aktie angegeben werden; z.B.: AAPL (für Apple);
Als nächstes wird die API aufgerufen, und alle verfügbaren close-Werte abgerufen. 
Diese werden mithilfe des Datums sortiert und in einer Tabelle in der zuvor erstellten Datenbank "infos.db" abgespeichert. Wenn der API-Aufruf stattgefunden hat, 
aber noch keine Tabelle in der Datenbank mit dem Namen der Aktie existiert, wird eine neue Tabelle mit den Spalten date, amount und avg angelegt. 
Falls die Tabelle bereits existiert, wird diese verwendet. 
In die "date"-Spalte, wird das jeweilige Datum eingetragen, in die "amount"-Spalte der jeweilige close-Wert;
In die "avg"-spalte, wird der gleitende Durchschnitt für den jeweiligen Tag aus den letzten 200 Tagen eingetragen -> Dies wird erreicht, indem die letzten 200 close-Werte addiert werden, und anschließend durch 200 subtrahiert werden. 

Mit den in der Datenbank abgespeicherten Daten, wird zum Schluss noch ein JAVAFX-Linechart erstellt;
Dieser stelle die Close-Werte und die AVG-Werte als Linie dar -> Die AVG-Linie ist immer schwarz dargestellt, die Farbe der Close-Linie hängt allerdings vom letzten verfügbaren Close-Wert ab;
Falls der letzte verfügbare Close-Wert kleiner als der letzte verfügbare AVG-Wert ist, wird die Linie rot dargestellt, da diese dann eine "schlechte" Aktie ist;
Ist der letzte verfügbare Close-Wert allerdings größer als der letzte verfügbare AVG-Wert, wird die Linie grün dargestellt, da dies dann eine "gute" Aktie ist;

Beispiel für eine "schlechte" Aktie:
<img source = "https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/StockMarketProject/exampleRed.PNG">
