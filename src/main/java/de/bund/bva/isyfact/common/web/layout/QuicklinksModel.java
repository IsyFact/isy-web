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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Model für Quicklinks.
 * @author Capgemini, Tobias Gröger
 */
public class QuicklinksModel implements Serializable {

    /**
     * Die Serial-Version-UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die Quicklinks.
     */
    private Map<String, QuicklinksGroup> quicklinkGruppen = new LinkedHashMap<String, QuicklinksGroup>();

    /**
     * Erzeuge eine Quicklink Gruppe mit bestimmen Title und Group ID.
     *
     * @param groupId
     *            ID der Gruppe
     * @param title
     *            der Title (header)
     *
     * @return erzeugte Gruppe
     */
    public QuicklinksGroup erzeugeGruppe(String groupId, String title) {
        if (getGruppe(title) == null) {
            QuicklinksGroup group = new QuicklinksGroup();
            group.setName(title);
            group.setId(groupId);
            this.quicklinkGruppen.put(groupId, group);

        }
        return getGruppe(groupId);
    }

    /**
     * Fügt ein neues QuicklinksElementModel am Anfang hinzu.
     * @param quicklinksElementModel
     *            Das neue QuicklinksElementModel
     * @param groupId
     *            Der anzuzeigende header.
     * @param title
     *            nach group ID
     * 
     * @return gelöschte Element
     */
    public QuicklinksElementModel quicklinkAmAnfangHinzufuegen(QuicklinksElementModel quicklinksElementModel,
        String groupId, String title) {

        QuicklinksGroup group = getGruppe(groupId);

        if (group == null) {
            group = new QuicklinksGroup();
            group.setName(title);
            group.setId(groupId);
            this.quicklinkGruppen.put(groupId, group);
        }

        return group.elementAmAnfangHinzufuegen(quicklinksElementModel);

    }

    /**
     * Liefert eine Quicklink Gruppe mit bestimmter ID.
     *
     * @param gruppeId
     *            ID der Gruppe
     *
     * @return Quicklink Gruppe
     */
    public QuicklinksGroup getGruppe(String gruppeId) {
        return this.quicklinkGruppen.get(gruppeId);
    }

    /**
     * Liefert alle Quicklink Gruppen zurück.
     *
     * @return alle Quicklink Gruppen.
     */
    public Collection<QuicklinksGroup> getAlleGruppen() {
        return new ArrayList<QuicklinksGroup>(this.quicklinkGruppen.values());
    }

    /**
     * Liefert die Information ob bestimme Quicklink Gruppe existiert oder nicht.
     *
     * @param gruppeId
     *            ID der Gruppe
     *
     * @return true wenn existiert
     */
    public boolean hatGruppe(String gruppeId) {
        return getGruppe(gruppeId) != null;
    }
}
