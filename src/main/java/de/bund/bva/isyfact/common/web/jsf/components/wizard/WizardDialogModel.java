/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.jsf.components.wizard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Das Model für den Wizard Dialog.
 *
 * @author Capgemini, Andreas Hoerning
 * @version $Id: WizardDialogModel.java 136928 2015-05-18 07:04:43Z sdm_tgroeger $
 */
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
        return textButtonFinish;
    }

    public void setTextButtonFinish(String textButtonFinish) {
        this.textButtonFinish = textButtonFinish;
    }

}
