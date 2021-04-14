package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;

/**
 * Abstrakte Oberklasse, von der geerbt werden sollte, um {@link NavigationMenuGenerierenStrategie} zu
 * implementieren.
 */
public abstract class AbstractNavigationMenuGenerierenStrategie implements NavigationMenuGenerierenStrategie {

    /**
     * Ermittelt ob der Benutzer, die Berechtigung hat, eine {@link Applikationsgruppe} bzw. eine
     * {@link Anwendung} zu sehen.
     * @param userRollen
     *            Rollen die der User besitzt.
     * @param erlaubteRollen
     *            Rollen, die die {@link Applikationsgruppe} bzw. {@link Anwendung} voraussetzen.
     * @return {@code true}, falls der User berechtigt ist. Sonst {@code false}.
     *
     */
    protected boolean isUserBerechtigt(String[] userRollen, String[] erlaubteRollen) {
        if (erlaubteRollen[0].equals("") && erlaubteRollen.length == 1) {
            return true;
        }
        for (String userRolle : userRollen) {
            for (String erlaubteRolle : erlaubteRollen) {
                if (userRolle.trim().equals(erlaubteRolle.trim())) {
                    return true;
                }
            }
        }
        return false;
    }
}
