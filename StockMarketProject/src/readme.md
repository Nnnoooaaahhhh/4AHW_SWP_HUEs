Die für die Ausführung nötigen Libraries, können hier heruntergeladen werden:

[Link 1]https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

[Link 2]https://mvnrepository.com/artifact/commons-io/commons-io

[Link 3]https://mvnrepository.com/artifact/org.json/json/20140107

Die heruntergeladenen Libraries müssen dann zum Classpath hinzugefügt werden, dies ist aber je nach IDE unterschiedlich

Das Programm dient dazu, Aktienwerte auszulesen und auszuwerten;

Mit akutellem Stand findet momentan ein API-Aufruf statt, bei der die jeweilige Aktie zuvor eingegeben werden muss (z.B.: TSLA, AAPL,...) 
Von der ausgewählten Aktie werden dann die letzten 100 täglichen Werte abgerufen, und diese dann mit dem jeweiligen close-Wert in einer Datenbank
name "infos.db" abgespeichert; in dieser Datenbank wird auch eine Tabelle mit dem jeweiligen Namen der Aktie angelegt, falls diese noch nicht existiert;
