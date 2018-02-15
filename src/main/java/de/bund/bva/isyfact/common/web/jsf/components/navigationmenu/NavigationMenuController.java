package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;

/**
 * Die Klasse dient dazu ein {@link NavigationMenuModel} zu erstellen und in der Session abzulegen. Außerdem
 * wird die aktive {@link Applikation} hier ermittelt.
 */
public class NavigationMenuController {

    /**
     * Bean: Ein {@link NavigationAusleseHelper}.
     */
    private NavigationAusleseHelper navigationAusleseHelper;

    /**
     * Erstellt ein {@link NavigationMenuModel} mithilfe von
     * {@link NavigationAusleseHelper#bildeNavigationMenu()} und legt es in der Session ab. Wenn in der
     * Session bereits ein initialisiertes {@link NavigationMenuModel} vorhanden ist, entfällt der Aufruf des
     * {@link NavigationAusleseHelper}s. Schließlich wird die aktive Applikation ermittelt. Dies findet in
     * jedem Fall statt.
     */
    public void initialisiereNavigationMenue() {
        SharedAttributeMap<Object> sessionMap = ExternalContextHolder.getExternalContext().getSessionMap();
        synchronized (sessionMap.getMutex()) {
            NavigationMenuModel model =
                (NavigationMenuModel) sessionMap.get(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU);
            if (model == null) {
                model = this.navigationAusleseHelper.bildeNavigationMenu();
            }
            ermittleAktiveApplikation(model);
            sessionMap.put(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU, model);
        }
    }

    /**
     * Ermittelt die aktive {@link Applikation} anhand des aktuellen Flows.
     * @param model
     *            Das {@link NavigationMenuModel}, das alle {@link Applikation}en enthält.
     */
    private void ermittleAktiveApplikation(NavigationMenuModel model) {
        // Der aktuelle Flow. Wenn ein Subflow aufgerufen wurde, dann wird hier der ursprüngliche Flow
        // ausgelesen.
        FlowDefinition flowDefinition =
            RequestContextHolder.getRequestContext().getFlowExecutionContext().getDefinition();

        String flowName = flowDefinition.getId();
        boolean treffer = false;
        if (model != null) {
            for (Applikation app : model.getApplikationsListe()) {
                app.setAktiv(false);
                if (!treffer && flowName.contains(app.getLink())) {
                    app.setAktiv(true);
                    treffer = true;
                }
                if (!treffer) {
                    for (Anwendung anw : app.getAnwendungen()) {
                        if (!treffer && flowName.contains(anw.getLink())) {
                            app.setAktiv(true);
                            treffer = true;
                        }
                    }
                }
            }
        }
    }

    @Required
    public void setNavigationAusleseHelper(NavigationAusleseHelper navigationAusleseHelper) {
        this.navigationAusleseHelper = navigationAusleseHelper;
    }
}