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
package de.bund.bva.isyfact.common.web.konstanten;

/**
 * Diese Klasse enthält die Konstanten für Nachrichten.
 * <p>
 * Aufbau der Fehler-Ids: MEL_Error
 * <ul>
 * <li>MEL_Fehler - Fehler
 * </ul>
 * 
 * @author Capgemini, Artun Subasi
 * @author sd&amp;m AG, Felix Senn
 * @version $Id: MaskentexteSchluessel.java 130854 2015-02-18 10:42:27Z sdm_mhartung $
 */
public abstract class MaskentexteSchluessel {

    /**  Unzulässige Daten vorhanden. */
    public static final String MEL_UNZULAESSIGE_DATEN_VORHANDEN = "MEL_Unzulaessige_Daten_Vorhanden";
    
    /** Bitte prüfen Sie die gekennzeichneten Felder. */
    public static final String MEL_GEKENNZEICHNETE_FELDER_PRUEFEN = "MEL_Gekennzeichnete_Felder_Pruefen";
}
