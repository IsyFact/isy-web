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
package de.bund.bva.isyfact.common.web.exception.web;

import java.io.Serializable;

/**
 * Model für den Error-View.
 * 
 * @author Capgemini
 * @version $Id: ErrorModel.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
public class ErrorModel implements Serializable {

    /**
     * Die Serial-Version-UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Der Fehlertext.
     */
    private String fehlerText;

    /**
     * Die Fehlerüberschrift.
     */
    private String fehlerUeberschrift;

    public String getFehlerText() {
        return fehlerText;
    }

    public void setFehlerText(String fehlerText) {
        this.fehlerText = fehlerText;
    }

    public String getFehlerUeberschrift() {
        return fehlerUeberschrift;
    }

    public void setFehlerUeberschrift(String fehlerUeberschrift) {
        this.fehlerUeberschrift = fehlerUeberschrift;
    }

}
