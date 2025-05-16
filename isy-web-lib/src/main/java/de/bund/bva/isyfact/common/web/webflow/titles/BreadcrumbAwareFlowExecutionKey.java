package de.bund.bva.isyfact.common.web.webflow.titles;

import java.io.Serializable;

import org.springframework.webflow.execution.repository.support.CompositeFlowExecutionKey;

/**
 * Erweitert die Klasse @ CompositeFlowExecutionKey} , damit Snapshots für den Breadcrumb von gewöhnlichen
 * Snapshots unterschieden werden können.
 * 
 */
@Deprecated
public class BreadcrumbAwareFlowExecutionKey extends CompositeFlowExecutionKey {

    /**
     * Serial Verion UID.
     */
    private static final long serialVersionUID = -6679381321479048822L;

    /** identifiziert den Snapshot. */
    private String encodedKey;

    /** identifiziert die Konversation. */
    private Serializable executionId;

    /**
     * erstellt einen neuen Key mit den gegebenen Parametern.
     * @param encodedKey
     *            der Snapshot-Schlüssel
     * @param executionId
     *            der Konversation-Schlüssel
     */
    public BreadcrumbAwareFlowExecutionKey(String encodedKey, Serializable executionId) {
        super(0, 0);
        this.executionId = executionId;
        this.encodedKey = encodedKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BreadcrumbAwareFlowExecutionKey other = (BreadcrumbAwareFlowExecutionKey) obj;
        if (encodedKey == null) {
            if (other.encodedKey != null) {
                return false;
            }
        } else if (!encodedKey.equals(other.encodedKey)) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable getExecutionId() {
        return this.executionId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable getSnapshotId() {
        return encodedKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((encodedKey == null) ? 0 : encodedKey.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return encodedKey + BreadcrumbAwareFlowExecutionRepository.EXECUTION_SNAPSHOT_SPLITTER
            + executionId.toString();
    }

}
