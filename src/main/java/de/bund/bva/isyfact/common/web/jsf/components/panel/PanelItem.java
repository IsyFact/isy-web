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
package de.bund.bva.isyfact.common.web.jsf.components.panel;

/**
 * Ein Interface für Datenklassen. Dieses kann benutzt werden um sicherzustellen, dass alle benötigten Panel
 * Models auch für dynamisch Bedingte Daten (z.B. ein Panel pro Personalie) zur Verfügung steht.
 * 
 * @author Capgemini
 * @version $Id: PanelItem.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
public interface PanelItem {

    /**
     * Setzt das Panel Model.
     * @param panelModel
     *            Das Panel Model.
     */
    public void setPanelModel(PanelModel panelModel);

    /**
     * Holt das Panel Model.
     * @return Das Panel Model.
     */
    public PanelModel getPanelModel();

}
