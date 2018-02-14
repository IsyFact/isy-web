package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */

/**
 * Enthält alle Einträge und Infos um ein Navigationsmenü zu erstellen
 */
public class NavigationMenuModel implements Serializable {

    /**
     * Die SerialVersionUID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Liste von Applikationen
     */
    private List<Applikation> applikationsListe;

    /**
     * Konstruktor
     */
    public NavigationMenuModel() {
        this.applikationsListe = new ArrayList<>();
    }

    /**
     * Konstruktor
     *
     * @param appListe
     *            Enthält eine vorsortierte Liste der Applikationen
     */
    public NavigationMenuModel(List<Applikation> appListe) {
        this.applikationsListe = appListe;
    }

    public List<Applikation> getApplikationsListe() {
        return this.applikationsListe;
    }

    public void setApplikationsListe(List<Applikation> applikationsListe) {
        this.applikationsListe = applikationsListe;
    }

}