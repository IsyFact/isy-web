package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;

/**
 * Interface zum Generieren des {@link NavigationMenuModel}.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface NavigationMenuGenerierenStrategie {
    /**
     * Generiert ein {@link NavigationMenuModel}. Woher die dafür benötigten Daten stammen, hängt von der
     * jeweiligen Implementierung ab.
     * @return Das {@link NavigationMenuModel}.
     */
    public NavigationMenuModel generiereNavigationMenu();
}
