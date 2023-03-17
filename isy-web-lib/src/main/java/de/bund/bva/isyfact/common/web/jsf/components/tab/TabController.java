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
package de.bund.bva.isyfact.common.web.jsf.components.tab;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

import com.google.common.base.Strings;

import de.bund.bva.isyfact.common.web.GuiController;

/**
 * Ein Kontroller, welcher für die Steuerung der rf:tabGroup und rf:tab Tags zuständig ist.
 *
 * @author Capgemini
 * @version $Id: TabController.java 144628 2015-08-12 13:56:14Z sdm_ahoerning $
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class TabController implements GuiController {

    /**
     * Die Facete des Tab-Headers.
     */
    private static final String TAB_HEADER_FACET = "tabHeader";

    /**
     * Das Attribut des Tab-Headers, welcher den Titel beinhaltet.
     */
    private static final String TAB_HEADER_TITLE_ATTRIUBTE = "value";

    /**
     * Wechselt das Tab.
     *
     * @param tabGroupModel
     *            Das Tab Model.
     * @param newTab
     *            Das neue Tab.
     */
    public void changeTab(TabGroupModel tabGroupModel, String newTab) {
        tabGroupModel.setCurrentTab(newTab);
    }

    /**
     * Überprüft ob der angegene Tab momentan aktiv ist.
     *
     * @param tabGroupModel
     *            Das Model.
     * @param name
     *            Der Name des zu überprüfenden Tabs.
     *
     * @return Ob der Tab aktiv ist (<code>true</code>). Ansonsten <code>false</code>.
     */
    public boolean isCurrentlyActive(TabGroupModel tabGroupModel, String name) {
        return name.equals(tabGroupModel.getCurrentTab());
    }

    /**
     * Gibt den Tab-Titel auf Basis der aktuellen Tab-Content-Komponente sowie dem Tabnamen zurück.
     *
     * @param component
     *            Die Tab-Content-Komponente.
     * @param tabName
     *            Der Tab-Name.
     * @return Der Tab-Titel.
     */
    public String retrieveTabTitle(UIComponent component, String tabName) {

        // Ermittle Tab Group (Annahme: Der nächste UI-Naming-Container ist die Tab-Group)
        UIComponent tabGroup = component.getParent();
        while (!(tabGroup instanceof UINamingContainer) && tabGroup != null) {
            tabGroup = tabGroup.getParent();
        }

        // Ermittle Header Facet
        UIComponent facet = tabGroup.getFacet(TAB_HEADER_FACET);

        // Ermittle konkretes Header-Element
        for (UIComponent facetChild : facet.getChildren()) {

            for (UINamingContainer facetChildNamingContainer : //
            searchFirstAvailableNamingContainersInTree(facetChild)) {
                String name = (String) facetChildNamingContainer.getAttributes().get("name");

                if (!Strings.isNullOrEmpty(name) && name.equals(tabName)) {
                    return (String) facetChildNamingContainer.getAttributes().get(TAB_HEADER_TITLE_ATTRIUBTE);
                }
            }

        }

        return null;

    }

    /**
     * Sucht den erst möglichen NamingContainer in jedem Blatt einer Baumstruktur.
     *
     * @param parent
     *            Der Parent.
     * @return Die Liste an gefundenen {@link UINamingContainer}
     */
    private List<UINamingContainer> searchFirstAvailableNamingContainersInTree(UIComponent parent) {

        List<UINamingContainer> result = new ArrayList<UINamingContainer>();

        if (parent instanceof UINamingContainer) {
            result.add((UINamingContainer) parent);

        } else {

            if (!(parent.getChildren() == null || parent.getChildren().isEmpty())) {
                for (UIComponent child : parent.getChildren()) {
                    result.addAll(searchFirstAvailableNamingContainersInTree(child));
                }
            }
        }
        return result;
    }

}
