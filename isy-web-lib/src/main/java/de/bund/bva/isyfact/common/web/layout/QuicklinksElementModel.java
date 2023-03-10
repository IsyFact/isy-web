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

/**
 * Modell für ein Quicklinkselement.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class QuicklinksElementModel implements Serializable {

    /**
     * ID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Die ID eines Quicklinks.
     */
    private String id;

    /**
     * Der Text der angezeigt wird.
     */
    private String anzuzeigenderText;

    /**
     * Der dahinterliegende Link.
     */
    private String link;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnzuzeigenderText() {
        return this.anzuzeigenderText;
    }

    public void setAnzuzeigenderText(String anzuzeigenderText) {
        this.anzuzeigenderText = anzuzeigenderText;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
