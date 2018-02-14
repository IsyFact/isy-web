package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.pliscommon.aufrufkontext.AufrufKontext;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 *
 * Wird für das Auslesen des NavigationMenu über ein eine .properties-Datei verwendet.
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public class NavigationAusleseHelperProp extends NavigationAusleseHelperImpl {
    private Konfiguration konfiguration;

    /**
     * Der AufrufKontextVerwalter
     */
    private AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter;

    @Override
    public NavigationMenuModel bildeNavigationMenu() {
        String beschreibung;
        String appWert;
        String appLink;
        String appRolle;
        String appIcon;
        String farbe;
        Integer reihenfolge;
        String anwWert;
        String anwLink;
        String anwRolle;
        int anwReihenfolge;
        Applikation applikation;

        Set<String> allKeys = this.konfiguration.getSchluessel();
        ArrayList<Integer> appnumbers = getAnzahlApps(allKeys);

        String[] userRollen = this.aufrufKontextVerwalter.getAufrufKontext().getRolle();
        List<Applikation> appListe = new ArrayList<>();
        for (int i : appnumbers) {
            appRolle = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.ROLLE, "");

            List<Anwendung> anwendungen = new ArrayList<>();
            ArrayList<Integer> anwNumbers = getAnzahlAnwendungen(allKeys, i);
            for (int j : anwNumbers) {
                anwRolle = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.ANWENDUNG + Integer.toString(j)
                    + AbstractNavigationMenuPropSchluessel.ROLLE, "");
                if (userHasRight(userRollen, anwRolle.split(","))) {
                    anwWert =
                        this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.ANWENDUNG
                            + Integer.toString(j) + AbstractNavigationMenuPropSchluessel.WERT, "");
                    anwLink =
                        this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.ANWENDUNG
                            + Integer.toString(j) + AbstractNavigationMenuPropSchluessel.LINK, "");
                    anwReihenfolge = this.konfiguration.getAsInteger(
                        AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION + Integer.toString(i)
                            + AbstractNavigationMenuPropSchluessel.ANWENDUNG + Integer.toString(j)
                            + AbstractNavigationMenuPropSchluessel.REIHENFOLGE,
                        AbstractNavigationMenuPropSchluessel.DEFAULT_REIHENFOLGE);
                    anwendungen.add(new Anwendung(anwWert, anwLink, anwRolle, anwReihenfolge));
                }
            }

            if (userHasRight(userRollen, appRolle.split(",")) || anwendungen.size() > 0) {
                appWert = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.WERT, "");
                beschreibung =
                    this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                        + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.BESCHREIBUNG, "");
                appLink = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.LINK, "");
                appIcon = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.ICON, "");
                farbe = this.konfiguration.getAsString(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + AbstractNavigationMenuPropSchluessel.FARBE,
                    AbstractNavigationMenuPropSchluessel.DEFAULT_FARBE);
                reihenfolge = this.konfiguration.getAsInteger(
                    AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION + Integer.toString(i)
                        + AbstractNavigationMenuPropSchluessel.REIHENFOLGE,
                    AbstractNavigationMenuPropSchluessel.DEFAULT_REIHENFOLGE);

                anwendungen = (sortAnwendungen(anwendungen));
                applikation = new Applikation(appWert, beschreibung, appLink, appIcon, farbe, appRolle, false,
                    reihenfolge, anwendungen);
                appListe.add(applikation);
            }
        }

        appListe = sortApps(appListe);
        return new NavigationMenuModel(appListe);

    }

    /**
     * @param allKeys
     * @param i
     * @return
     */
    private ArrayList<Integer> getAnzahlAnwendungen(Set<String> allKeys, int appnumber) {
        int keyNumber;
        ArrayList<Integer> anwNumbers = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION
                + Integer.toString(appnumber) + AbstractNavigationMenuPropSchluessel.ANWENDUNG)) {
                keyNumber = Integer.parseInt(key.split("\\.")[5]);
                if (!anwNumbers.contains(keyNumber))
                    anwNumbers.add(keyNumber);
            }
        }
        return anwNumbers;
    }

    private ArrayList<Integer> getAnzahlApps(Set<String> allKeys) {

        int keyNumber;
        ArrayList<Integer> appNumbers = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(AbstractNavigationMenuPropSchluessel.GUI_NAVBAR_APPLIKATION)) {
                keyNumber = Integer.parseInt(key.split("\\.")[3]);
                if (!appNumbers.contains(keyNumber))
                    appNumbers.add(keyNumber);
            }
        }
        return appNumbers;
    }

    @Required
    public void setAufrufKontextVerwalter(AufrufKontextVerwalter<AufrufKontext> aufrufKontextverwalter) {
        this.aufrufKontextVerwalter = aufrufKontextverwalter;
    }

    @Required
    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }
}
