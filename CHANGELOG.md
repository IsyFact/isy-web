# v4.3.3
- Header-Bereich: Linkes und rechtes Logo und der Text, der neben dem rechten Logo stehen soll, sind nun konfigurierbar.

## Hinweise zum Upgrade
- Anwendungen müssen nun die Konfigurationsparameter 'gui.header.logo.rechts.pfad', 'gui.header.logo.links.pfad' und 'gui.header.text.logo.rechts' setzen, damit die Logos bzw. der Text angezeigt werden.

# v4.3.2
- `IFS-17`: Umbenennung der Artifact-ID und Group-ID

## Bugfixes
- Fix für das Four-Eyes-Icon.

# v4.3.1

## Bugfixes
- Attribut "customId" in Form-Komponenten wird nicht mehr in das class-Attribut, sondern ein data-Attribut (data-isy-custom-id) geschrieben

# v4.3.0

## Bugfixes
- `IFS-30`:
	* Das Sortieren von DataTables im Client-Mode funktioniert wieder.
	* Fix: "." im Attribut reference bzw. referenceId führt dazu, dass AJAX Aufrufe nicht behandelt werden. Betrifft folgende Komponenten:
		* formSelectOneDropdown.xhtml
		* selectOneDropdown.xhtml (referenceId)
	* formSelectOneDropdown: Klick auf Label selektiert wieder das entsprechende Dropdown.
- Tabs: Das Attribut skipAction (tabHeader) hat nun den Standardwert false und korrespondiert somit zum Standardwert des Attributs preload (tabContent).
	Standardmäßig wird ein Tab dementsprechend nicht vorgeladen. Wenn das Vorladen gewünscht ist, müssen beide Attribute explizit auf true gesetzt werden.

## Neuerungen
- `IFS-29`: 
	* Deaktivierte Eingabefelder haben einen entsprechenden Cursur.
	* DataTable übernimmt Änderungen von DataTable3.
	* Einführung von <h>-Tags für Überschriften von Panels.
	* Korrigierter JS-Code für die Formatierung von Geldbeträgen.
	* formCurrencyInput hat neuen, optionalen Parameter zum Ausrichten des Texts.
	* selectManyList und selectOneList übernommen.
	* Labels gefixt für:
		* formActionInput
		* formTextarea
		* formListpicker
		* formCurrencyInput
		* formBrowseAndCollect		
- `IFS-18`: Optionales Anzeigen der Versionsnummer im Seiten-Titel.
- `IFS-23`: Eingabe von Geldbeträgen mit mehr als zwei Nachkommastellen

## Hinweise zum Upgrade
- Tabs: Anwendungen die das Attribut preload (tabContent) auf true gesetzt haben, ohne das Attribut skipAction (tabHeader) explizit auch auf true gesetzt zu haben, müssen skipAction nun auch explizit auf true setzen (siehe oben: Bugfix IFRF-24). Generell ist darauf zu achten, dass beide Attribute denselben Wert haben. Wenn die Werte in der Anwendung überhaupt nicht explizit gesetzt werden, muss nichts unternommen werden.

# v4.2.7

## Bugfixes
- Fix für Labels, die nicht zum dazugehörigen Input passen.

# v4.2.6

## Bugfixes
- Fix für ViewState wenn Browser Zurück-Button gedrückt wird. Es wird jetzt ein Link angezeigt, der zurück zur Anwendung führt.

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