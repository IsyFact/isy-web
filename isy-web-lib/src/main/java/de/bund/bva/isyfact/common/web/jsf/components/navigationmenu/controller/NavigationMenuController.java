package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.controller;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.NavigationMenuGenerierenStrategie;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;

import java.util.Objects;

/**
 * The class is used to create a {@link NavigationMenuModel} and store it in the session. Also
 * the active {@link Applikationsgruppe} is determined here.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
public class NavigationMenuController {

    /**
     * Bean: One {@link NavigationMenuGenerierenStrategie}.
     */
    private NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie;

    /**
     * The last active applicationgroup
     */
    private Applikationsgruppe applikationsgruppeCache = null;

    @Autowired
    public NavigationMenuController(NavigationMenuGenerierenStrategie navigationMenuGenerierenStrategie) {
        this.navigationMenuGenerierenStrategie = navigationMenuGenerierenStrategie;
    }

    /**
     * Creates a {@link NavigationMenuModel} using
     * {@link NavigationMenuGenerierenStrategie#generiereNavigationMenu()} ()} and places it in the session. If
     * there is already an initialized {@link NavigationMenuModel} in the session, there is no need to
     * call {@link NavigationMenuGenerierenStrategie#generiereNavigationMenu()} ().
     * Finally, the active application group is determined. This takes place in each case.
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
     * Determines the active {@link Applikationsgruppe} based on the current URL.
     * @param model
     *      The {@link NavigationMenuModel} that contains all {@link Applikationsgruppe}n.
     */
    private void ermittleAktiveApplikationsgruppe(NavigationMenuModel model) {
        if (Objects.isNull(model)) {
            return;
        }

        String aktuelleRelativeUrl = ermittleAktuelleUrlMitKontextPfad();

        boolean treffer = false;
        for (Applikationsgruppe app : model.getApplikationsListe()) {
            app.setAktiv(false);
            if (!treffer && aktuelleRelativeUrl.equals(app.getLink())) {
                this.applikationsgruppeCache = app;
                app.setAktiv(true);
                treffer = true;
            }
            if (!treffer) {
                for (Anwendung anw : app.getAnwendungen()) {
                    if (!treffer && aktuelleRelativeUrl.equals(anw.getLink())) {
                        this.applikationsgruppeCache = app;
                        app.setAktiv(true);
                        treffer = true;
                    }
                }
            }
        }

        if (!treffer) {
            // When no active applicationgroup found, use the cached one and set it active
            if (Objects.nonNull(this.applikationsgruppeCache)) {
                this.applikationsgruppeCache.setAktiv(true);
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