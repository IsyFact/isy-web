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

/**
 * Stellt eine konkrete Seite im Wizard Dialog dar.
 *
 * @author Capgemini
 * @version $Id: WizardDialogPage.java 144104 2015-08-05 16:04:58Z sdm_ahoerning $
 */
public class WizardDialogPage implements Serializable {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Eine ID, welche die Wizardseite eindeutig innerhalb des Wizards identifiziert.
     */
    private String wizardDialogPageId;

    /**
     * Seite wurde abgearbeitet.
     */
    private boolean pageDone;

    /**
     * Seite wurde erfolgreich oder nicht erfolgreich abgearbeitet.
     */
    private boolean pageSuccessful;

    /**
     * Seite wurde deaktviert.
     */
    private boolean pageDisabled;

    /**
     * Ob der Button aktiviert ist oder nicht.
     */
    private boolean buttonPreviousActivated;

    /**
     * Ob der Button aktiviert ist oder nicht.
     */
    private boolean buttonAbortActivated;

    /**
     * Ob der Button aktiviert ist oder nicht.
     */
    private boolean buttonCompleteActivated;

    /**
     * Ob der Button aktiviert ist oder nicht.
     */
    private boolean buttonNextActivated;

    /**
     * Ob der Button sichtbar ist oder nicht.
     */
    private boolean buttonNextVisible = true;

    /**
     * Ob der Button sichtbar ist oder nicht.
     */
    private boolean buttonPreviousVisible = true;

    /**
     * Ob der Button sichtbar ist oder nicht.
     */
    private boolean buttonAbortVisible = true;

    /**
     * Ob der Button sichtbar ist oder nicht.
     */
    private boolean buttonCompleteVisible = true;

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private boolean buttonNextAjaxActivated = true;

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private boolean buttonPreviousAjaxActivated = true;

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private boolean buttonAbortAjaxActivated = true;

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private boolean buttonCompleteAjaxActivated = true;

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonNextAjaxAction = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonPreviousAjaxAction = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonAbortAjaxAction = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonCompleteAjaxAction = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonNextAjaxRender = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonPreviousAjaxRender = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonAbortAjaxRender = "@form";

    /**
     * Ob Ajax aktiviert ist oder nicht.
     */
    private String buttonCompleteAjaxRender = "@form";

    /**
     * Ob Ajax blockiert oder nicht.
     */
    private boolean buttonNextAjaxBlock;

    /**
     * Ob Ajax blockiert oder nicht.
     */
    private boolean buttonPreviousAjaxBlock;

    /**
     * Ob Ajax blockiert oder nicht.
     */
    private boolean buttonAbortAjaxBlock;

    /**
     * Ob Ajax blockiert oder nicht.
     */
    private boolean buttonCompleteAjaxBlock;

    /**
     * Title der Wizardseite.
     */
    private String title;

    /**
     * Konstruktor.
     *
     * @param wizardDialogPageId
     *            Die eindeutige ID der Seite.
     * @param title
     *            Titel der Seite.
     */
    public WizardDialogPage(String wizardDialogPageId, String title) {
        super();
        this.wizardDialogPageId = wizardDialogPageId;
        this.title = title;
    }

    public String getWizardDialogPageId() {
        return this.wizardDialogPageId;
    }

    public void setWizardDialogPageId(String wizardDialogPageId) {
        this.wizardDialogPageId = wizardDialogPageId;
    }

    public boolean isButtonPreviousActivated() {
        return this.buttonPreviousActivated;
    }

    public void setButtonPreviousActivated(boolean buttonPreviousActivated) {
        this.buttonPreviousActivated = buttonPreviousActivated;
    }

    public boolean isButtonAbortActivated() {
        return this.buttonAbortActivated;
    }

    public void setButtonAbortActivated(boolean buttonAbortActivated) {
        this.buttonAbortActivated = buttonAbortActivated;
    }

    public boolean isButtonCompleteActivated() {
        return this.buttonCompleteActivated;
    }

    public void setButtonCompleteActivated(boolean buttonCompleteActivated) {
        this.buttonCompleteActivated = buttonCompleteActivated;
    }

    public boolean isButtonNextActivated() {
        return this.buttonNextActivated;
    }

    public void setButtonNextActivated(boolean buttonNextActivated) {
        this.buttonNextActivated = buttonNextActivated;
    }

    public boolean isPageDone() {
        return this.pageDone;
    }

    public void setPageDone(boolean pageDone) {
        this.pageDone = pageDone;
    }

    public boolean isPageDisabled() {
        return this.pageDisabled;
    }

    public void setPageDisabled(boolean pageDisabled) {
        this.pageDisabled = pageDisabled;
    }

    public boolean isPageSuccessful() {
        return this.pageSuccessful;
    }

