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
package de.bund.bva.isyfact.common.web.tempwebresource;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.common.web.tempwebresource.persistence.TempWebResourceDao;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;

/**
 * Diese Klasse löscht nach einem vorgegebenen Zeitraum alle alten Web-Ressourcen aus der Datenbank.
 *
 */
public class TempWebResourcePurgeService implements InitializingBean {

    /** Logger. */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(TempWebResourcePurgeService.class);

    /** Eine Stunde in Millisekunden ausgedrückt. */
    private static final long DEFAULT_LOESCHDATUM = 60L * 60L * 1000L;

    /** Der Transaktionsmanager. Wird per Spring gesetzt */
    private JpaTransactionManager transactionManager;

    /** Zeitraum in Millisekunden, vor dem gelöscht werden soll. Default: 1 Stunde */
    private long loeschIntervall = DEFAULT_LOESCHDATUM;

    /** TempWebResourceDao. */
    private TempWebResourceDao tempWebResourceDao;

    /**
     * Löscht alle alten Ressourcen.
     */
    public void loescheAlteTempWebResources() {

        // Transaktion öffnen
        TransactionStatus txStatus =
            this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        Date loeschdatum = new Date(System.currentTimeMillis() - this.loeschIntervall);
        LOG.info(LogKategorie.JOURNAL, EreignisSchluessel.E_DATENBANK_BEREINIGUNG,
            "Bereinige Datenbank - Lösche Einträge aus Tabelle TEMP_WEB_RESOURCE, die älter als {} sind.",
            loeschdatum);
        this.tempWebResourceDao.loescheTempWebResourcesAelterAls(loeschdatum);

        // Transaktion schließen
        this.transactionManager.commit(txStatus);
    }

    /**
     * Setzt das Feld 'transactionManager'.
     * @param transactionManager
     *            Neuer Wert für transactionManager
     */
    public void setTransactionManager(JpaTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    /**
     * Setzt das Feld 'loeschIntervall'.
     * @param loeschIntervall
     *            Neuer Wert für loeschIntervall
     */
    public void setLoeschIntervall(long loeschIntervall) {
        this.loeschIntervall = loeschIntervall;
    }

    /**
     * Setzt das Feld 'tempWebResourceDao'.
     * @param tempWebResourceDao
     *            Neuer Wert für tempWebResourceDao
     */
    @Required
    public void setTempWebResourceDao(TempWebResourceDao tempWebResourceDao) {
        this.tempWebResourceDao = tempWebResourceDao;
    }

    /**
     * Stellt sicher, dass ein positiver Wert als Löschdatum angegeben wurde.
     */
    @Override
    public void afterPropertiesSet() {
        if (this.loeschIntervall < 0) {
            LOG.warn(EreignisSchluessel.E_KONFIGURATION,
                "Es wurde ein negatives Löschintervall für die TEMP_WEB_RESOURCE"
                    + "-Tabelle konfiguriert. Verwende Standardwert 1 Stunde.");
            this.loeschIntervall = DEFAULT_LOESCHDATUM;
        }
    }

}
