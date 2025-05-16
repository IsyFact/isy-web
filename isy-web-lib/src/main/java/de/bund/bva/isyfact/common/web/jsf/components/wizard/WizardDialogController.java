package de.bund.bva.isyfact.common.web.jsf.components.wizard;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.exception.IsyFactTechnicalRuntimeException;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;

/**
 * Ein Controller für den Wizard, welcher die Basisfunktionalität eines Wizards bereitstellt. Die einzelnen
 * Funktionen zum Blättern usw. können bei Bedarf überschrieben werden.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public abstract class WizardDialogController implements GuiController {

    /**
     * Initialisiert die Pages des Wizards mit dem Standardverhalten.
     *
     * @param model
     *            Das Model.
     */
    protected void initializeDefaultPages(WizardDialogModel model) {

        if (model.getWizardDialogPages() == null || model.getWizardDialogPages().size() == 0) {
            throw new IsyFactTechnicalRuntimeException(
                FehlerSchluessel.ALLGEMEIN_KOMPONENTE_FALSCH_KONFIGURIERT,
                "Es sind keine WizardDialogPages vorhanden.");
        }

        for (int i = 0; i < model.getWizardDialogPages().size(); i++) {

            WizardDialogPage page = model.getWizardDialogPages().get(i);

            // (1) Die erste Seite hat keinen "vorher" Button.
            if (i == 0) {
                page.setButtonPreviousActivated(false);
            } else {
                page.setButtonPreviousActivated(true);
            }

            // (2) Die letzte Seite hat keinen "weiter" Button.
            if ((i + 1) == model.getWizardDialogPages().size()) {
                page.setButtonNextActivated(false);
            } else {
                page.setButtonNextActivated(true);
            }

            // (3) Die letzte Seite hat einen "abschließen" Button
            if ((i + 1) == model.getWizardDialogPages().size()) {
                page.setButtonCompleteActivated(true);
            } else {
                page.setButtonCompleteActivated(false);
            }

            // (4) Die erste Seite ist initial aktiv
            if (i == 0) {
                model.setActiveWizardDialogPageId(page.getWizardDialogPageId());
            }

            // (5) Alle Seiten können abgebrochen werden
            page.setButtonAbortActivated(true);

        }
    }

    /**
     * Rufe nächste Seite auf.
     *
     * @param model
     *            Das Model.
     *
     * @return Ob der Übergang durchgeführt werden kann (true) oder nicht (false).
     */
    public boolean next(WizardDialogModel model) {

        for (int i = 0; i < model.getWizardDialogPages().size(); i++) {

            WizardDialogPage page = model.getWizardDialogPages().get(i);

            if (page.getWizardDialogPageId().equals(model.getActiveWizardDialogPageId())) {

                // Nächste Seite auswählen
                if ((i + 2) > model.getWizardDialogPages().size()) {
                    throw new IsyFactTechnicalRuntimeException(
                        FehlerSchluessel.ALLGEMEIN_KOMPONENTE_FALSCH_KONFIGURIERT,
                        "Diese Operation kann nicht auf der letzten Seite ausgeführt werden.");
                }
                model.setNextActiveWizardDialogPageId(
                    model.getWizardDialogPages().get(i + 1).getWizardDialogPageId());

                break;

            }
        }

        return true;

    }

    /**
     * Rufe vorherige Seite auf.
     *
     * @param model
     *            Das Model.
     *
     * @return Ob der Übergang durchgeführt werden kann (true) oder nicht (false).
     */
    public boolean previous(WizardDialogModel model) {

        for (int i = 0; i < model.getWizardDialogPages().size(); i++) {

            WizardDialogPage page = model.getWizardDialogPages().get(i);

            if (page.getWizardDialogPageId().equals(model.getActiveWizardDialogPageId())) {

                // Vorherige Seite auswählen
                if (i == 0) {
                    throw new IsyFactTechnicalRuntimeException(
                        FehlerSchluessel.ALLGEMEIN_KOMPONENTE_FALSCH_KONFIGURIERT,
                        "Diese Operation kann nicht auf der ersten Seite ausgeführt werden.");
                }
                model.setNextActiveWizardDialogPageId(
                    model.getWizardDialogPages().get(i - 1).getWizardDialogPageId());

                break;

            }
        }

        return true;

    }

    /**
     * Bricht die Verarbeitung ab.
     *
     * @param model
     *            Das Model.
     *
     * @return Ob der Übergang durchgeführt werden kann (true) oder nicht (false).
     */
    public abstract boolean cancel(WizardDialogModel model);

    /**
     * Beendet die Verarbeitung des Wizards.
     *
     * @param model
     *            Das Model.
     * @return <code>true</code>, falls die Verarbeitung erfolgreich war, <code>false</code> wenn nicht
     *         (Wizard Dialog soll nicht geschlossen werden, es sollgen ggf. Fehler angzeigt werden).
     */
    public abstract boolean finish(WizardDialogModel model);

}
