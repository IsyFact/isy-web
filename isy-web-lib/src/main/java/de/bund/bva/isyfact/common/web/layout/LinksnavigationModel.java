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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Model-Klasse fuer Maskenmodel vom Typ "Linksnavigation".
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class LinksnavigationModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Liste an Navigationselementen für eine Seite.
     */
    private List<LinksnavigationelementModel> linksnavigationelemente;

    /**
     * Die Überschrift der Linksnavigation.
     */
    private String headline;

    /**
     * Fügt ein neues LinksnavigationelementModel hinzu.
     * @param linksnavigationelementModel
     *            Das neue LinksnavigationelementModel
     */
    public void addLinksnavigationelementModel(LinksnavigationelementModel linksnavigationelementModel) {
        if (linksnavigationelemente == null) {
            linksnavigationelemente = new ArrayList<LinksnavigationelementModel>();
        }
        linksnavigationelemente.add(linksnavigationelementModel);
    }

    public List<LinksnavigationelementModel> getLinksnavigationelemente() {
        return linksnavigationelemente;
    }

    public void setLinksnavigationelemente(List<LinksnavigationelementModel> linksnavigationelemente) {
        this.linksnavigationelemente = linksnavigationelemente;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

}
