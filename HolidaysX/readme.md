Die .java Datei des Programmes kann hier direkt über Github heruntergeladen werden;

Die für die Ausführung nötigen Libraries, können hier heruntergeladen werden:

[Link 1]https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc

[Link 2]https://mvnrepository.com/artifact/commons-io/commons-io

[Link 3]https://mvnrepository.com/artifact/org.json/json/20140107


Die heruntergeladenen Libraries müssen dann zum Classpath hinzugefügt werden, dies ist aber je nach IDE 
unterschiedlich

Bei der Ausführung wird zuerst nach einer Anzahl von Jahren gefragt, die Zahl eingeben (z.B. 5) und dann
mit der Eingabetaste bestätigen;
Dann bei der Anfrage vom Startdatum ein Datum mit dem gegeben Format eingeben (z.B. 2020 04 13) und erneut
mit der Eingabetaste bestätigen;

Nun wird ein API Aufruf gestartet, um alle Feiertage in dieser Zeit, also ab dem angegebenen Startdatum bis alle
Jahre durchgegangen wurden, also solange wie angegeben wurde, zu speichern;
Dann werden alle über die API verfügbaren Ferien abgerufen und ebenfalls abgespeichert;

Bei jedem Feiertag wird nun geschaut, welcher Wochentag es ist;
Falls der Feiertag aber in den Ferien ist, wird dies nicht geschaut, falls er nicht in den Ferien ist 
wird bei dem jeweiligen Wochentag ein Zähler um 1 erhöht;

Danach werden die entsprechenden Daten über SQLite in eine Datenbank eingetragen; Die Datenbank trägt hier
den Namen "databaseDates.db"; Falls diese bereits existiert wird in diese hineingeschrieben, falls nicht wird
eine neue Datenbank mit diesem Namen angelegt;
In die Datenbank werden allerdings nur die Daten von den Wochentagen angelegt, die ohne die Ferien, also wo die 
Ferientage bereits weggezählt wurden, eingetragen;

Nun werden noch alle Daten aus der Datenbank ausgegeben, alle Lifetime-Tage zusammengezählt und ausgegeben, 
und schließlich noch ein JAVAFX Diagramm mit den Daten des derzeit ausgeführten Programms aufgerufen;
Beim JAVAFX Diagramm werden wieder die Tage ohne Ferien und mit Ferien dargestellt;

Preview des JAVAFX Diagrammes, bei einer Anzahl von Jahren von 10:

![Preview](https://github.com/Nnnoooaaahhhh/4AHW_SWP_HUEs/blob/master/HolidaysX/example.PNG)
