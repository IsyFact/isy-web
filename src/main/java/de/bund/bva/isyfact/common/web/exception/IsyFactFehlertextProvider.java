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
package de.bund.bva.isyfact.common.web.exception;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import de.bund.bva.isyfact.exception.FehlertextProvider;

/**
 * Diese Klasse stellt Methoden zum Auslesen von Fehlertexten auf Basis von AusnahmeIDs und Parametern zur
 * Verfügung.
 *
 * @author Capgemini, Jonas Zitz
 * @version $Id: IsyFactFehlertextProvider.java 130620 2015-02-17 08:49:16Z sdm_tgroeger $
 */
public class IsyFactFehlertextProvider implements FehlertextProvider {

    /**
     * Das ResourceBoundle mit den Fehlertexten.
     */
    public static final ResourceBundle FEHLERTEXT_BUNDLE = ResourceBundle.getBundle(
        "resources/nachrichten/isyweb_fehler", Locale.getDefault());

    /**
     * Holt den Fehlertext zum Schlüssel.
     * @param schluessel
     *            der Schlüssel
     * @return der Fehlertext
     */
    public static String getMessage(String schluessel) {
        return FEHLERTEXT_BUNDLE.getString(schluessel);
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage(String schluessel, String... parameter) {
        return MessageFormat.format(getMessage(schluessel), (Object[]) parameter);
    }

}
