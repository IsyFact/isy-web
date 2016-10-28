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
package de.bund.bva.isyfact.common.web.global;

/**
 * Dient der Ermittlung der Webbrowser-Version.
 * 
 * @author Capgemini, Jonas Zitz
 * @version $Id: WebBrowserVersion.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
public enum WebBrowserVersion {

    /**
     * IE kleiner 7.
     */
    INTERNET_EXPLORER_VERSION_KLEINER_7("no-js lt-ie9 lt-i8 lt-ie7"),

    /**
     * IE 7.
     */
    INTERNET_EXPLORER_VERSION_7("no-js lt-ie9 lt-i8"),

    /**
     * IE 8.
     */
    INTERNET_EXPLORER_VERSION_8("no-js lt-ie9"),

    /**
     * Sonstige.
     */
    SONSTIGER_WEBBROWSER("no-js");

    /**
     * Die HTML-Tag Klassen.
     */
    private String htmlTagCssKlassen;

    /**
     * Konstruktor.
     * 
     * @param htmlTagCssKlassen
     *            Die einzubindenden HTML-Tag Klassen.
     */
    private WebBrowserVersion(String htmlTagCssKlassen) {
        this.htmlTagCssKlassen = htmlTagCssKlassen;
    }

    public String getHtmlTagCssKlassen() {
        return htmlTagCssKlassen;
    }

    /**
     * Bestimmt, ob die InternetExplorer-Version kleiner oder gleich IE8 ist.
     * @return <code>true</code>, falls die InternetExplorer-Version kleiner oder gleich IE8 ist.
     *         <code>false</code> ansonsten.
     */
    public boolean isInternetExplorerVersionKleinerGleich8() {
        boolean result =
            INTERNET_EXPLORER_VERSION_KLEINER_7.equals(this) || INTERNET_EXPLORER_VERSION_7.equals(this)
                || INTERNET_EXPLORER_VERSION_8.equals(this);
        return result;
    }
}
