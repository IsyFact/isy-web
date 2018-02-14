package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import org.springframework.beans.factory.annotation.Required;

/**
 * Hält ein NavigationMenuModel
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public class NavigationMenuModelHolder {

    /**
     * Ein NavigationMenuModel
     */
    private NavigationMenuModel navigationMenuModel;

    /**
     * Ein NavigationAusleseHelper
     */
    private NavigationAusleseHelper navigationAusleseHelper;

    /**
     * getter befüllt das Model einmalig falls noch nicht gesetzt
     * @return Das aktuelle NavigationMenuModel.
     */
    public NavigationMenuModel getNavigationMenuModel() {
        if (this.navigationMenuModel == null) {
            this.navigationMenuModel = this.navigationAusleseHelper.bildeNavigationMenu();
        }
        return this.navigationMenuModel;

    }

    public NavigationAusleseHelper getNavigationAusleseHelper() {
        return this.navigationAusleseHelper;
    }

    public void setNavigationMenuModel(NavigationMenuModel model) {
        this.navigationMenuModel = model;

    }

    @Required
    public void setNavigationAusleseHelper(NavigationAusleseHelper navigationAusleseHelper) {
        this.navigationAusleseHelper = navigationAusleseHelper;
    }

}
