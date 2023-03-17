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
package de.bund.bva.isyfact.common.web.tempwebresource.impl;

import org.springframework.transaction.annotation.Transactional;

import de.bund.bva.isyfact.common.web.exception.ZugriffBerechtigungException;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceZugriff;
import de.bund.bva.isyfact.common.web.tempwebresource.persistence.TempWebResource;
import de.bund.bva.isyfact.common.web.tempwebresource.persistence.TempWebResourceDao;

/**
 * Implementierung der Komponente TempWebResourceZugriff.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Transactional
public class TempWebResourceZugriffImpl implements TempWebResourceZugriff {

    /** TempWebResourceDao. */
    private TempWebResourceDao tempWebResourceDao;

    public TempWebResourceZugriffImpl(TempWebResourceDao tempWebResourceDao) {
        this.tempWebResourceDao = tempWebResourceDao;
    }

    /**
     * {@inheritDoc}
     */
    public TempWebResourceRo speichereTempWebResource(String benutzerkennung, String bhknz, byte[] ressource,
        String kennzeichen, String mimeType) {
        return speichereTempWebResource(benutzerkennung, bhknz, ressource, kennzeichen, mimeType, null);
    }

    /**
     * {@inheritDoc}
     */
    public TempWebResourceRo speichereTempWebResource(String benutzerkennung, String bhknz, byte[] ressource,
        String kennzeichen, String mimeType, String dateiname) {
        TempWebResource webResource = new TempWebResource();
        webResource.setBenutzerkennung(benutzerkennung);
        webResource.setBhknz(bhknz);
        webResource.setKennzeichen(kennzeichen);
        webResource.setMimeType(mimeType);
        webResource.setDateiname(dateiname);
        webResource.setInhalt(ressource);
        this.tempWebResourceDao.speichereTempWebResource(webResource);
        return webResource;
    }

    /**
     * {@inheritDoc}
     */
    public TempWebResourceRo ladeTempWebResource(long id, String benutzerkennung, String bhknz) {

        TempWebResourceRo webResource = this.tempWebResourceDao.ladeTempWebResource(id);
        pruefeZugriffsberechtigung(webResource, benutzerkennung, bhknz);
        return webResource;
    }

    /**
     * {@inheritDoc}
     */
    public void loescheTempWebResource(long id, String benutzerkennung, String bhknz) {

        TempWebResourceRo webResource = this.tempWebResourceDao.ladeTempWebResource(id);
        pruefeZugriffsberechtigung(webResource, benutzerkennung, bhknz);
        this.tempWebResourceDao.loescheTempWebResource(webResource);
    }

    /**
     * {@inheritDoc}
     */
    public void loescheTempWebResources(String benutzerkennung, String bhknz, String kennzeichen) {
        this.tempWebResourceDao.loescheTempWebResources(benutzerkennung, bhknz, kennzeichen);
    }

    /**
     * Prüft, ob der Benutzer auf die gegebene Web-Ressource zugreifen darf.
     *
     * @param webResource
     *            die Web-Ressource
     * @param benutzerkennung
     *            die Kennung des aktuellen Benutzers
     * @param bhknz
     *            das Behördenkennzeichen des aktuellen Benutzers
     */
    private void pruefeZugriffsberechtigung(TempWebResourceRo webResource, String benutzerkennung,
        String bhknz) {
        if (webResource != null) {
            if (!(benutzerkennung.equals(webResource.getBenutzerkennung()) && bhknz.equals(webResource
                .getBhknz()))) {

                throw new ZugriffBerechtigungException(
                    FehlerSchluessel.KEINE_BERECHTIGUNG_FUER_TEMP_WEB_RESOURCE, new String[] {
                        benutzerkennung, webResource.getDateiname(), webResource.getBenutzerkennung() });
            }
        }
    }

    /**
     * Setzt das Feld 'tempWebResourceDao'.
     * @param tempWebResourceDao
     *            Neuer Wert für tempWebResourceDao
     */
    public void setTempWebResourceDao(TempWebResourceDao tempWebResourceDao) {
        this.tempWebResourceDao = tempWebResourceDao;
    }
}
