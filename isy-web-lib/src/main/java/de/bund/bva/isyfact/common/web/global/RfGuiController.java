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
