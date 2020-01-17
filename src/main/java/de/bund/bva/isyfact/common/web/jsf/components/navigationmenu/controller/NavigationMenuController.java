package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.NavigationMenuGenerierenStrategie;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;

/**
 * Die Klasse dient dazu ein {@link NavigationMenuModel} zu erstellen und in der Session abzulegen. Außerdem
 * wird die aktive {@link Applikationsgruppe} hier ermittelt.
 */
@Controller
public class NavigationMenuController {

    /**
     * Bean: Eine {@link NavigationMenuGenerierenStrategie}.
     */
    private NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie;

    @Autowired
    public NavigationMenuController(NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie) {
        this.navigationMenuGenerierenStrategie = navigationMenuGenerierenStrategie;
    }

    /**
     * Erstellt ein {@link NavigationMenuModel} mithilfe von
     * {@link NavigationMenuGenerierenStrategie#generiereNavigationMenu()} und legt es in der Session ab. Wenn
     * in der Session bereits ein initialisiertes {@link NavigationMenuModel} vorhanden ist, entfällt der
     * Aufruf von {@link NavigationMenuGenerierenStrategie}. Schließlich wird die aktive Applikationsgruppe
     * ermittelt. Dies findet in jedem Fall statt.
     */
    public void initialisiereNavigationMenue() {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();
        synchronized (sessionMap.getMutex()) {
            NavigationMenuModel model =
                (NavigationMenuModel) sessionMap.get(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU);
            if (model == null) {
                model = this.navigationMenuGenerierenStrategie.generiereNavigationMenu();
            }
            ermittleAktiveApplikationsgruppe(model);
            sessionMap.put(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU, model);
        }
    }

    /**
     * Ermittelt die aktive {@link Applikationsgruppe} anhand der aktuellen URL.
     * @param model
     *            Das {@link NavigationMenuModel}, das alle {@link Applikationsgruppe}en enthält.
     */
    private void ermittleAktiveApplikationsgruppe(NavigationMenuModel model) {
        if (model == null) {
            return;
        }

        String aktuelleRelativeUrl = ermittleAktuelleUrlMitKontextPfad();

        boolean treffer = false;
        for (Applikationsgruppe app : model.getApplikationsListe()) {
            app.setAktiv(false);
            if (!treffer && aktuelleRelativeUrl.equals(app.getLink())) {
                app.setAktiv(true);
                treffer = true;
            }
            if (!treffer) {
                for (Anwendung anw : app.getAnwendungen()) {
                    if (!treffer && aktuelleRelativeUrl.equals(anw.getLink())) {
                        app.setAktiv(true);
                        treffer = true;
                    }
                }
            }
        }
    }

    private String ermittleAktuelleUrlMitKontextPfad() {
        StringBuffer requestURL =
            ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getRequestURL();
        String contextPath =
            ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest())
                .getContextPath();
        return requestURL.substring(requestURL.indexOf(contextPath), requestURL.length());
    }

    public void setNavigationMenuGenerierenStrategie(
        NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie) {
        this.navigationMenuGenerierenStrategie = navigationMenuGenerierenStrategie;
    }
}