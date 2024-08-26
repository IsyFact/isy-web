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
 * Ist das Model zum Informationsbereich.
 * 
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
// TODO: Das Informationsbereich-Model sollte in XHTML ausgelagert werden (Überschriften usw. sollten nicht in
// JAVA-Code gehalten werden)
public class InformationsbereichModel extends AbstractSeitenelementModel {

    /**
     * Id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der Icon Typ für das Informationsicon.
     */
    private IconTyp iconTyp;

    /**
     * Die Überschrift.
     */
    private String ueberschrift;

    /**
     * Die 2. Überschrift.
     */
    private String ueberschrift_2;

    /**
     * Der Inhalt.
     */
    private String inhalt;

    public IconTyp getIconTyp() {
        return iconTyp;
    }

    public void setIconTyp(IconTyp iconTyp) {
        this.iconTyp = iconTyp;
    }

    public String getUeberschrift() {
        return ueberschrift;
    }

    public void setUeberschrift(String ueberschrift) {
        this.ueberschrift = ueberschrift;
    }

    public String getUeberschrift_2() {
        return ueberschrift_2;
    }

    public void setUeberschrift_2(String ueberschrift_2) {
        this.ueberschrift_2 = ueberschrift_2;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

}
