package de.bund.bva.isyfact.common.web.layout;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Quicklinks Gruppe.
 */
public class QuicklinksGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private boolean sichtbar = true;

    private int maxAnzahlElemente;

    private List<QuicklinksElementModel> elemente = new LinkedList<QuicklinksElementModel>();

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSichtbar() {
        return this.sichtbar;
    }

    public void setSichtbar(boolean sichtbar) {
        this.sichtbar = sichtbar;
    }

    public List<QuicklinksElementModel> getElemente() {
        return new ArrayList<QuicklinksElementModel>(this.elemente);
    }

    public void setElemente(List<QuicklinksElementModel> elements) {
        this.elemente = elements;
    }

    public void elementLoeschen(QuicklinksElementModel element) {
        this.elemente.remove(element);
    }

    public void elementHinzufuegen(QuicklinksElementModel element) {
        maxAnzahlElementeGewahrleisten();
        this.elemente.add(element);
    }

    public QuicklinksElementModel elementAmAnfangHinzufuegen(QuicklinksElementModel element) {
        QuicklinksElementModel elem = maxAnzahlElementeGewahrleisten();
        this.elemente.add(0, element);
        return elem;
    }

    private QuicklinksElementModel maxAnzahlElementeGewahrleisten() {
        QuicklinksElementModel elem = null;
        if (this.elemente.size() > 0 && this.elemente.size() >= this.maxAnzahlElemente) {
            elem = this.elemente.remove(this.elemente.size() - 1);
        }
        return elem;
    }

    public int getMaxAnzahlElemente() {
        return this.maxAnzahlElemente;
    }

    public void setMaxAnzahlElemente(int maxAnzahlDerElemente) {
        this.maxAnzahlElemente = maxAnzahlDerElemente;
    }

    public int groesse() {
        return this.elemente.size();
    }

    public boolean istLeer() {
        return groesse() <= 0;
    }

    public boolean istNichtLeer() {
        return !istLeer();
    }

    public void leeren() {
        this.elemente.clear();
    }
}
