# v4.7.2
- `IFE-51`: Unterstützung Fontawesome 4 und 5 Icons 

# v4.7.0
- `IFS-96`: Autoscroll Funtion für Tab-Controls
- `IFS-142`: Neuimplementierung Breadcrumb
- `IFS-150`: OptimisticLockHandler zur Behandlung von OptimisticLockExceptions
- `IFS-153`: Listpicker Positions-Autofocus im Dropdownfenster
- `IFS-154`: Datepicker zeigt nach manueller Eingabe eingegebenes Datum
- `IFS-163`: Unterstützung für IE8 eingestellt
- `IFS-165`: UI-Block wird bei manchen Fehlermeldungen nicht deaktiviert 
- `IFS-182`: Datepicker um Übernahme des heutigen Datums erweitert
- `IFS-202`: DownloadHelper hinzugefügt

# v4.6.0
- `IFS-55`: Vervollständigung der Datumseingabe im DatePicker bei zweistelliger Jahreszahl.
- `IFS-58`: Alte Breadcrumb-Implementierung als Deprecated erklärt.
- `IFS-74`: isy-style aus Build herausgelöst. Die isy-style muss als zusätzliche Maven-Dependency eingebunden werden.
- `IFS-82`: Ausrichtung von Tabelleninhalten mittig, links- und rechtsbündig möglich.
- `IFS-86`: GUI-Performance-Optimmierung.
- `IFS-87`: Ajax-Funktionalität im buttonToolbar.xhtml deaktivierbar gemacht.
- `IFS-91`: Patch für JSF Composite Components und JSTL-Tags.
- `IFS-93`: Rendering von Eingabefeldern im Readonly-Modus auf Label geändert.
- `IFS-94`: Neue Widgets: formsNumericInput und formNumericInputWithFourEyes.
- `IFS-97`: Einklappbare Linksnavigation.
- `IFS-100`: Cache-Period auf 24 Stunden gesetzt.
- `IFS-101`: Tab-Widgets blockieren bei Tab-Wechsel.
- `IFS-107`: Konfiguration von Farben, Texten und Logos. 
- `IFS-112`: Grunt Maven Plugin durch Frontend Maven Plugin ersetzt.

# v4.5.2
- `IFS-92`: Austausch des Icon-Fonts durch Font Awesome.

# v4.5.0
- `IFS-34`: formUpload übernommen.

## Hinweise zum Upgrade
- Der Tag isy:upload ist entfallen, stattdessen ist nun formUpload zu nutzen.
- formUpload: Zur Nutzung der Komponente muss die Anwendung javax.servlet-api mindestens in Version 3.0.1 einbinden und überall die servlet-api 2.5 exkludieren, da sich der Name des Artefakts geändert hat. In der Context-Konfiguration des Tomcats muss `allowCasualMultipartParsing=true` gesetzt werden. In der web.xml muss das FacesServlet um Parameter zur `multipart-config` erweitert werden. Im entsprechenden Flow muss das Flag `multipartForm` des `globalFlowModel`s auf true gesetzt werden.

# v4.4.0
- `IFS-39`: Ein generischer Bestätigungsdialog ist nun verfügbar.
- `RF-161`: Bibliotheken binden genutzte Bibliotheken direkt ein und nicht mehr über BOM-Bibliotheken
- `IFS-60`: Datatable zeigt einen Hinweis, wenn eine Tabelle keine Treffer enthält.
- `IFS-56`: Die Checkbox in Überschriftzeilen von Treffertabellen ist nun eine Tri-State-Checkbox.
- `IFS-61`: Die Darstellung sortierbarer Spalten von Tabellen wurde verbessert.
- `IFS-41`: In Listpickern kann der Schlüssel aufgelöst werden (siehe Attribut `inputComplement`). Das Feature funktioniert nur für Listpicker, die eine Inputmask definiert haben.

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