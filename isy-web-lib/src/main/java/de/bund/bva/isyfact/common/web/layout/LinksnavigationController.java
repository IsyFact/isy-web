package de.bund.bva.isyfact.common.web.layout;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.RequestContextHolder;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import de.bund.bva.isyfact.security.core.Berechtigungsmanager;

/**
 * Controller für die Linksnavigation.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LinksnavigationController extends AbstractGuiController<ApplikationseiteModel> {

    /**
     * Konfiguration.
     */
    private Konfiguration konfiguration;

    /**
     * Berechtigungsmanager.
     */
    private Berechtigungsmanager berechtigungsmanager;

    @Autowired
    public LinksnavigationController(Konfiguration konfiguration, Berechtigungsmanager berechtigungsmanager) {
        this.konfiguration = konfiguration;
        this.berechtigungsmanager = berechtigungsmanager;
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

        // Auslesen der konfigurierten Linksnavigationen.
        List<String> linksnavigationenNamen =
            Lists.newArrayList(splitter.split(this.konfiguration.getAsString("gui.linksnavigation.ids")));

        boolean linksnavigationModelGefunden = false;

        for (String linksnavigationName : linksnavigationenNamen) {

            LinksnavigationModel linksnavigationModel = new LinksnavigationModel();

            // Überschrift der Linksnavigation
            String headline =
                this.konfiguration.getAsString("gui.linksnavigation." + linksnavigationName + ".headline",
                    null);
            linksnavigationModel.setHeadline(headline);

            String anzuzeigenderText;
            String link;
            String rollen;

            int i = 1;

            LinksnavigationelementModel linksnavigationelementModel = null;

            do {
                anzuzeigenderText =
                    this.konfiguration.getAsString("gui.linksnavigation." + linksnavigationName + "." + i
                        + ".text", null);
                link =
                    this.konfiguration.getAsString("gui.linksnavigation." + linksnavigationName + "." + i
                        + ".link", null);
                rollen =
                    this.konfiguration.getAsString("gui.linksnavigation." + linksnavigationName + "." + i
                        + ".rollen", null);

                if (Strings.isNullOrEmpty(rollen) || istBerechtigt(Arrays.asList(rollen.split(",")))) {

                    if (anzuzeigenderText != null && link != null) {
                        linksnavigationelementModel = new LinksnavigationelementModel();
                        linksnavigationelementModel.setAnzuzeigenderText(anzuzeigenderText);
                        linksnavigationelementModel.setLink(link);

                        // Der aktuelle Flow wird als aktiv in der Linksnavigation konfiguriert
                        if (link.equals(flowName)) {
                            linksnavigationelementModel.setActive(true);
                            linksnavigationModelGefunden = true;
                        }

                        linksnavigationModel.addLinksnavigationelementModel(linksnavigationelementModel);
                    }
                }

                i++;
            } while (anzuzeigenderText != null && link != null);

            // Der aktuelle Flow wurde in der konfigurierten Linksnavigation gefunden
            if (linksnavigationModelGefunden) {
                model.setLinksnavigationModel(linksnavigationModel);

                break;
            }
        }
    }

    /**
     * Überschreibt die Linksnavigation, die über die Initialisierungsmethode gesetzt wurde, mit einer
     * anderen.
     * @param model
     *            das Model
     * @param id
     *            die Id der zu verwendenden Linksnavigation
     */
    public void ueberschreibeLinksnavigation(ApplikationseiteModel model, String id) {

        // Der aktuelle Flow. Wenn ein Subflow aufgerufen wurde, dann wird hier der ursprüngliche Flow
        // ausgelesen.
        FlowDefinition flowDefinition =
            RequestContextHolder.getRequestContext().getFlowExecutionContext().getDefinition();

        String flowName = flowDefinition.getId();

        LinksnavigationModel linksnavigationModel = new LinksnavigationModel();

        // Überschrift der Linksnavigation
        String headline = this.konfiguration.getAsString("gui.linksnavigation." + id + ".headline", null);
        linksnavigationModel.setHeadline(headline);

        String anzuzeigenderText;
        String link;
        String rollen;

        int i = 1;

        LinksnavigationelementModel linksnavigationelementModel = null;

        do {
            anzuzeigenderText =
                this.konfiguration.getAsString("gui.linksnavigation." + id + "." + i + ".text", null);
            link = this.konfiguration.getAsString("gui.linksnavigation." + id + "." + i + ".link", null);
            rollen = this.konfiguration.getAsString("gui.linksnavigation." + id + "." + i + ".rollen", null);

            if (Strings.isNullOrEmpty(rollen) || istBerechtigt(Arrays.asList(rollen.split(",")))) {

                if (anzuzeigenderText != null && link != null) {
                    linksnavigationelementModel = new LinksnavigationelementModel();
                    linksnavigationelementModel.setAnzuzeigenderText(anzuzeigenderText);
                    linksnavigationelementModel.setLink(link);

                    // Der aktuelle Flow wird als aktiv in der Linksnavigation konfiguriert
                    if (link.equals(flowName)) {
                        linksnavigationelementModel.setActive(true);
                    }

                    linksnavigationModel.addLinksnavigationelementModel(linksnavigationelementModel);
                }
            }
            i++;
        } while (anzuzeigenderText != null && link != null);

        model.setLinksnavigationModel(linksnavigationModel);
    }

    /**
     * Entfernt die Linksnavigation, die über die Initialisierungsmethode gesetzt wurde.
     * @param model
     *            das Model
     */
    public void entferneLinksnavigation(ApplikationseiteModel model) {
        model.setLinksnavigationModel(null);
    }

    /**
     * Ueberprueft, ob aktueller Nutzer eine der benoetigten Rollen besitzt.
     * @param rollen
     *            Liste mit den erlaubten Rollen.
     * @return ob aktueller Nutzer berechtigt.
     */
    private boolean istBerechtigt(List<String> rollen) {

        for (String rolle : berechtigungsmanager.getRollen()) {
            if (rollen.contains(rolle)) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<ApplikationseiteModel> getMaskenModelKlasseZuController() {
        return ApplikationseiteModel.class;
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }


}
