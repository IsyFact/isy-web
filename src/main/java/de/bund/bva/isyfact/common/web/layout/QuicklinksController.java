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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.RequestContextHolder;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Controller für die Quicklinks.
 *
 */
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class QuicklinksController extends AbstractGuiController<ApplikationseiteModel> {

    /**
     * Der Session-Key zum Speichern der Quicklinks.
     */
    private static final String SESSION_KEY_QUICKLINKS = "quicklinks";

    /**
     * Die Konfiguration.
     */
    private Konfiguration konfiguration;

    @Autowired
    public QuicklinksController(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialisiereModel(ApplikationseiteModel model) {
        // Der aktuelle Flow. Wenn ein Subflow aufgerufen wurde, dann wird hier der ursprüngliche Flow
        // ausgelesen.
        FlowDefinition flowDefinition =
            RequestContextHolder.getRequestContext().getFlowExecutionContext().getDefinition();

        String flowName = flowDefinition.getId();
        Splitter splitter = Splitter.on(",").trimResults();
        String gruppenIdsKonfig = this.konfiguration.getAsString("gui.quicklinks.gruppenIds", null);
        if (gruppenIdsKonfig != null) {
            List<String> gruppenIds = Lists.newArrayList(splitter.split(gruppenIdsKonfig));
            if (!gruppenIds.isEmpty()) {
                // Gruppen existieren => Sessiondaten laden und synchronisieren
                SharedAttributeMap<Object> sessionMap =
                    ExternalContextHolder.getExternalContext().getSessionMap();
                synchronized (sessionMap.getMutex()) {
                    QuicklinksModel quicklinksModel =
                        (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);
                    if (quicklinksModel == null) {
                        quicklinksModel = new QuicklinksModel();
                        sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
                    }
                    for (String gruppeId : gruppenIds) {
                        QuicklinksGruppeModel quicklinksGruppeModel =
                            quicklinksModel.getQuicklinksGruppen().get(gruppeId);
                        if (quicklinksGruppeModel == null) {
                            quicklinksGruppeModel = new QuicklinksGruppeModel();
                            quicklinksGruppeModel.setGruppeId(gruppeId);
                            quicklinksModel.getQuicklinksGruppen().put(gruppeId, quicklinksGruppeModel);
                        }
                        // Attribut contextflow laden
                        String contextFlowKonfig = this.konfiguration
                            .getAsString("gui.quicklinks." + gruppeId + ".contextflow", null);
                        if (contextFlowKonfig != null) {
                            List<String> contextFlows = Lists.newArrayList(splitter.split(contextFlowKonfig));
                            quicklinksGruppeModel.setSichtbar(contextFlows.contains(flowName));
                        } else {
                            quicklinksGruppeModel.setSichtbar(true);
                        }

                        // Attribut text laden
                        String text =
                            this.konfiguration.getAsString("gui.quicklinks." + gruppeId + ".text", null);

                        if (text == null) {
                            // Fallback Gruppen ID verwenden
                            quicklinksGruppeModel.setAnzuzeigenderGruppenname(gruppeId);
                        } else {
                            quicklinksGruppeModel.setAnzuzeigenderGruppenname(text);
                        }
                    }
                    // Variable immer in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
                    // eine
                    // Aktualisierung sehen.
                    sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<ApplikationseiteModel> getMaskenModelKlasseZuController() {
        return ApplikationseiteModel.class;
    }

    /**
     * Fügt einen Quicklink hinzu.
     *
     * @param quicklinkselementModel
     *            der Quicklink
     * @param gruppeId
     *            Die Gruppen ID der Quicklinks (Oder der Headername, falls ohne Konfiguration gearbeitet
     *            wird.)
     * @param maxAnzahl
     *            Die maximale Anzahl der anzuzeigenden Elemente.
     * @return QuicklinkselementModel
     */
    public QuicklinksElementModel fuegeQuicklinkHinzu(QuicklinksElementModel quicklinkselementModel,
        String gruppeId, int maxAnzahl) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel == null) {
                quicklinksModel = new QuicklinksModel();
                sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
            }

            // Wenn der Quicklink breits vorhanden ist, dann wird er aktualisiert und nach oben gesetzt
            entferneQuicklink(quicklinkselementModel.getId());

            // Quicklink immer am Anfang einfügen
            QuicklinksElementModel quicklinkselementModelGeloescht = addQuicklinkselementModelAtTheBeginning(
                quicklinksModel, quicklinkselementModel, gruppeId, maxAnzahl);

            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine
            // Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);

            return quicklinkselementModelGeloescht;
        }

    }

    /**
     * Entfernt einen Quicklink, der eine bestimmte ID besitzt. Diese Funktion iteriert über ALLE möglichen
     * Quicklinksgruppen.
     *
     * @param id
     *            die ID
     */
    public void entferneQuicklink(String id) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel == null) {
                return;
            }

            for (QuicklinksGruppeModel gruppe : quicklinksModel.getQuicklinksGruppen().values()) {
                QuicklinksElementModel zuLoschendesElement = null;
                for (QuicklinksElementModel element : gruppe.getQuicklinksElemente()) {
                    if (element.getId().equals(id)) {
                        zuLoschendesElement = element;
                        break; // IDs innerhalb einer Gruppe zwingend eindeutig
                    }
                }
                if (zuLoschendesElement != null) {
                    gruppe.getQuicklinksElemente().remove(zuLoschendesElement);
                }
            }
            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        }

    }

    /**
     * Entfernt einen Quicklink, der eine bestimmte ID besitzt.
     *
     * @param gruppeId
     *            Die ID der Gruppe.
     * @param id
     *            die ID.
     */
    public void entferneQuicklink(String gruppeId, String id) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel == null) {
                return;
            }

            QuicklinksGruppeModel gruppe = quicklinksModel.getQuicklinksGruppen().get(gruppeId);

            if (gruppe == null) {
                return;
            }

            QuicklinksElementModel zuLoschendesElement = null;
            for (QuicklinksElementModel element : gruppe.getQuicklinksElemente()) {
                if (element.getId().equals(id)) {
                    zuLoschendesElement = element;
                    break; // IDs innerhalb einer Gruppe zwingend eindeutig
                }

            }

            if (zuLoschendesElement != null) {
                gruppe.getQuicklinksElemente().remove(zuLoschendesElement);
            }

            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine
            // Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);

        }

    }

    /**
     * Verschiebt alle Quicklink-Elemente von einer Quicklink-Liste zu einer Anderen.
     *
     * @param vonGruppeId
     *            Die Gruppen ID der ursprünglichen Quicklinks-Liste (alternativ die Header-Bezeichnung)
     * @param nachGruppeId
     *            Die Gruppen ID der Quicklinks-Liste in welche die Element verschoben werden soll.
     *            (alternativ die Header-Bezeichnung)
     * @param maxAnzahl
     *            Die maximale Anzahl der anzuzeigenden Elemente.
     */
    public void verschiebeQuicklinks(String vonGruppeId, String nachGruppeId, int maxAnzahl) {

        // Variable aus der Session holen
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();

        synchronized (sessionMap.getMutex()) {
            QuicklinksModel quicklinksModel = (QuicklinksModel) sessionMap.get(SESSION_KEY_QUICKLINKS);

            if (quicklinksModel != null && quicklinksModel.getQuicklinksGruppen().get(vonGruppeId) != null) {
                List<QuicklinksElementModel> elemente =
                    quicklinksModel.getQuicklinksGruppen().get(vonGruppeId).getQuicklinksElemente();
                ListIterator<QuicklinksElementModel> iterator = elemente.listIterator(elemente.size());

                List<QuicklinksElementModel> elementeVerschieben = new ArrayList<>();
                while (iterator.hasPrevious()) {
                    elementeVerschieben.add(iterator.previous());
                }

                for (QuicklinksElementModel model : elementeVerschieben) {
                    entferneQuicklink(model.getId());
                    fuegeQuicklinkHinzu(model, nachGruppeId, maxAnzahl);
                }

            }
            // Variable immer wieder in Session schreiben, damit übergeordnete Sessionmanager auf jeden Fall
            // eine
            // Aktualisierung sehen.
            sessionMap.put(SESSION_KEY_QUICKLINKS, quicklinksModel);
        }

    }

    /**
     * Fügt ein neues QuicklinkselementModel am Anfang hinzu.
     *
     * @param quicklinksModel
     *            Das QuicklinksModel.
     * @param quicklinkselementModel
     *            Das neue QuicklinksElementModel.
     * @param gruppeId
     *            Die ID der Gruppe.
     * @param maxAnzahl
     *            Die maximale Anzahl an Quicklinks in der Gruppe.
     *
     * @return quicklinkselementModelGeloescht.
     */
    private QuicklinksElementModel addQuicklinkselementModelAtTheBeginning(QuicklinksModel quicklinksModel,
        QuicklinksElementModel quicklinkselementModel, String gruppeId, int maxAnzahl) {

        QuicklinksGruppeModel quicklinksGruppeModel = quicklinksModel.getQuicklinksGruppen().get(gruppeId);

        if (quicklinksGruppeModel == null) {
            // Null, falls keine Konfiguration existiert. Fallback: Gruppe über die Gruppen ID erzeugen, damit
            // ist auch der Fallback zu früheren Versionen der isy-web gewährleistet (gruppeId = headerName).
            quicklinksGruppeModel = new QuicklinksGruppeModel();
            quicklinksGruppeModel.setGruppeId(gruppeId);
            quicklinksGruppeModel.setAnzuzeigenderGruppenname(gruppeId);
            quicklinksGruppeModel.setSichtbar(true);
            quicklinksModel.getQuicklinksGruppen().put(gruppeId, quicklinksGruppeModel);
        }

        quicklinksGruppeModel.getQuicklinksElemente().add(0, quicklinkselementModel);

        return limitiereAnzahl(maxAnzahl, quicklinksGruppeModel);

    }

    /**
     * Limitiert die Anzahl an Quicklinks in einer Gruppe.
     * @param maxAnzahl
     *            Die maximale Anzahl.
     * @param quicklinksGruppeModel
     *            Die Gruppe.
     *
     * @return quicklinkselementModelGeloescht.
     */
    private QuicklinksElementModel limitiereAnzahl(int maxAnzahl,
        QuicklinksGruppeModel quicklinksGruppeModel) {
        QuicklinksElementModel quicklinkselementModelGeloescht = null;
        if (quicklinksGruppeModel.getQuicklinksElemente().size() > maxAnzahl) {
            quicklinkselementModelGeloescht = quicklinksGruppeModel.getQuicklinksElemente()
                .get(quicklinksGruppeModel.getQuicklinksElemente().size() - 1);
            quicklinksGruppeModel.getQuicklinksElemente()
                .remove(quicklinksGruppeModel.getQuicklinksElemente().size() - 1);
        }

        return quicklinkselementModelGeloescht;
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
