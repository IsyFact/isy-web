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

/**
 * Ein globales Model, welches für jeden Flow erzeugt wird und im Flow-Scope liegt.
 *
 * @author Capgemini, Andreas Hörning.
 * @version $Id: GlobalFlowModel.java 142517 2015-07-23 10:35:01Z sdm_ahoerning $
 */
public class GlobalFlowModel extends AbstractMaskenModel {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die Fehlermeldungen bei AJAX-Fehlern.
     */
    private String ajaxErrorMessage;

    /**
     * Der Titel der Fehlermeldung bei AJAX-Fehlern.
     */
    private String ajaxErrorMessageTitle;

    /**
     * Pfad zum ResourcesBundle des aktuellen Flows.
     */
    private String pathToResourcesBundleCurrentFlow;

    /**
     * Der Name des Sachbearbeiters.
     */
    private String sachbearbeiterName;

    /**
     * Angabe, ob beim Laden des aktuellen Views ein bestimmtes Element des Inhaltsbereichs fokussiert werden
     * soll.
     */
    private boolean focusOnloadActive;

    /**
     * Angabe ob ein Multipart Form verwendet werden soll. Ein Multipart sollte nur bei Dateiuploads verwendet
     * werden. In diesem Falle sollte kein AJAX verwendet werden, da der JSF AJAX Client kein Fehlerhandling
     * bei Multipart Requests durchführen kann (Stand JSF 2.2.11) und die Anwendung daher bei z.B. einem
     * Verbindungsfehler zum Server blockiert und nicht mehr verwendet werden kann.
     */
    private boolean multipartForm;

    public String getAjaxErrorMessage() {
        return this.ajaxErrorMessage;
    }

    public void setAjaxErrorMessage(String ajaxErrorMessage) {
        this.ajaxErrorMessage = ajaxErrorMessage;
    }

    public String getAjaxErrorMessageTitle() {
        return this.ajaxErrorMessageTitle;
    }

    public void setAjaxErrorMessageTitle(String ajaxErrorMessageTitle) {
        this.ajaxErrorMessageTitle = ajaxErrorMessageTitle;
    }

    public String getPathToResourcesBundleCurrentFlow() {
        return this.pathToResourcesBundleCurrentFlow;
    }

    public void setPathToResourcesBundleCurrentFlow(String pathToResourcesBundleCurrentFlow) {
        this.pathToResourcesBundleCurrentFlow = pathToResourcesBundleCurrentFlow;
    }

    public String getSachbearbeiterName() {
        return this.sachbearbeiterName;
    }

    public void setSachbearbeiterName(String sachbearbeiterName) {
        this.sachbearbeiterName = sachbearbeiterName;
    }

    public boolean isFocusOnloadActive() {
        return this.focusOnloadActive;
    }

    public void setFocusOnloadActive(boolean focusOnloadActive) {
        this.focusOnloadActive = focusOnloadActive;
    }

    public boolean isMultipartForm() {
        return this.multipartForm;
    }

    public void setMultipartForm(boolean multipartForm) {
        this.multipartForm = multipartForm;
    }

}
