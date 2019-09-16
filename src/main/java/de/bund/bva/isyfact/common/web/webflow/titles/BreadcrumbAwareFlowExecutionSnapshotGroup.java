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
package de.bund.bva.isyfact.common.web.webflow.titles;

import java.io.Serializable;
import java.util.LinkedHashMap;

import org.springframework.webflow.execution.repository.impl.FlowExecutionSnapshotGroup;
import org.springframework.webflow.execution.repository.snapshot.FlowExecutionSnapshot;
import org.springframework.webflow.execution.repository.snapshot.SnapshotNotFoundException;

/**
 * Verwaltet eine Gruppe von Snapshots. Zum Breadcrumb gehörige Snapshots werden gesondert behandelt und nicht
 * entfernt, sobald die Anzahl der gespeicherten Snapshots {@link #getMaxSnapshots()} übersteigt.
 * 
 * @author Capgemini, Tobias Waller
 * @version $Id: BreadcrumbAwareFlowExecutionSnapshotGroup.java 123758 2014-10-10 10:01:14Z sdm_ahoerning $
 */
@Deprecated
public class BreadcrumbAwareFlowExecutionSnapshotGroup implements FlowExecutionSnapshotGroup, Serializable {

    /**
     * Serial Version UID.
     */
    private static final long serialVersionUID = -9223063152560721825L;

    /** Anzahl der Snapshots für den Breadcrumb. */
    private int breadcrumbSnapshotCount;

    /** Anzahl der maximal vorgehaltenen normalen Snapsots. */
    private int maxSnapshots;

    /**
     * The snapshot map; the key is a snapshot id, and the value is a {@link FlowExecutionSnapshot} object.
     */
    private LinkedHashMap<Serializable, FlowExecutionSnapshot> snapshots =
        new LinkedHashMap<Serializable, FlowExecutionSnapshot>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSnapshot(Serializable snapshotId, FlowExecutionSnapshot snapshot) {
        if (snapshots.containsKey(snapshotId)) {
            snapshots.remove(snapshotId);
        }
        snapshots.put(snapshotId, snapshot);
        if (getSnapshotCount() > getMaxSnapshots()) {
            performCleanup();
        }
    }

    /**
     * Sucht die größte gespeicherte Id.
     * @return die größte gespeicherte Id
     */
    public int getBiggestSnapshotId() {
        int result = 0;
        for (Serializable key : snapshots.keySet()) {
            String str = key.toString();
            int i = Integer.parseInt(str);
            result = result > i ? result : i;
        }
        return result;
    }

    /**
     * Liefert das Feld 'maxSnapshots' zurück.
     * @return Wert von maxSnapshots
     */
    public int getMaxSnapshots() {
        return maxSnapshots;
    }

    /**
     * gibt den Snapshot mit der gegebenen snapshotId zurück.
     * @param snapshotId
     *            die snapshotId des zu suchenden Snapshots
     * @return den snapshot mit der gegebenen snapshotId
     */
    @Override
    public FlowExecutionSnapshot getSnapshot(Serializable snapshotId) {
        FlowExecutionSnapshot snapshot = snapshots.get(snapshotId);
        if (snapshot == null) {
            throw new SnapshotNotFoundException(snapshotId);
        }
        return snapshot;
    }

    /**
     * Gibt die Anzahl der gespeicherten Snapshots, die nicht zum Breadcrumb gehörden zurück. {@inheritDoc}
     */
    @Override
    public int getSnapshotCount() {
        return snapshots.size() - breadcrumbSnapshotCount;
    }

    /**
     * legt einen zum Breadcrumb gehörigen Snapshot mit der gegebenen Id an.
     * @param sitemapKey
     *            die Id unter welcher der Snapshot abgelegt wird.
     * @param snapshot
     *            der abzulegende Snapshot
     */
    public void makeSitemapLink(String sitemapKey, FlowExecutionSnapshot snapshot) {
        if (!snapshots.containsKey(sitemapKey)) {
            breadcrumbSnapshotCount++;
        }
        addSnapshot(sitemapKey, snapshot);
    }

    /**
     * löscht den ältesten nicht zum Breadcrumb gehörigen Snapshot.
     */
    private void performCleanup() {
        for (Serializable id : snapshots.keySet()) {
            if (!(istBreadcrumbSnapshot(id))) {
                snapshots.remove(id);
                return;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllSnapshots() {
        // hier wird alles zurück gesetzt.
        // da die Breadcrumb-Zustände durch das Entfernen aller normalen Zustände nicht
        // mehr erreichbar sind, können diese ebenfalls entfernt werden.
        snapshots.clear();
        breadcrumbSnapshotCount = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSnapshot(Serializable snapshotId) {
        snapshots.remove(snapshotId);
        if (istBreadcrumbSnapshot(snapshotId)) {
            breadcrumbSnapshotCount--;
        }
    }

    /**
     * Setzt das Feld 'maxSnapshots'.
     * @param maxSnapshots
     *            Neuer Wert für maxSnapshots
     */
    public void setMaxSnapshots(int maxSnapshots) {
        this.maxSnapshots = maxSnapshots;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateSnapshot(Serializable snapshotId, FlowExecutionSnapshot snapshot) {
        if (!snapshots.containsKey(snapshotId)) {
            throw new SnapshotNotFoundException(snapshotId);
        }
        snapshots.put(snapshotId, snapshot);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Serializable nextSnapshotId() {
        return Integer.valueOf(getBiggestSnapshotId() + 1);
    }

    /**
     * Ersucht, ob ein Schlüssel zu einem Breadcrumeintrag gehört.
     * 
     * @param snapshotId
     *            der Schlüssel, der untersucht werden soll
     * @return <code>true</code> wenn es sich um einen Breadcrumb-Eintrag handelt, <code>false</code> sonst.
     */
    private boolean istBreadcrumbSnapshot(Serializable snapshotId) {
        if (snapshotId instanceof String) {
            String s = (String) snapshotId;
            return s.startsWith(BreadcrumbAwareFlowExecutionRepository.BREADCRUMB_PREFIX);
        } else {
            return false;
        }
    }

}
