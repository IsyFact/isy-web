package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren;

import java.util.ArrayList;
import java.util.List;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikation;

/**
 * Abstrakte Oberklasse, von der geerbt werden sollte, um {@link NavigationMenuGenerierenStrategie} zu
 * implementieren.
 */
public abstract class AbstractNavigationMenuGenerierenStrategie implements NavigationMenuGenerierenStrategie {

    /**
     * Sortiert eine Liste von Anwendungen nach dem Wert den sie hat von klein nach groß.
     *
     * @param anwendungen
     *            Liste von {@link Anwendung}en
     * @return Sortierte Liste von {@link Anwendung}en
     */
    protected List<Anwendung> sortAnwendungen(List<Anwendung> anwendungen) {
        List<Anwendung> sortierteListe = new ArrayList<>();
        for (Anwendung anwendung : anwendungen) {
            if (sortierteListe.size() < 1) {
                sortierteListe.add(anwendung);
            } else {
                for (Anwendung gesetzteAnwendung : sortierteListe) {
                    if (anwendung.getWert() <= gesetzteAnwendung.getWert()) {
                        sortierteListe.add(sortierteListe.indexOf(gesetzteAnwendung), anwendung);
                        break;
                    }
                    if (sortierteListe.indexOf(gesetzteAnwendung) == sortierteListe.size() - 1) {
                        sortierteListe.add(anwendung);
                        break;
                    }
                }

            }
        }
        return sortierteListe;
    }

    /**
     * Sortiert eine Liste von {@link Applikation}en nach dem Wert den sie hat von klein nach groß.
     *
     * @param appListe
     *            Liste von {@link Applikation}en
     * @return Sortierte Liste von {@link Applikation}en
     */
    protected ArrayList<Applikation> sortApps(List<Applikation> appListe) {
        ArrayList<Applikation> sortierteListe = new ArrayList<>();
        for (Applikation applikation : appListe) {
            if (sortierteListe.size() < 1) {
                sortierteListe.add(applikation);
            } else {
                for (Applikation gesetzteApplikation : sortierteListe) {
                    if (applikation.getWert() <= gesetzteApplikation.getWert()) {
                        sortierteListe.add(sortierteListe.indexOf(gesetzteApplikation), applikation);
                        break;
                    }
                    if (sortierteListe.indexOf(gesetzteApplikation) == sortierteListe.size() - 1) {
                        sortierteListe.add(applikation);
                        break;
                    }
                }
            }
        }
        return sortierteListe;
    }

    /**
     * @param userRollen
     *            Rollen die der User besitzt
     * @param erlaubteRollen
     *            Rollen, die die {@link Applikation} bzw. {@link Anwendung} benötigen
     * @return true= User besitzt eine Rolle die Zugriff hat false= User besitzt keine Rolle mit Zugriff
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
