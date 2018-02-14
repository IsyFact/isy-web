package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

/**
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public interface NavigationAusleseHelper {
    /**
     * Liest aus der Konfiguration alle für das Menü benötigten Daten und gibt ein NavigationMenuModel zurück,
     * welches all diese Daten hält.
     *
     * @return Das NavigationMenuModel
     * 
     *
     */
    public NavigationMenuModel bildeNavigationMenu();
}
