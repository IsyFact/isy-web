package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.generieren.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
        Set<String> alleSchluessel = this.konfiguration.getSchluessel();
        Set<Integer> idsApplikationsgruppen = ermittleIdsApplikationsgruppen(alleSchluessel);
        List<Applikationsgruppe> applikationsgruppen = new ArrayList<>();
        String[] userRollen = this.aufrufKontextVerwalter.getAufrufKontext().getRolle();
        for (int i : idsApplikationsgruppen) {
            // Lies zuerst die Anwendungen. Eine Applikationsgruppe muss jedoch nicht zwingend Anwendungen
            // haben.
            List<Anwendung> anwendungen = new ArrayList<>();
            Set<Integer> idsAnwendungen = ermittleIdsAnwendungen(alleSchluessel, i);
            for (int j : idsAnwendungen) {
                String[] anwendungRollen =
                    StringUtils.deleteWhitespace(this.konfiguration.getAsString(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.ROLLEN,
                        "")).split(",");
                if (isUserBerechtigt(userRollen, anwendungRollen)) {
                    String anwendungWert = this.konfiguration.getAsString(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.WERT,
                        "");
                    String anwendungLink = this.konfiguration.getAsString(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j) + NavigationMenuKonfigurationSchluesselKonstanten.LINK,
                        "");
                    int anwendungReihenfolge = this.konfiguration.getAsInteger(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG
                            + Integer.toString(j)
                            + NavigationMenuKonfigurationSchluesselKonstanten.REIHENFOLGE,
                        NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_REIHENFOLGE);
                    anwendungen.add(new Anwendung(anwendungWert, anwendungLink, anwendungReihenfolge));
                }
            }

            String[] applikationsgruppeRollen =
                StringUtils
                    .deleteWhitespace(this.konfiguration.getAsString(
                        NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                            + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.ROLLEN,
                        ""))
                    .split(",");
            if (isUserBerechtigt(userRollen, applikationsgruppeRollen) || anwendungen.size() > 0) {
                String applikationsgruppeWert =
                    this.konfiguration
                        .getAsString(
                            NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                                + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.WERT,
                            "");
                String applikationsgruppeLink =
                    this.konfiguration
                        .getAsString(
                            NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                                + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.LINK,
                            "");
                String applikationsgruppeFarbe = this.konfiguration.getAsString(
                    NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.FARBE,
                    NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_FARBE);
                int applikationsgruppeReihenfolge = this.konfiguration.getAsInteger(
                    NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                        + Integer.toString(i) + NavigationMenuKonfigurationSchluesselKonstanten.REIHENFOLGE,
                    NavigationMenuKonfigurationSchluesselKonstanten.DEFAULT_REIHENFOLGE);

                if (!isUserBerechtigt(userRollen, applikationsgruppeRollen)) {
                    // Der Benutzer ist eigentlich nicht berechtigt für diese Applikationsgruppe. Er darf
                    // aber mindestens eine Anwendung innerhalb der Gruppe sehen, deshalb muss die Gruppe
                    // grundsätzlich angezeigt werden. Der Link der Gruppe selbst wird geleert, damit er
                    // gar nicht erst im HTML gerendert wird.
                    applikationsgruppeLink = "";
                    LOG.info(LogKategorie.SICHERHEIT, EreignisSchluessel.E_NAVMENU_ROLLEN_IGNORE,
                        "Rollen für die Applikationsgruppe '{}' wurden aufgrund einer oder mehrerer Anwendungen innerhalb der Gruppe ignoriert.",
                        applikationsgruppeWert);
                }

                Collections.sort(anwendungen);
                Applikationsgruppe applikationsgruppe =
                    new Applikationsgruppe(applikationsgruppeWert, applikationsgruppeLink,
                        applikationsgruppeFarbe, applikationsgruppeReihenfolge, anwendungen);
                applikationsgruppen.add(applikationsgruppe);
            }
        }

        Collections.sort(applikationsgruppen);
        return new NavigationMenuModel(applikationsgruppen);

    }

    /**
     * Ermittelt die IDs der {@link Anwendung}en, die zu der {@link Applikationsgruppe} mit der übergebenen ID
     * gehören. Analog zu {@link #ermittleIdsApplikationsgruppen(Set)} wird durch alle
     * Konfigurations-Schlüssel iteriert, um robust gegen Lücken innerhalb der Folge von IDs zu sein.
     * @param alleKonfigurationsSchluessel
     *            Alle Schlüssel aus der {@link Konfiguration}.
     * @param idApplikationsgruppe
     *            ID der {@link Applikationsgruppe} für die die IDs der dazugehörigen {@link Anwendung}en
     *            ermittelt werden sollen.
     * @return Die Menge der IDs der {@link Anwendung}en, die zu der {@link Applikationsgruppe} mit der
     *         übergebenen ID gehören.
     */
    private Set<Integer> ermittleIdsAnwendungen(Set<String> alleKonfigurationsSchluessel,
        int idApplikationsgruppe) {
        Set<Integer> idsAnwendungen = new HashSet<>();
        for (String key : alleKonfigurationsSchluessel) {
            if (key.startsWith(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE
                + Integer.toString(idApplikationsgruppe)
                + NavigationMenuKonfigurationSchluesselKonstanten.ANWENDUNG)) {
                // Die ID der jeweiligen Anwendung befindet sich an der fünften Stelle.
                int idAnwendung = Integer.parseInt(key.split("\\.")[5]);
                idsAnwendungen.add(idAnwendung);
            }
        }
        return idsAnwendungen;
    }

    /**
     * Ermittelt die IDs aller {@link Applikationsgruppe}n. Hierzu werden alle Schlüssel aus der
     * {@link Konfiguration} iteriert und für jene mit dem Präfix
     * {@link NavigationMenuKonfigurationSchluesselKonstanten#GUI_NAVBAR_APPLIKATIONSGRUPPE} wird die ID in
     * die Ergebnismenge aufgenommen. Anmerkung: Das Vorgehen, durch alle Konfigurations-Schlüssel zu
     * iterieren und Schlüssel mit dem passenden Postfix zu untersuchen, wurde bewusst gewählt. So
     * funktioniert der Code auch, wenn die IDs der {@link Applikationsgruppe}n Lücken aufweisen.
     * @param alleKonfigurationsSchluessel
     *            Alle Schlüssel aus der {@link Konfiguration}.
     * @return Die Ergebnismenge, die die IDs aller konfigurierten {@link Applikationsgruppe}n enthält.
     */
    private Set<Integer> ermittleIdsApplikationsgruppen(Set<String> alleKonfigurationsSchluessel) {
        Set<Integer> idsApplikationsgruppen = new HashSet<>();
        for (String schluessel : alleKonfigurationsSchluessel) {
            if (schluessel
                .startsWith(NavigationMenuKonfigurationSchluesselKonstanten.GUI_NAVBAR_APPLIKATIONSGRUPPE)) {
                // Die ID befindet sich an der vierten Stelle.
                int idApplikationsgruppe = Integer.parseInt(schluessel.split("\\.")[3]);
                idsApplikationsgruppen.add(idApplikationsgruppe);
            }
        }
        return idsApplikationsgruppen;
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
