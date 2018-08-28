/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.konstanten;

/**
 * Diese Klasse enthält die Konstanten für Nachrichten / FehlerIds.
 * <p>
 * Aufbau der Fehler-Ids: PLWEBXXYYY
 * <ul>
 * <li>PLWEB00YYY - Allgemein
 * </ul>
 *
 * @author Capgemini, Artun Subasi
 * @author sd&amp;m AG, Felix Senn
 * @version $Id: FehlerSchluessel.java 134126 2015-04-08 13:52:30Z sdm_ahoerning $
 */
public abstract class FehlerSchluessel {

    /** Darstellung eines technischen Fehlers auf der GUI mit der Ausnahme-ID und UUID, ohne Fehlertext. */
    public static final String FEHLERTEXT_GUI_TECHNISCH = "PLWEB00100";

    /** Darstellung eines fachlichen Fehlers auf der GUI mit dem Fehlertext bzw Ausnahme-ID und UUID. */
    public static final String FEHLERTEXT_GUI_FACHLICH = "PLWEB00101";

    /** Darstellung eines technischen Fehlers im LOG mit der Ausnahme-ID und UUID und Fehlertext. */
    public static final String FEHLERTEXT_LOG_TECHNISCH = "PLWEB00102";

    /** Darstellung eines fachlichen Fehlers im LOG mit der Ausnahme-ID und UUID und Fehlertext. */
    public static final String FEHLERTEXT_LOG_FACHLICH = "PLWEB00103";

    /** Die Überschrift eines technischen Fehlers in der GUI. */
    public static final String FEHLERTEXT_GUI_TECHNISCH_UEBERSCHRIFT = "PLWEB00104";

    /** Die Überschrift eines fachlichen Fehlers in der GUI. */
    public static final String FEHLERTEXT_GUI_FACHLICH_UEBERSCHRIFT = "PLWEB00105";

    /** Der Fehlertext, welcher auf der Clientseite bei AJAX-Fehlern angezeigt wird. */
    public static final String FEHLERTEXT_GUI_AJAX = "PLWEB00106";

    /** Der Fehlertext der bei einem nicht erfolgreichen Download angezeigt wird */
    public static final String DOWNLOAD_WAR_NICHT_ERFOLGREICH = "PLWEB00112";

    /** Der Fehlertext der bei einem konkurrierendem Zugriff angezeigt wird */
    public static final String KONKURRIERENDER_ZUGRIFF = "PLWEB00113";

    /** Der Benutzer {0} darf auf die TempWebResource {1} nicht zugreifen, da sie Benutzer {2} gehört. */
    public static final String KEINE_BERECHTIGUNG_FUER_TEMP_WEB_RESOURCE = "PLWEB01000";

    /** HTML-Text Datei nicht gefunden. */
    public static final String DATEI_NICHT_GEFUNDEN_HTML = "PLWEB01001";

    /** Es muss mindestens ein Treffer ausgewählt sein. */
    public static final String MINDESTENS_EIN_TREFFER = "PLWEB10000";

    /** Es muss genau ein Treffer ausgewählt sein. */
    public static final String GENAU_EIN_TREFFER = "PLWEB10001";


    // /////////// Initialisierungs-Filter Fehler /////////////

    /** Initialisierungsfehler: Der Paramter {0} wurde nicht angegeben oder enthaelt keinen gueltigen Wert. */
    public static final String FEHLER_INITIALISIERUNGSFILTER_PARAMETER_FEHLT_ODER_UNGUELTIG = "PLWEB02001";

    // /////////// Allgemeine Fehler /////////////

    /** Es ist ein technischer Fehler aufgetreten: {0}. */
    public static final String ALLGEMEINER_TECHNISCHER_FEHLER_MIT_PARAMETER = "PLWEB90000";

    /** Die Komponente wurde falsch konfiguriert: {0}. */
    public static final String ALLGEMEIN_KOMPONENTE_FALSCH_KONFIGURIERT = "PLWEB90001";

}
