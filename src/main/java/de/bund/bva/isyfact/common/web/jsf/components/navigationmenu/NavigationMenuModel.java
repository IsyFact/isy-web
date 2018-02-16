package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Enthält eine Liste von {@link Applikationsgruppe}n um ein Navigationsmenü zu darzustellen.
 */
public class NavigationMenuModel implements Serializable {

    /**
     * Die SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Liste von {@link Applikationsgruppe}en.
     */
    private List<Applikationsgruppe> applikationsListe;

    /**
     * Konstruktor.
     */
    public NavigationMenuModel() {
        this.applikationsListe = new ArrayList<>();
    }

    /**
     * Konstruktor, der die Liste der {@link Applikationsgruppe}en, direkt setzt.
     *
     * @param appListe
     *            Enthält eine vorsortierte Liste der {@link Applikationsgruppe}en.
     */
    public NavigationMenuModel(List<Applikationsgruppe> appListe) {
        this.applikationsListe = appListe;
    }

    public List<Applikationsgruppe> getApplikationsListe() {
        return this.applikationsListe;
    }

    public void setApplikationsListe(List<Applikationsgruppe> applikationsListe) {
        this.applikationsListe = applikationsListe;
    }

}