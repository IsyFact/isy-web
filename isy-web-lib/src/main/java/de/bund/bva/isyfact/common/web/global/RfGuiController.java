package de.bund.bva.isyfact.common.web.global;

import de.bund.bva.isyfact.common.web.GuiController;

/**
 * Die Schnittstelle, welche alle Controller für eine Maske oder Teilbereich einer Maske realisieren müssen.
 *
 * @param <T>
 *            Das spezifische Maskemodel.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public interface RfGuiController<T extends AbstractMaskenModel> extends GuiController {

    /**
     * Initialisert das Model beim initialen Aufruf des Flows. Diese Methode darf nur einmal beim Start des
     * Flows (on-start) aufgerufen werden.
     *
     * @param model
     *            Das Maskenmodel.
     */
    public void initialisiereModel(T model);

}
