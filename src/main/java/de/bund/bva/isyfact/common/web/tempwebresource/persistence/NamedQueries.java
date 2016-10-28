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
package de.bund.bva.isyfact.common.web.tempwebresource.persistence;

/**
 * Diese Klasse enthaelt die Namen aller Named Queries in der Datei <tt>NamedQueries.hbm.xml</tt>. Zugriff auf
 * die Queries dieser Datei sollte nur ueber Konstanten dieser Klasse geschehen, damit ihre Verwendung
 * nachverfolgt werden kann.
 *
 */
public final class NamedQueries {

    /**
     * Keine Instanziierung erlaubt.
     */
    private NamedQueries() {
    }

    /** Sucht alle TempWebResources anhand von Benutzerkennung, Behördenkennzeichen und Kennzeichen. */
    public static final String TEMP_WEB_RESOURCE_SUCHEN = "tempwebresource.suchen";

    /** Löscht alle TempWebResources, die älter als das angegebene Datum sind. */
    public static final String TEMP_WEB_RESOURCE_LOESCHEN_NACH_DATUM = "tempwebresource.loeschen.datum";

    /** Löscht alle TempWebResources mit den gesuchten Kriterien. */
    public static final String TEMP_WEB_RESOURCE_LOESCHEN_NACH_KRITERIEN =
        "tempwebresource.loeschen.kriterien";

}
