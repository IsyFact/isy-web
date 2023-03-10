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
package de.bund.bva.isyfact.common.web.layout;

import de.bund.bva.isyfact.common.web.global.AbstractMaskenModel;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Applikationseite".
 *
 * @author Capgemini
 * @version $Id: ApplikationseiteModel.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ApplikationseiteModel extends AbstractMaskenModel {

    // TODO Erweitern um gruppe aktiver quicklinks, bei leer default alle anzeigen

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Das LinksnavigationModel zur Applikationseite.
     */
    private LinksnavigationModel linksnavigationModel;

    public LinksnavigationModel getLinksnavigationModel() {
        return this.linksnavigationModel;
    }

    public void setLinksnavigationModel(LinksnavigationModel linksnavigationModel) {
        this.linksnavigationModel = linksnavigationModel;
    }
}
