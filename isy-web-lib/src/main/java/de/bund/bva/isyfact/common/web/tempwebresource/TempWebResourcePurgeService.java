package de.bund.bva.isyfact.common.web.tempwebresource;

import java.util.Date;

import org.springframework.beans.factory.InitializingBean;
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
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
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

    public TempWebResourcePurgeService(TempWebResourceDao tempWebResourceDao) {
        this.tempWebResourceDao = tempWebResourceDao;
    }

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
