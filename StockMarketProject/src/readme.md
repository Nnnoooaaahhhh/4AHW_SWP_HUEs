Das Programm dient dazu, Aktienwerte auszulesen und auszuwerten;

Mit akutellem Stand findet momentan ein API-Aufruf statt, bei der die jeweilige Aktie zuvor eingegeben werden muss (z.B.: TSLA, AAPL,...) 
Von der ausgewählten Aktie werden dann die letzten 100 täglichen Werte abgerufen, und diese dann mit dem jeweiligen close-Wert in einer Datenbank
name "infos.db" abgespeichert; in dieser Datenbank wird auch eine Tabelle mit dem jeweiligen Namen der Aktie angelegt, falls diese noch nicht existiert;
