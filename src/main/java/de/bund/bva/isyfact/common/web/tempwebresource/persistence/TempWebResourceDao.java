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

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;

/**
 * Die DAO-Klasse für die Entität TempWebResource.
 *
 */
@Repository
public class TempWebResourceDao {

    /**
     * Für den Zugriff auf JPA wird der JPA {@link EntityManager} verwendet. Dieser wird per Dependency
     * Injection injiziert. Für die Injizierung des EntityManagers wird die JPA-Annotation
     * {@link PersistenceContext} verwendet.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * Speichert die angegebene TempWebResource. Dabei wird das Attribut Zeitpunkt auf die aktuelle Systemzeit
     * gesetzt.
     *
     * @param webResource
     *            die zu speichernde TempWebResource
     */
    public void speichereTempWebResource(TempWebResource webResource) {
        if (webResource == null) {
            throw new IllegalArgumentException();
        }
        webResource.setZeitpunkt(new Date());

        this.em.persist(webResource);
    }

    /**
     * Lädt die TempWebResource mit der gegebenen ID.
     *
     * @param id
     *            die ID der TempWebResource
     * @return die TempWebResource
     */
    public TempWebResourceRo ladeTempWebResource(long id) {
        return this.em.find(TempWebResource.class, id);
    }

    /**
     * Löscht die gegebene TempWebResource.
     *
     * @param webResource
     *            die zu löschende TempWebResource
     */
    public void loescheTempWebResource(TempWebResourceRo webResource) {
        if (webResource != null) {
            this.em.remove(webResource);
        }
    }

    /**
     * Löscht alle TempWebResources, die älter als das Löschdatum sind. Es wird an dieser Stelle eine
     * Bulk-Operation verwendet.
     *
     * @param loeschdatum
     *            das Referenz-Löschdatum
     */
    public void loescheTempWebResourcesAelterAls(final Date loeschdatum) {
        if (loeschdatum == null) {
            throw new IllegalArgumentException();
        }

        Query query = this.em.createNamedQuery(NamedQueries.TEMP_WEB_RESOURCE_LOESCHEN_NACH_DATUM);
        query.setParameter(1, loeschdatum);
        query.executeUpdate();
    }

    /**
     * Löscht alle TempWebResources, die den Kriterien entsprechen.
     *
     * @param benutzerkennung
     *            die Benutzerkennung der TempWebResource
     * @param bhknz
     *            das Behördenkennzeichen der TempWebResource
     * @param kennzeichen
     *            das Kennzeichen der TempWebResource
     */
    public void loescheTempWebResources(final String benutzerkennung, final String bhknz,
        final String kennzeichen) {

        Query query = this.em.createNamedQuery(NamedQueries.TEMP_WEB_RESOURCE_LOESCHEN_NACH_KRITERIEN);
        query.setParameter(1, benutzerkennung);
        query.setParameter(2, bhknz);
        query.setParameter(3, kennzeichen);
        query.executeUpdate();
    }

    /**
     * Sucht TempWebResources anhand von Benutzerkennung, Behördenkennzeichen und Kennzeichen.
     *
     * @param benutzerkennung
     *            die Benutzerkennung der TempWebResource
     * @param bhknz
     *            das Behördenkennzeichen der TempWebResource
     * @param kennzeichen
     *            das Kennzeichen der TempWebResource
     * @return die gesuchten TempWebResource absteigend nach Erstellungszeitpunkt sortiert.
     */
    public List<TempWebResource> sucheTempWebResources(String benutzerkennung, String bhknz,
        String kennzeichen) {

        if (benutzerkennung == null || benutzerkennung.isEmpty() || bhknz == null || bhknz.isEmpty() ||
            kennzeichen == null || kennzeichen.isEmpty()) {
            throw new IllegalArgumentException();
        }

        TypedQuery<TempWebResource> query =
            this.em.createNamedQuery(NamedQueries.TEMP_WEB_RESOURCE_SUCHEN, TempWebResource.class);
        query.setParameter(1, benutzerkennung);
        query.setParameter(2, bhknz);
        query.setParameter(3, kennzeichen);
        return query.getResultList();
    }

    /**
     * Sucht die neueste TempWebResource anhand von Benutzerkennung, Behördenkennzeichen und Kennzeichen.
     *
     * @param benutzerkennung
     *            die Benutzerkennung der TempWebResource
     * @param bhknz
     *            das Behördenkennzeichen der TempWebResource
     * @param kennzeichen
     *            das Kennzeichen der TempWebResource
     * @return die gesuchte TempWebResource oder <code>null</code>, wenn keine gefunden wurde.
     */
    public TempWebResource sucheNeuesteTempWebResource(String benutzerkennung, String bhknz,
        String kennzeichen) {

        List<TempWebResource> webResources = sucheTempWebResources(benutzerkennung, bhknz, kennzeichen);

        if (CollectionUtils.isEmpty(webResources)) {
            return null;
        } else {
            return webResources.get(0);
        }
    }

    /**
     * Prüft, ob eine TempWebResource mit gegebener Benutzerkennung, Behördenkennzeichen und Kennzeichen
     * existiert.
     *
     * @param benutzerkennung
     *            die Benutzerkennung der TempWebResource
     * @param bhknz
     *            das Behördenkennzeichen der TempWebResource
     * @param kennzeichen
     *            das Kennzeichen der TempWebResource
     * @return <code>true</code>, wenn es eine TempWebResource gibt
     */
    public boolean existiertTempWebResource(String benutzerkennung, String bhknz, String kennzeichen) {
        return sucheNeuesteTempWebResource(benutzerkennung, bhknz, kennzeichen) != null;
    }
}
