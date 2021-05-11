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
package de.bund.bva.isyfact.web.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;
import de.bund.bva.isyfact.common.web.tempwebresource.persistence.TempWebResource;
import de.bund.bva.isyfact.common.web.tempwebresource.persistence.TempWebResourceDao;

/**
 * Diese Klasse testet den TempWebResource-DAO.
 * 
 * @author Capgemini sd&m AG
 * @version $Id: TempWebResourceDaoTest.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "resources/spring/plis-persistenz_jpa.xml", //
    "resources/spring/plis-querschnitt_konfiguration.xml" })
@Ignore
public class TempWebResourceDaoTest {

    @Autowired
    private TempWebResourceDao tempWebResourceDao;

    /**
     * Testet die Methode sucheDatei mit ungültigem Aufrufparametern.
     */
    public void testSucheDateiUngueltigeParameter() {
        try {
            tempWebResourceDao.sucheNeuesteTempWebResource(null, "bhknz", "kennzeichen");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            assertNull(tempWebResourceDao.sucheNeuesteTempWebResource("", "bhknz", "kennzeichen"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            assertNull(tempWebResourceDao.sucheNeuesteTempWebResource("benutzerkennung", null, "kennzeichen"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            assertNull(tempWebResourceDao.sucheNeuesteTempWebResource("benutzerkennung", "", "kennzeichen"));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            assertNull(tempWebResourceDao.sucheNeuesteTempWebResource("benutzerkennung", "bhknz", null));
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            assertNull(tempWebResourceDao.sucheNeuesteTempWebResource("benutzerkennung", "bhknz", ""));
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSucheTempWebResources() {
        String benutzerkennung = "Felix";
        String bhknz = "90060";
        String dateiinhalt = "Dies ist ein Test";

        TempWebResource resource1 = erzeugeTempWebResource(benutzerkennung, bhknz, "Kennzeichen A", dateiinhalt);

        TempWebResource resource2 = erzeugeTempWebResource(benutzerkennung, bhknz, "Kennzeichen B", dateiinhalt);

        List<TempWebResource> list =
            tempWebResourceDao.sucheTempWebResources(benutzerkennung, bhknz, "Kennzeichen A");
        assertEquals(1, list.size());
        assertEquals(resource1, list.get(0));

        list = tempWebResourceDao.sucheTempWebResources(benutzerkennung, bhknz, "Kennzeichen B");
        assertEquals(1, list.size());
        assertEquals(resource2, list.get(0));
    }

    /**
     * Testet die Methode loescheDateienAelterAls mit ungültigem Aufrufparameter.
     */
    public void testLoescheDateienAelterAlsUngueltigeParameter() {
        try {
            tempWebResourceDao.loescheTempWebResourcesAelterAls(null);
            fail();
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Testet die Methode speichereDatei mit ungültigem Aufrufparameter.
     */
    public void testSpeichereDateiUngueltigeParameter() {
        try {
            tempWebResourceDao.speichereTempWebResource(null);
            fail();
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Testet Speichern, Suchen/Exist
     * <ul>
     * <li>speichereDatei (Erstanlage)</li>
     * <li>exisitertDatei (+ sucheDatei)</li>
     * <li>sucheDatei</li>
     * </ul>
     */
    public void testSchreibenLesenExistsErfolgreich() {

        String benutzerkennung = "Felix";
        String bhknz = "900600";
        String kennzeichen = "Mein Kennzeichen";
        String dateiinhalt = "Dies ist ein Test";

        TempWebResource datei = erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt);

        // Prüfen, ob die Datei existiert
        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));

        // Auslesen
        TempWebResourceRo neueDatei =
            tempWebResourceDao.sucheNeuesteTempWebResource(benutzerkennung, bhknz, kennzeichen);
        assertSame(datei, neueDatei);
    }

    public void testLoeschen() throws InterruptedException {
        String benutzerkennung = "Felix";
        String bhknz = "900600";
        String kennzeichen = "Mein Kennzeichen";
        String dateiinhalt = "Dies ist ein Test";

        TempWebResource datei = erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt);

        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));

        tempWebResourceDao.loescheTempWebResource(datei);

        assertFalse(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));
    }

    /**
     * Testet das Löschen einer Datei anhand von Benutzerkennung u. BHKZ.
     */
    public void testLoeschenNachKriterien() {
        String benutzerkennung = "Felix";
        String bhknz = "900600";
        String kennzeichen = "Mein Kennzeichen";
        String dateiinhalt = "Dies ist ein Test";

        erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt);

        // Prüfen, ob die Datei existiert
        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));

        // Löschen
        tempWebResourceDao.loescheTempWebResources(benutzerkennung, bhknz, kennzeichen);

        // Prüfen, ob die Datei existiert
        assertFalse(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));
    }

    /**
     * Testet das Löschen von Dateien, deren Timestamp älter als ein angegebener Wert ist.
     */
    public void testLoeschenAelterAls() throws InterruptedException {

        String benutzerkennung = "Felix";
        String bhknz = "90060";
        String dateiinhalt = "Dies ist ein Test";
        String kennzeichen = "Mein Kennzeichen";

        long vorher = System.currentTimeMillis();

        erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt);

        // Legt die zweite Datei mit Verzögerung an.
        Thread.sleep(4000L);
        long dazwischen = System.currentTimeMillis();

        erzeugeTempWebResource(benutzerkennung + "2", bhknz + "2", kennzeichen, dateiinhalt);

        // Datum liegt vor beiden Daten
        tempWebResourceDao.loescheTempWebResourcesAelterAls(new Date(vorher - 1000));
        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));
        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung + "2", bhknz + "2", kennzeichen));

        // Nun löschen der älteren Datei
        tempWebResourceDao.loescheTempWebResourcesAelterAls(new Date(dazwischen - 2000));
        assertFalse(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));
        assertTrue(tempWebResourceDao.existiertTempWebResource(benutzerkennung + "2", bhknz + "2", kennzeichen));

        // Nun Löschen aller Dateien
        tempWebResourceDao.loescheTempWebResourcesAelterAls(new Date(dazwischen + 30000));
        assertFalse(tempWebResourceDao.existiertTempWebResource(benutzerkennung, bhknz, kennzeichen));
        assertFalse(tempWebResourceDao.existiertTempWebResource(benutzerkennung + "2", bhknz + "2", kennzeichen));
    }

    /**
     * Testet, ob das Überschreiben einer Datei korrekt funktioniert.
     */
    public void testSpeicherDateiUeberschreiben() throws InterruptedException {
        String benutzerkennung = "Felix";
        String bhknz = "900600";
        String dateiinhalt = "Dies ist ein Test";
        String dateiinhalt2 = "Neuer Text";
        String kennzeichen = "Mein Kennzeichen";

        TempWebResource datei = erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt);
        tempWebResourceDao.speichereTempWebResource(datei);

        tempWebResourceDao.loescheTempWebResources(benutzerkennung, bhknz, kennzeichen);

        TempWebResource datei2 = erzeugeTempWebResource(benutzerkennung, bhknz, kennzeichen, dateiinhalt2);
        tempWebResourceDao.speichereTempWebResource(datei2);

        TempWebResourceRo neueDatei =
            tempWebResourceDao.sucheNeuesteTempWebResource(benutzerkennung, bhknz, kennzeichen);
        assertNotNull(neueDatei);
        assertEquals(benutzerkennung, neueDatei.getBenutzerkennung());
        assertEquals(bhknz, neueDatei.getBhknz());
        assertEquals(dateiinhalt2, new String(neueDatei.getInhalt()));
    }

    private TempWebResource erzeugeTempWebResource(String benutzerkennung, String bhknz, String kennzeichen,
        String dateiinhalt) {
        TempWebResource datei = new TempWebResource();
        datei.setBenutzerkennung(benutzerkennung);
        datei.setBhknz(bhknz);
        datei.setKennzeichen(kennzeichen);
        datei.setInhalt(dateiinhalt.getBytes());
        tempWebResourceDao.speichereTempWebResource(datei);
        return datei;
    }
}
