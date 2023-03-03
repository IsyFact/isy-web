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

/**
 * Ist das Model für die Toolbar.
 *
 * @author Capgemini
 * @version $Id: SeitentoolbarModel.java 124705 2014-11-06 12:05:12Z sdm_ahoerning $
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SeitentoolbarModel extends AbstractSeitenelementModel {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ob der Zurück-Button angezeigt werden soll oder nicht.
     */
    private boolean zeigeZurueckButton;
    
    /**
     * Bestimmt, ob die mittlere Buttongruppe in der Seitentoolbar angezeigt wird.
     * Wenn nicht, nimmt die rechte Buttongruppe den freigewordenen Platz ein.   
     */
    private boolean zeigeMittlereButtongruppe = true;

    public boolean isZeigeZurueckButton() {
        return this.zeigeZurueckButton;
    }

    public void setZeigeZurueckButton(boolean zeigeZurueckButton) {
        this.zeigeZurueckButton = zeigeZurueckButton;
    }
    
    public boolean isZeigeMittlereButtongruppe() {
        return this.zeigeMittlereButtongruppe;
    }
    
    public void setZeigeMittlereButtongruppe(boolean zeigeMittlereButtongruppe) {
        this.zeigeMittlereButtongruppe = zeigeMittlereButtongruppe;
    }

}
