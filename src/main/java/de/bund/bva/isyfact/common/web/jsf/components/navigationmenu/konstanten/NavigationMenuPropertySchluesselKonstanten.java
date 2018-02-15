package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten;

/**
 * Die Klasse enthält Konstanten um das NavigationMenu aus den Properties zu lesen Die Properties sind dabei
 * wie folgt aufgebaut:
 *
 * Applikation: gui.navbar.applikation.<Integer>.<Eigenschaft>
 *
 * Anwendung: gui.navbar.applikation.<Integer>.anwendung.<Eigenschaft>
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public abstract class NavigationMenuPropertySchluesselKonstanten {

    /**
     * Start aller Einträge (gefolgt von einer Zahl darum ein Punkt am Ende)
     */
    public static final String GUI_NAVBAR_APPLIKATION = "gui.navbar.applikation.";

    /**
     * Mitteltag für eine anwendung (gefolgt von einer Zahl darum ein Punkt am Ende)
     */
    public static final String ANWENDUNG = ".anwendung.";

    /**
     * Ende für einen Rolleneigenschaft
     */
    public static final String ROLLE = ".rolle";

    /**
     * Ende für einen Linkeigenschaft
     */
    public static final String LINK = ".link";

    /**
     * Ende für einen Farbeneigenschaft
     */
    public static final String FARBE = ".farbe";

    /**
     * Die Farbe, welche gewählt wird, falls keine Farbe gesetzt ist.
     */
    public static final String DEFAULT_FARBE = "#337299";

    /**
     * Ende für einen Iconeigenschaft
     */
    public static final String ICON = ".icon";

    /**
     * Ende für einen Werteigenschaft
     */
    public static final String WERT = ".wert";

    /**
     * Ende für einen Reihenfolgeneigenschaft
     */
    public static final String REIHENFOLGE = ".reihenfolge";

    /**
     * Der Reihenfolgenwert, welche gewählt wird, falls keine Reihenfolgenwert gesetzt ist.
     */
    public static final int DEFAULT_REIHENFOLGE = 1;

    /**
     * Ende für einen Beschreibungseigenschaft
     */
    public static final String BESCHREIBUNG = ".beschreibung";
}
