package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu.konstanten;

/**
 * Die Klasse enthält Konstanten, die beim Auslesen des Navigationsmenüs aus der Konfiguration verwendet
 * werden. Die Properties sind dabei wie folgt aufgebaut:
 *
 * Applikationsgruppe: gui.navbar.applikationsgruppe.<Integer>.<Eigenschaft>
 *
 * Anwendung: gui.navbar.applikationsgruppe.<Integer>.anwendung.<Eigenschaft>
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public abstract class NavigationMenuKonfigurationSchluesselKonstanten {

    /**
     * Start aller Einträge (gefolgt von einer Zahl darum ein Punkt am Ende).
     */
    public static final String GUI_NAVBAR_APPLIKATIONSGRUPPE = "gui.navbar.applikationsgruppe.";

    /**
     * Mitteltag für eine Anwendung (gefolgt von einer Zahl darum ein Punkt am Ende).
     */
    public static final String ANWENDUNG = ".anwendung.";

    /**
     * Postfix für eine Rolleneigenschaft.
     */
    public static final String ROLLEN = ".rollen";

    /**
     * Postfix für eine Linkeigenschaft.
     */
    public static final String LINK = ".link";

    /**
     * Postfix für eine Farbeneigenschaft.
     */
    public static final String FARBE = ".farbe";

    /**
     * Die Farbe, welche gewählt wird, falls keine Farbe gesetzt ist.
     */
    public static final String DEFAULT_FARBE = "#337299";

    /**
     * Postfix für eine Werteigenschaft.
     */
    public static final String WERT = ".wert";

    /**
     * Postfix für eine Reihenfolgeneigenschaft.
     */
    public static final String REIHENFOLGE = ".reihenfolge";

    /**
     * Der Reihenfolgenwert, welche gewählt wird, falls keine Reihenfolgenwert gesetzt ist.
     */
    public static final int DEFAULT_REIHENFOLGE = 1;
}
