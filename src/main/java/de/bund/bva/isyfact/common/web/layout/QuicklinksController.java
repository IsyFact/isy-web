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
package de.bund.bva.isyfact.common.web.layout;

import java.util.List;

import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;

/**
 * Controller für die Quicklinks.
 *
 */
// REVIEW (JM) RAIS-47: Was ist mit Abwärtskompatibilität? Bitte wenn möglich so gestalten, dass man die
// Methoden auch ohne die neue GroupID nutzen kann. Dann hat man nur eine Default-Group die visible ist (wegen
// Legacy Systemen). Wo ist die Methode fuegeQuicklinksHinzu hin?
public class QuicklinksController extends AbstractGuiController<ApplikationseiteModel> {

    /**
     * Der Session-Key zum Speichern der Quicklinks.
     */
    private static final String SESSION_KEY_QUICKLINKS = "quicklinks";

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialisiereModel(ApplikationseiteModel model) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<ApplikationseiteModel> getMaskenModelKlasseZuController() {
        return ApplikationseiteModel.class;
    }

    /**
     * Erzeuge Quicklink Gruppe.
     *
     * @param gruppeId
     *            ID der Gruppe
     * @param title
     *            der Titel (Header)
     * @param maxAnzahl
     *            max Anzahl der Elemente in der Gruppe
     */
    public void erzeugeLinkGruppe(String gruppeId, String title, int maxAnzahl) {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);
            if (quicklinksModel == null) {
                quicklinksModel = new QuicklinksModel();
            }

            if (!quicklinksModel.hatGruppe(gruppeId)) {
                quicklinksModel.erzeugeGruppe(gruppeId, title).setMaxAnzahlElemente(maxAnzahl);
            }
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        }
    }

    /**
     * Leere Quicklink Gruppe.
     *
     * @param gruppeId
     *            ID der Gruppe
     */
    public void leereLinkGruppe(String gruppeId) {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null && quicklinksModel.hatGruppe(gruppeId)) {
                quicklinksModel.getGruppe(gruppeId).leeren();
            }

            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        }
    }

    /**
     * Setze max Anzahl der Elemente in der Gruppe.
     *
     * @param groupId
     *            ID der Gruppe
     * @param maxAnzahl
     *            max Anzahl der Elemente in der Gruppe
     */
    public void setzteMaxAnzahlElemente(String groupId, int maxAnzahl) {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null && quicklinksModel.hatGruppe(groupId)) {
                quicklinksModel.getGruppe(groupId).setMaxAnzahlElemente(maxAnzahl);
            }

        }
    }

    /**
     * Zeige bestimmte Gruppe.
     *
     * @param gruppeId
     *            ID der Gruppe
     */
    public void zeigeGruppe(String gruppeId) {
        setzetSichbarkeitFueGruppe(gruppeId, true);
    }

    /**
     * Blende bestimmte Gruppe aus.
     *
     * @param gruppeId
     *            ID der Gruppe
     */
    public void ausblendeGruppe(String gruppeId) {
        setzetSichbarkeitFueGruppe(gruppeId, false);
    }

    /**
     * Zeige alle Gruppen.
     */
    public void ausblendeAlleGruppen() {
        setzeSichtbarkeitFuerAlleGruppen(false);

    }

    /**
     * Blende alle Gruppen aus.
     */
    public void zeigeAlleGruppen() {
        setzeSichtbarkeitFuerAlleGruppen(true);

    }

    /**
     * Setzte die Eigenschaft, ob alle Gruppen angezeigt werden sollen oder nicht.
     *
     * @param visible
     *            die Wert der Flag
     */
    private void setzeSichtbarkeitFuerAlleGruppen(boolean visible) {
        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null) {
                for (QuicklinksGroup group : quicklinksModel.getAlleGruppen()) {
                    group.setSichtbar(visible);
                }
            }
        }

    }

    /**
     * Setzte die Eigenschaft, ob bestimme Gruppe angezeigt werden soll oder nicht.
     *
     * @param gruppeId
     *            ID der Gruppe
     * @param sichtbar
     *            die Wert der Flag
     */
    public void setzetSichbarkeitFueGruppe(String gruppeId, boolean sichtbar) {
        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null && quicklinksModel.hatGruppe(gruppeId)) {
                quicklinksModel.getGruppe(gruppeId).setSichtbar(sichtbar);
            }
        }

    }

    /**
     * Fügt einen Quicklink hinzu.
     *
     * @param QuicklinksElementModel
     *            der Quicklink
     * @param gruppeId
     *            ID der Gruppe
     *
     * @return gelöschte Element
     */
    public QuicklinksElementModel fuegeQuicklinkHinzu(QuicklinksElementModel QuicklinksElementModel,
        String gruppeId) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        QuicklinksElementModel candidate = null;
        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            // Wenn der Quicklink breits vorhanden ist, dann wird er aktualisiert und nach oben gesetzt
            entferneQuicklinksIntern(QuicklinksElementModel.getId(), gruppeId, quicklinksModel);

            // Quicklink immer am Anfang einfügen
            candidate = quicklinksModel.quicklinkAmAnfangHinzufuegen(QuicklinksElementModel, gruppeId, null);

            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine
            // Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);

        }

        return candidate;
    }

    /**
     * Entfernt einen Quicklink, der eine bestimmte ID besitzt.
     *
     * @param elementId
     *            die ID des Elements
     * @param gruppeId
     *            die ID der Gruppe
     *
     * @return gelöschte element
     */
    public QuicklinksElementModel entferneQuicklink(String elementId, String gruppeId) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        QuicklinksElementModel candidate = null;
        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            candidate = entferneQuicklinksIntern(elementId, gruppeId, quicklinksModel);

            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        }
        return candidate;
    }

    /**
     * Entfernt einen Quicklink, der eine bestimmte ID besitzt.
     *
     * @param id
     *            die ID
     * @param gruppeId
     *            die ID der Gruppe
     *
     * @return gelöschte element
     */
    private QuicklinksElementModel entferneQuicklinksIntern(String id, String gruppeId,
        QuicklinksModel quicklinksModel) {
        QuicklinksElementModel candidate = null;

        if (quicklinksModel != null) {

            QuicklinksGroup group = quicklinksModel.getGruppe(gruppeId);

            if (group != null && group.istNichtLeer()) {
                for (QuicklinksElementModel entry : group.getElemente()) {
                    if (id.equals(entry.getId())) {
                        candidate = entry;
                    }
                }
                group.elementLoeschen(candidate);
            }

        }

        return candidate;
    }

    /**
     * Verschiebt alle Quicklink-Elemente von einer Quicklink-Liste zu einer Anderen.
     *
     * @param von
     *            Die Header-Bezeichnung der ursprünglichen Quicklinks-Liste.
     * @param nach
     *            Die Header-Bezeichnung der Quicklinks-Liste in welche die Element verschoben werden soll.
     * @param maxAnzahl
     *            Die maximale Anzahl der anzuzeigenden Elemente.
     */
    public void verschiebeQuicklinks(String von, String nach, String title, int maxAnzahl) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null && quicklinksModel.hatGruppe(von)) {

                QuicklinksGroup vonGroup = quicklinksModel.getGruppe(von);
                List<QuicklinksElementModel> elements = vonGroup.getElemente();

                vonGroup.leeren();

                for (QuicklinksElementModel model : elements) {
                    quicklinksModel.quicklinkAmAnfangHinzufuegen(model, nach, title);
                }

            }

        }

    }
}
