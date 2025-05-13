package de.bund.bva.isyfact.common.web.jsf.components.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Das Model für den Wizard Dialog.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class WizardDialogModel implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die aktuell aktive Seite.
     */
    private String activeWizardDialogPageId;

    /**
     * Die ID der neuen Seite.
     */
    private String nextActiveWizardDialogPageId;

    /**
     * Die einzelnen Seiten des Wizards.
     */
    private List<WizardDialogPage> wizardDialogPages = new ArrayList<WizardDialogPage>();

    /**
     * Der Text, der im Finish-Button angezeigt werden soll. Wenn kein Text konfiguriert wird, dann wird ein
     * Standardtext angezeigt.
     */
    private String textButtonFinish;

    /**
     * Ob die Schritt-Anzeige angezeigt werden soll. Dies betrifft das komplette Segment, das die einzelnen
     * Schritte des Wizards anzeigt (Titel, Schrittnummer und grüner Balken darunter). Standardmäßig auf
     * {@code true}, so dass es aktiv ausgeschaltet werden muss, wenn die Schrittanzeige nicht erwünscht ist.
     */
    private boolean showWizardStepBar = true;

    /**
     * Ob die Seite mit der angegebenen ID derzeit angezeigt wird oder werden soll.
     * @param id
     *            Die ID.
     * @return <code>true</code> falls ja, <code>false</code> ansonsten.
     */
    public boolean isPageActive(String id) {
        return id.equals(this.activeWizardDialogPageId);
    }

    /**
     * Ob die Seite mit der angegebenen ID schon abgearbeitet wurde.
     * @param id
     *            Die ID.
     * @return <code>true</code> falls ja, <code>false</code> ansonsten.
     */
    public boolean isPageDone(String id) {

        for (WizardDialogPage page : this.wizardDialogPages) {
            if (page.getWizardDialogPageId().equals(id)) {
                return page.isPageDone();
            }
        }

        return false;

    }

    /**
     * Ob die Seite mit der angegebenen ID deaktiviert ist.
     * @param id
     *            Die ID.
     * @return <code>true</code> falls ja, <code>false</code> ansonsten.
     */
    public boolean isPageDisabled(String id) {

        for (WizardDialogPage page : this.wizardDialogPages) {
            if (page.getWizardDialogPageId().equals(id)) {
                return page.isPageDisabled();
            }
        }

        return false;

    }

    /**
     * Gibt die momentan aktive Seite zurück.
     * @return Die aktive Seite.
     */
    public WizardDialogPage getActiveWizardDialogPage() {
        for (WizardDialogPage page : this.wizardDialogPages) {
            if (page.getWizardDialogPageId().equals(this.activeWizardDialogPageId)) {
                return page;
            }
        }

        return null;
    }

    /**
     * Gibt die Seite zur angegebenen ID zurück.
     * @param wizardDialogPageId
     *            Die Id.
     * @return Die Seite.
     */
    public WizardDialogPage getWizardDialogPage(String wizardDialogPageId) {

        for (WizardDialogPage page : this.wizardDialogPages) {
            if (page.getWizardDialogPageId().equals(wizardDialogPageId)) {
                return page;
            }
        }

        return null;

    }

    public List<WizardDialogPage> getWizardDialogPages() {
        return this.wizardDialogPages;
    }

    public void setWizardDialogPages(List<WizardDialogPage> wizardDialogPages) {
        this.wizardDialogPages = wizardDialogPages;
    }

    public String getActiveWizardDialogPageId() {
        return this.activeWizardDialogPageId;
    }

    public void setActiveWizardDialogPageId(String activeWizardDialogPageId) {
        this.activeWizardDialogPageId = activeWizardDialogPageId;
    }

    public String getNextActiveWizardDialogPageId() {
        return this.nextActiveWizardDialogPageId;
    }

    public void setNextActiveWizardDialogPageId(String nextActiveWizardDialogPageId) {
        this.nextActiveWizardDialogPageId = nextActiveWizardDialogPageId;
    }

    public String getTextButtonFinish() {
        return this.textButtonFinish;
    }

    public void setTextButtonFinish(String textButtonFinish) {
        this.textButtonFinish = textButtonFinish;
    }

    public boolean isShowWizardStepBar() {
        return this.showWizardStepBar;
    }

    public void setShowWizardStepBar(boolean showWizardStepBar) {
        this.showWizardStepBar = showWizardStepBar;
    }

}
