package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.webflow.context.ExternalContextHolder;
import org.springframework.webflow.core.collection.SharedAttributeMap;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonstanten;

/**
 * Der NavigationMenuController wird verwendet um das NavigationMenu zu befüllen und gegebenenfalls auch zu
 * ändern.
 *
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public class NavigationMenuController {

    /**
     * Ein NavigationAusleseHelper
     */
    private NavigationAusleseHelper navigationAusleseHelper;

    /**
     * Initialisiert das {@link NavigationMenuModel}.
     *
     * @param model
     *            Das {@link NavigationMenuModel}.
     *
     */
    public void initialisiereModel(NavigationMenuModel model) {
        if (model == null) {
            model = this.navigationAusleseHelper.bildeNavigationMenu();
            SharedAttributeMap<Object> sessionMap =
                ExternalContextHolder.getExternalContext().getSessionMap();
            synchronized (sessionMap.getMutex()) {
                sessionMap.put(NavigationMenuKonstanten.SESSION_KEY_NAVIGATION_MENU, model);
            }
        }
    }

    @Required
    public void setNavigationAusleseHelper(NavigationAusleseHelper navigationAusleseHelper) {
        this.navigationAusleseHelper = navigationAusleseHelper;
    }
}