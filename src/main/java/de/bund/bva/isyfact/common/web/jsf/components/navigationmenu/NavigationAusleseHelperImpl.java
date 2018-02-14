package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public abstract class NavigationAusleseHelperImpl implements NavigationAusleseHelper {

    /**
     * Sortiert eine Liste von Anwendungen nach dem Wert den sie hat von klein nach groß.
     *
     * @param anwendungen
     *            Liste von Anwendungen
     * @return Sortierte Liste von Anwendungen
     */

    protected List<Anwendung> sortAnwendungen(List<Anwendung> anwendungen) {
        List<Anwendung> tempList = new ArrayList<>();
        for (Anwendung anwendung : anwendungen) {
            if (tempList.size() < 1) {
                tempList.add(anwendung);
            } else {
                for (Anwendung placedAnwendung : tempList) {
                    if (anwendung.getWert() <= placedAnwendung.getWert()) {
                        tempList.add(tempList.indexOf(placedAnwendung), anwendung);
                        break;
                    }
                    if (tempList.indexOf(placedAnwendung) == tempList.size() - 1) {
                        tempList.add(anwendung);
                        break;
                    }
                }

            }
        }
        return tempList;
    }

    /**
     * Sortiert eine Liste von Applikationen nach dem Wert den sie hat von klein nach groß.
     *
     * @param appListe
     *            Liste von Applikationen
     * @return Sortierte Liste von Applikationen
     */
    protected ArrayList<Applikation> sortApps(List<Applikation> appListe) {
        ArrayList<Applikation> tempListe = new ArrayList<>();
        for (Applikation applikation : appListe) {
            if (tempListe.size() < 1) {
                tempListe.add(applikation);
            } else {
                for (Applikation placedApplikation : tempListe) {
                    if (applikation.getWert() <= placedApplikation.getWert()) {
                        tempListe.add(tempListe.indexOf(placedApplikation), applikation);
                        break;
                    }
                    if (tempListe.indexOf(placedApplikation) == tempListe.size() - 1) {
                        tempListe.add(applikation);
                        break;
                    }
                }
            }
        }
        return tempListe;
    }

    /**
     * @param userRollen
     *            Rollen die der User besitzt
     * @param erlaubteRollen
     *            Rollen, die die App bzw. Anwendung benötigen
     * @return true= User besitzt eine Rolle die Zugriff hat false= User besitzt keine Rolle mit Zugriff
     *
     */
    protected boolean userHasRight(String[] userRollen, String[] erlaubteRollen) {
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
