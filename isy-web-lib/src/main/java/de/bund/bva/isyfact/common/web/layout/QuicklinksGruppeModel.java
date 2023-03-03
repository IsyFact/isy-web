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
 * Definiert die Gruppe von Quicklinks.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class QuicklinksGruppeModel implements Serializable {

    /**
     * Die Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die ID der Gruppe.
     */
    private String gruppeId;

    /**
     * Der lesbare Gruppenname.
     */
    private String anzuzeigenderGruppenname;

    /**
     * Ob die Gruppe sichtbar ist oder nicht.
     */
    private boolean sichtbar;

    /**
     * Die Elemente der Gruppe.
     */
    private List<QuicklinksElementModel> quicklinksElemente = new ArrayList<>();

    public String getGruppeId() {
        return this.gruppeId;
    }

    public void setGruppeId(String gruppeId) {
        this.gruppeId = gruppeId;
    }

    public List<QuicklinksElementModel> getQuicklinksElemente() {
        return this.quicklinksElemente;
    }

    public void setQuicklinksElemente(List<QuicklinksElementModel> quicklinksElemente) {
        this.quicklinksElemente = quicklinksElemente;
    }

    public String getAnzuzeigenderGruppenname() {
        return this.anzuzeigenderGruppenname;
    }

    public void setAnzuzeigenderGruppenname(String anzuzeigenderGruppenname) {
        this.anzuzeigenderGruppenname = anzuzeigenderGruppenname;
    }

    public boolean isSichtbar() {
        return this.sichtbar;
    }

    public void setSichtbar(boolean sichtbar) {
        this.sichtbar = sichtbar;
    }

}