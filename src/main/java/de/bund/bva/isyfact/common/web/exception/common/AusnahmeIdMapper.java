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
package de.bund.bva.isyfact.common.web.exception.common;

import de.bund.bva.isyfact.exception.FehlertextProvider;

/**
 * Anhand dieses Mappers werden Ausnahme-IDs für die Fehler hinterlegt, welche nicht in der Anwendung speziell
 * erzeugt werden. Für Isy-Exceptions dürfen keine Ausnahme-ID hinterlegt werden, weil sie bereits über eine
 * Ausnahme-ID verfügen und von der WebflowExceptionHandler ohne weiteres behandelt werden können.
 * <p>
 * Die zu den Ausnahme-IDs gehörenden Fehlertexte dürfen nicht parameterisiert sein.
 * <p>
 * Beispiele für die Fehler, welche nicht in der Anwendung speziell erzeugt werden: bestimmte Datenbankfehler,
 * Fehler durch Programmierfehler (NullPointerException,…).
 * <p>
 * <b>Als Fallback für undifferenzierte Fehler muss eine allgemeine Fehler-ID hinterlegt werden.</b>
 *
 * @author Capgemini
 * @version $Id: AusnahmeIdMapper.java 141870 2015-07-16 10:07:27Z sdm_jmalkiewicz $
 */
public interface AusnahmeIdMapper {

    /**
     * Liefert eine Ausnahme-ID für eine Exception, welche nicht in der Anwendung speziell erzeugt wird (z.b.
     * DataAccessException, TransactionException).
     *
     * @param throwable
     *            Die Exception.
     *
     * @return eine Ausnahme-ID für eine Exception, welche nicht in der Anwendung speziell erzeugt wird. Der
     *         Ruckgabewert ist <code>null</code>, wenn es sich um einen unvorhergesehenen Fehler handelt.
     */
    public String getAusnahmeId(Throwable throwable);

    /**
     * Gibt die AusnahmeId zurück, die verwendet werden soll, wenn ein unvorhergesehener Fehler aufgetreten
     * ist.
     * @return die AusnahmeId zurück, die verwendet werden soll, wenn ein unvorhergesehener Fehler aufgetreten
     *         ist
     */
    public String getFallbackAusnahmeId();

    /**
     * Liefert einen {@link FehlertextProvider} zurück, mit dem die Anwendung spezifische Fehlertexte laden
     * kann.
     *
     * @return einen {@link FehlertextProvider} zurück, mit dem die Anwendung spezifische Fehlertexte laden
     *         kann.
     */
    public FehlertextProvider getFehlertextProvider();

}