    public void setPageSuccessful(boolean pageSuccessful) {
        this.pageSuccessful = pageSuccessful;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isButtonNextAjaxActivated() {
        return this.buttonNextAjaxActivated;
    }

    public void setButtonNextAjaxActivated(boolean buttonNextAjaxActivated) {
        this.buttonNextAjaxActivated = buttonNextAjaxActivated;
    }

    public boolean isButtonPreviousAjaxActivated() {
        return this.buttonPreviousAjaxActivated;
    }

    public void setButtonPreviousAjaxActivated(boolean buttonPreviousAjaxActivated) {
        this.buttonPreviousAjaxActivated = buttonPreviousAjaxActivated;
    }

    public boolean isButtonAbortAjaxActivated() {
        return this.buttonAbortAjaxActivated;
    }

    public void setButtonAbortAjaxActivated(boolean buttonAbortAjaxActivated) {
        this.buttonAbortAjaxActivated = buttonAbortAjaxActivated;
    }

    public boolean isButtonCompleteAjaxActivated() {
        return this.buttonCompleteAjaxActivated;
    }

    public void setButtonCompleteAjaxActivated(boolean buttonCompleteAjaxActivated) {
        this.buttonCompleteAjaxActivated = buttonCompleteAjaxActivated;
    }

    public boolean isButtonNextVisible() {
        return this.buttonNextVisible;
    }

    public void setButtonNextVisible(boolean buttonNextVisible) {
        this.buttonNextVisible = buttonNextVisible;
    }

    public boolean isButtonPreviousVisible() {
        return this.buttonPreviousVisible;
    }

    public void setButtonPreviousVisible(boolean buttonPreviousVisible) {
        this.buttonPreviousVisible = buttonPreviousVisible;
    }

    public boolean isButtonAbortVisible() {
        return this.buttonAbortVisible;
    }

    public void setButtonAbortVisible(boolean buttonAbortVisible) {
        this.buttonAbortVisible = buttonAbortVisible;
    }

    public boolean isButtonCompleteVisible() {
        return this.buttonCompleteVisible;
    }

    public void setButtonCompleteVisible(boolean buttonCompleteVisible) {
        this.buttonCompleteVisible = buttonCompleteVisible;
    }

    public String getButtonNextAjaxAction() {
        return this.buttonNextAjaxAction;
    }

    public void setButtonNextAjaxAction(String buttonNextAjaxAction) {
        this.buttonNextAjaxAction = buttonNextAjaxAction;
    }

    public String getButtonPreviousAjaxAction() {
        return this.buttonPreviousAjaxAction;
    }

    public void setButtonPreviousAjaxAction(String buttonPreviousAjaxAction) {
        this.buttonPreviousAjaxAction = buttonPreviousAjaxAction;
    }

    public String getButtonAbortAjaxAction() {
        return this.buttonAbortAjaxAction;
    }

    public void setButtonAbortAjaxAction(String buttonAbortAjaxAction) {
        this.buttonAbortAjaxAction = buttonAbortAjaxAction;
    }

    public String getButtonCompleteAjaxAction() {
        return this.buttonCompleteAjaxAction;
    }

    public void setButtonCompleteAjaxAction(String buttonCompleteAjaxAction) {
        this.buttonCompleteAjaxAction = buttonCompleteAjaxAction;
    }

    public String getButtonNextAjaxRender() {
        return this.buttonNextAjaxRender;
    }

    public void setButtonNextAjaxRender(String buttonNextAjaxRender) {
        this.buttonNextAjaxRender = buttonNextAjaxRender;
    }

    public String getButtonPreviousAjaxRender() {
        return this.buttonPreviousAjaxRender;
    }

    public void setButtonPreviousAjaxRender(String buttonPreviousAjaxRender) {
        this.buttonPreviousAjaxRender = buttonPreviousAjaxRender;
    }

    public String getButtonAbortAjaxRender() {
        return this.buttonAbortAjaxRender;
    }

    public void setButtonAbortAjaxRender(String buttonAbortAjaxRender) {
        this.buttonAbortAjaxRender = buttonAbortAjaxRender;
    }

    public String getButtonCompleteAjaxRender() {
        return this.buttonCompleteAjaxRender;
    }

    public void setButtonCompleteAjaxRender(String buttonCompleteAjaxRender) {
        this.buttonCompleteAjaxRender = buttonCompleteAjaxRender;
    }

    public boolean isButtonNextAjaxBlock() {
        return this.buttonNextAjaxBlock;
    }

    public void setButtonNextAjaxBlock(boolean buttonNextAjaxBlock) {
        this.buttonNextAjaxBlock = buttonNextAjaxBlock;
    }

    public boolean isButtonPreviousAjaxBlock() {
        return this.buttonPreviousAjaxBlock;
    }

    public void setButtonPreviousAjaxBlock(boolean buttonPreviousAjaxBlock) {
        this.buttonPreviousAjaxBlock = buttonPreviousAjaxBlock;
    }

    public boolean isButtonAbortAjaxBlock() {
        return this.buttonAbortAjaxBlock;
    }

    public void setButtonAbortAjaxBlock(boolean buttonAbortAjaxBlock) {
        this.buttonAbortAjaxBlock = buttonAbortAjaxBlock;
    }

    public boolean isButtonCompleteAjaxBlock() {
        return this.buttonCompleteAjaxBlock;
    }

    public void setButtonCompleteAjaxBlock(boolean buttonCompleteAjaxBlock) {
        this.buttonCompleteAjaxBlock = buttonCompleteAjaxBlock;
    }

}
