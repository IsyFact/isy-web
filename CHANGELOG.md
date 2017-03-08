# v4.3.0 (unveröffentlicht)

## Bugfixes
- `IFS-30`:
	* Das Sortieren von DataTables im Client-Mode funktioniert wieder.
	* Fix: "." im Attribut reference bzw. referenceId führt dazu, dass AJAX Aufrufe nicht behandelt werden. Betrifft folgende Komponenten:
		* formSelectOneDropdown.xhtml
		* selectOneDropdown.xhtml (referenceId)
	* formSelectOneDropdown: Klick auf Label selektiert wieder das entsprechende Dropdown.

## Neuerungen
- `IFS-29`: 
	* Deaktivierte Eingabefelder haben einen entsprechenden Cursur.
	* DataTable übernimmt Änderungen von DataTable3 (DAISY).
	* Einführung von <h>-Tags für Überschriften von Panels.
	* Korrigierter JS-Code für die Formatierung von Geldbeträgen.
	* formCurrencyInput hat neuen, optionalen Parameter zum Ausrichten des Texts.
	* selectManyList und selectOneList übernommen (DAISY)
- `IFS-18`: Optionales Anzeigen der Versionsnummer im Seiten-Titel.
- `IFS-23`: Eingabe von Geldbeträgen mit mehr als zwei Nachkommastellen

# v4.2.2

# v4.2.1

## Neuerungen
- Konfigurierbaren Cache für statische Ressourcen eingebaut.
- Verbesserung Wizardoberfläche (Wizardschritte ausblendbar).

## Bugfixes
- Konflikte mit Detailansicht-Buttons in DataTable behoben.
- Fehler Lazy-Loading von Lichtbildern in Detailansicht behoben.

# v4.2.0

## Neuerungen
- Vereinfachung Quicklinksfunktionalität aus Version 4.1.2 wieder eingebaut.