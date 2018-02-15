package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikation;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.AbstractNavigationMenuGenerierenStrategie;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuPropertySchluesselKonstanten;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontext;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Generiert das {@link NavigationMenuModel} anhand der Konfiguration der Anwendung. Eine entsprechende
 * Konfigurations-Datei muss von der Anwendung bereitgestellt werden.
 */
public class NavigationMenuGenerierenAusKonfiguration extends AbstractNavigationMenuGenerierenStrategie {

    /**
     * Bean: Zugriff auf die {@link Konfiguration}.
     */
    private Konfiguration konfiguration;

    /**
     * Bean: Der AufrufKontextVerwalter.
     */
    private AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter;

    @Override
    public NavigationMenuModel generiereNavigationMenu() {
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
            appRolle = this.konfiguration
                .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                    + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ROLLE, "");

            List<Anwendung> anwendungen = new ArrayList<>();
            ArrayList<Integer> anwNumbers = getAnzahlAnwendungen(allKeys, i);
            for (int j : anwNumbers) {
                anwRolle = this.konfiguration
                    .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                        + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ANWENDUNG
                        + Integer.toString(j) + NavigationMenuPropertySchluesselKonstanten.ROLLE, "");
                if (userHasRight(userRollen, anwRolle.split(","))) {
                    anwWert = this.konfiguration
                        .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuPropertySchluesselKonstanten.WERT, "");
                    anwLink = this.konfiguration
                        .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuPropertySchluesselKonstanten.LINK, "");
                    anwReihenfolge = this.konfiguration.getAsInteger(
                        NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuPropertySchluesselKonstanten.REIHENFOLGE,
                        NavigationMenuPropertySchluesselKonstanten.DEFAULT_REIHENFOLGE);
                    anwendungen.add(new Anwendung(anwWert, anwLink, anwRolle, anwReihenfolge));
                }
            }

            if (userHasRight(userRollen, appRolle.split(",")) || anwendungen.size() > 0) {
                appWert = this.konfiguration
                    .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                        + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.WERT, "");
                beschreibung =
                    this.konfiguration.getAsString(
                        NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                            + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.BESCHREIBUNG,
                        "");
                appLink = this.konfiguration
                    .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                        + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.LINK, "");
                appIcon = this.konfiguration
                    .getAsString(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                        + Integer.toString(i) + NavigationMenuPropertySchluesselKonstanten.ICON, "");
                farbe = this.konfiguration.getAsString(
                    NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION + Integer.toString(i)
                        + NavigationMenuPropertySchluesselKonstanten.FARBE,
                    NavigationMenuPropertySchluesselKonstanten.DEFAULT_FARBE);
                reihenfolge = this.konfiguration.getAsInteger(
                    NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION + Integer.toString(i)
                        + NavigationMenuPropertySchluesselKonstanten.REIHENFOLGE,
                    NavigationMenuPropertySchluesselKonstanten.DEFAULT_REIHENFOLGE);

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
            if (key.startsWith(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION
                + Integer.toString(appnumber) + NavigationMenuPropertySchluesselKonstanten.ANWENDUNG)) {
                keyNumber = Integer.parseInt(key.split("\\.")[5]);
                if (!anwNumbers.contains(keyNumber)) {
                    anwNumbers.add(keyNumber);
                }
            }
        }
        return anwNumbers;
    }

    private ArrayList<Integer> getAnzahlApps(Set<String> allKeys) {

        int keyNumber;
        ArrayList<Integer> appNumbers = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(NavigationMenuPropertySchluesselKonstanten.GUI_NAVBAR_APPLIKATION)) {
                keyNumber = Integer.parseInt(key.split("\\.")[3]);
                if (!appNumbers.contains(keyNumber)) {
                    appNumbers.add(keyNumber);
                }
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
