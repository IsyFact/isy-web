package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;
import java.util.Objects;
import java.util.Stack;

/**
 * Modelklasse zur Darstellung eines einfachen Location-Breadcrumb.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class LocationBreadcrumbModel implements Serializable {

    private String objektname = "";

    private Stack<String> hierarchieebenen = new Stack<>();

    /**
     * Gibt die Hierarchiebenen in der Form
     * <p>
     *     Ebene 1 - Ebene 2 - ... - Ebene n-1 - Ebene n
     * </p>
     *
     * zurück.
     *
     * @return die Hierarchiebenen als String verkettet.
     */
    public String getHierarchieebene() {
        return String.join(" - ", hierarchieebenen);
    }

    /**
     * Der Objektname des Breadcrumbs.
     *
     * @return der Objektname
     */
    public String getObjektname() {
        return objektname;
    }

    /**
     * Setzt den Objektnamen des Breadcrumbs.
     *
     * @param objektname der Objektname
     */
    public void setObjektname(String objektname) {
        this.objektname = objektname;
    }

    /**
     * Fügt eine Hierarchiebene hinzu.
     *
     * @param ebene der Name der Hierarchiebene
     */
    public void pushHierarchiebene(String ebene) {
        if (!Objects.isNull(ebene) && !ebene.isEmpty()) {
            hierarchieebenen.push(ebene);
        }
    }

    /**
     * Entferne die oberste Hierarchiebene.
     *
     * @return die entfernte Hierarchiebene
     */
    public String popHierarchieebene() {
        return hierarchieebenen.pop();
    }

    /**
     * Entferne alle Hierarchieebenen.
     */
    public void clearHierarchieebenen() {
        hierarchieebenen.clear();
    }
}
