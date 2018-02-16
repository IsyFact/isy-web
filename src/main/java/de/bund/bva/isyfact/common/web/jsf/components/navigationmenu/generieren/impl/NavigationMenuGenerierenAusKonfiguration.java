package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Anwendung;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.Applikationsgruppe;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.NavigationMenuModel;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.AbstractNavigationMenuGenerierenStrategie;
import de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten.NavigationMenuKonfigurationSchluesselKonstanten;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontext;
import de.bund.bva.pliscommon.aufrufkontext.AufrufKontextVerwalter;
import de.bund.bva.pliscommon.konfiguration.common.Konfiguration;

/**
 * Generiert das {@link NavigationMenuModel} anhand der Konfiguration der Anwendung. Eine entsprechende
 * Konfigurations-Datei muss von der Anwendung bereitgestellt werden.
 */
public class NavigationMenuGenerierenAusKonfiguration extends AbstractNavigationMenuGenerierenStrategie {

    /** Der Logger. */
    private static final IsyLogger LOG =
        IsyLoggerFactory.getLogger(NavigationMenuGenerierenAusKonfiguration.class);

    /**
     * Bean: Zugriff auf die {@link Konfiguration}.
     */
    private Konfiguration konfiguration;

    /**
     * Bean: Der AufrufKontextVerwalter.
     */
    private AufrufKontextVerwalter<AufrufKontext> aufrufKontextVerwalter;

    /**
     * Generiert ein {@link NavigationMenuModel}. Die benötigten Daten werden hierbei aus der
     * {@link Konfiguration} bezogen.
     */
    @Override
    public NavigationMenuModel generiereNavigationMenu() {
        String beschreibung;
        String appWert;
        String appLink;
        String appRolle;
        String appIcon;
        String farbe;
        int reihenfolge;
        String anwWert;
        String anwLink;
        String anwRolle;
        int anwReihenfolge;
        Applikationsgruppe applikation;

        // TODO IFS-99 Kann man das nicht eleganter lösen?
        Set<String> alleSchluessel = this.konfiguration.getSchluessel();
        ArrayList<Integer> appNummern = getAnzahlApps(alleSchluessel);

        String[] userRollen = this.aufrufKontextVerwalter.getAufrufKontext().getRolle();
        List<Applikationsgruppe> appListe = new ArrayList<>();
        for (int i : appNummern) {
            appRolle = this.konfiguration
                .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                    + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ROLLE, "");

            List<Anwendung> anwendungen = new ArrayList<>();
            ArrayList<Integer> anwNummern = getAnzahlAnwendungen(alleSchluessel, i);
            for (int j : anwNummern) {
                anwRolle = this.konfiguration
                    .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                        + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.ROLLE, "");
                if (isUserBerechtigt(userRollen, anwRolle.split(","))) {
                    anwWert = this.konfiguration
                        .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.WERT, "");
                    anwLink = this.konfiguration
                        .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.LINK, "");
                    anwReihenfolge = this.konfiguration.getAsInteger(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.REIHENFOLGE,
                        NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_REIHENFOLGE);
                    anwendungen.add(new Anwendung(anwWert, anwLink, anwRolle, anwReihenfolge));
                }
            }

            if (isUserBerechtigt(userRollen, appRolle.split(",")) || anwendungen.size() > 0) {
                if (!isUserBerechtigt(userRollen, appRolle.split(","))) {
                    LOG.info(LogKategorie.SICHERHEIT, EreignisSchluessel.E_NAVMENU_ROLLEN_IGNORE,
                        "Applikationsrollen wurden aufgrund von ein oder mehrerer Anwendungen ignoriert.");
                }

                appWert = this.konfiguration
                    .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.WERT, "");
                beschreibung =
                    this.konfiguration.getAsString(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.BESCHREIBUNG,
                        "");
                appLink = this.konfiguration
                    .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.LINK, "");
                appIcon = this.konfiguration
                    .getAsString(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ICON, "");
                farbe = this.konfiguration.getAsString(
                    NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE + Integer.toString(i)
                        + NavigationMenuKonfigurationSchluesselKonstanten.FARBE,
                    NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_FARBE);
                reihenfolge = this.konfiguration.getAsInteger(
                    NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE + Integer.toString(i)
                        + NavigationMenuKonfigurationSchluesselKonstanten.REIHENFOLGE,
                    NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_REIHENFOLGE);

                Collections.sort(anwendungen);
                applikation = new Applikationsgruppe(appWert, beschreibung, appLink, appIcon, farbe, appRolle,
                    reihenfolge, anwendungen);
                appListe.add(applikation);
            }
        }

        Collections.sort(appListe);
        return new NavigationMenuModel(appListe);

    }

    /**
     * TODO IFS-99 Dokumentieren.
     * @param allKeys
     *            Alle Schlüssel aus der {@link Konfiguration}
     * @param appnumber
     *            Nummer der {@link Applikationsgruppe} für die die {@link Anwendung}en gezählt werden.
     * @return Gibt eine Liste der {@link Anwendung}snummern für {@link Applikationsgruppe} mit der Zahl
     *         appnumber
     */
    private ArrayList<Integer> getAnzahlAnwendungen(Set<String> allKeys, int appnumber) {
        int keyNumber;
        ArrayList<Integer> anwNumbers = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                + Integer.toString(appnumber) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG)) {
                keyNumber = Integer.parseInt(key.split("\\.")[5]);
                if (!anwNumbers.contains(keyNumber)) {
                    anwNumbers.add(keyNumber);
                }
            }
        }
        return anwNumbers;
    }

    /**
     * TODO IFS-99 Dokumentieren.
     * @param allKeys
     *            Alle Schlüssel aus der {@link Konfiguration}
     * @return Gibt eine Liste der {@link Applikationsgruppe}snummern zurück welche in der
     *         {@link Konfiguration} geladen werden.
     */
    private ArrayList<Integer> getAnzahlApps(Set<String> allKeys) {

        int keyNumber;
        ArrayList<Integer> appNumbers = new ArrayList<>();
        for (String key : allKeys) {
            if (key.startsWith(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE)) {
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
