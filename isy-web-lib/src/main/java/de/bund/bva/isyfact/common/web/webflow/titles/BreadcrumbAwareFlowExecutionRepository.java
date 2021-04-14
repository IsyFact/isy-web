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

import org.springframework.util.StringUtils;
import org.springframework.webflow.conversation.Conversation;
import org.springframework.webflow.conversation.ConversationId;
import org.springframework.webflow.conversation.ConversationManager;
import org.springframework.webflow.execution.FlowExecution;
import org.springframework.webflow.execution.FlowExecutionKey;
import org.springframework.webflow.execution.repository.impl.DefaultFlowExecutionRepository;
import org.springframework.webflow.execution.repository.impl.FlowExecutionSnapshotGroup;
import org.springframework.webflow.execution.repository.snapshot.FlowExecutionSnapshotFactory;
import org.springframework.webflow.execution.repository.support.CompositeFlowExecutionKey;

/**
 * Diese Klasse erweitert das {@link DefaultFlowExecutionRepository} um die Funktion Snapshots für den
 * Breadcrumb zu speichern. Diese werden nicht wie die übrigen Snapshots nach dem Überlauf von der
 * vorgehaltenen Daten über {@link #getMaxSnapshots()} entfernt, sondern vorgehalten, um die Breadcrumb-
 * Navigation zu garantieren.
 * 
 * @author Capgemini
 * @version $Id: BreadcrumbAwareFlowExecutionRepository.java 105410 2013-07-19 14:38:58Z sdm_ahoerning
 */
@Deprecated
public class BreadcrumbAwareFlowExecutionRepository extends DefaultFlowExecutionRepository {

    /**
     * Prefix für die Kodierung von {@link BreadcrumbAwareFlowExecutionKey}s - damit diese von normalen Keys
     * unterschieden werden können.
     */
    public static final String BREADCRUMB_PREFIX = "breadcrumb_";

    /** Konstante um die SnapshotId von der ExecutionId im kodierten String zu trennen. */
    static final String EXECUTION_SNAPSHOT_SPLITTER = "_c_";

    /**
     * Erzeugt ein neues ExecutionRepository mit dem gegebenen Conversation Manager und Snapshot Factory.
     * @param conversationManager
     *            the conversation manager to use
     * @param snapshotFactory
     *            the flow execution snapshot factory to use
     */
    public BreadcrumbAwareFlowExecutionRepository(ConversationManager conversationManager,
        FlowExecutionSnapshotFactory snapshotFactory) {
        super(conversationManager, snapshotFactory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected FlowExecutionSnapshotGroup createFlowExecutionSnapshotGroup() {
        // verwendet eine {@link BreadcrumbAwareFlowExecutionSnapshotGroup}
        BreadcrumbAwareFlowExecutionSnapshotGroup group = new BreadcrumbAwareFlowExecutionSnapshotGroup();
        group.setMaxSnapshots(getMaxSnapshots());
        return group;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowExecutionKey getKey(FlowExecution execution) {
        CompositeFlowExecutionKey key = (CompositeFlowExecutionKey) execution.getKey();
        if (key == null) {
            return super.getKey(execution);
        } else {
            return getNextKey(execution);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected FlowExecutionKey getNextKey(FlowExecution execution) {
        CompositeFlowExecutionKey key = (CompositeFlowExecutionKey) execution.getKey();
        if (key instanceof BreadcrumbAwareFlowExecutionKey) {
            // wenn wir auf einem Breadcrumblink sind:
            // suche den größten Key aus der Liste und erhöhe um 1
            Conversation conversation = getConversation(key);
            BreadcrumbAwareFlowExecutionSnapshotGroup snapshotGroup =
                (BreadcrumbAwareFlowExecutionSnapshotGroup) getSnapshotGroup(conversation);
            Integer nextId = (Integer) snapshotGroup.nextSnapshotId();
            return parseFlowExecutionKey("e" + key.getExecutionId() + "s" + nextId);
        } else {
            // andernfalls den gewohnten Key verwenden:
            if (getAlwaysGenerateNewNextKey()) {
                return new CompositeFlowExecutionKey(key.getExecutionId(),
                    nextSnapshotId(key.getExecutionId()));
            } else {
                return execution.getKey();
            }
        }

    }

    /**
     * Erstellt einen Snapshot des aktuellen Zustandes und gibt die ExecutionId mit dem der Zustand
     * wiederhergestellt werden kann zurück.
     * @param flowExecutionKey
     *            der FlowExecutionKey des aktuellen Zustandes
     * @param breadcrumbKey
     *            Key zur Identifikation der Seite im Breadcrumb (soll dem Link-Titel entsprechen)
     * @return die ExecutionId mit dem der Zustand wiederhergestellt werden kann
     */
    public String makeSitemapLink(String flowExecutionKey, String breadcrumbKey) {
        FlowExecutionKey key = parseFlowExecutionKey(flowExecutionKey);
        Conversation conversation = getConversation(key);
        ((BreadcrumbAwareFlowExecutionSnapshotGroup) getSnapshotGroup(conversation)).makeSitemapLink(
            BREADCRUMB_PREFIX + breadcrumbKey, getSnapshotFactory().createSnapshot(getFlowExecution(key)));
        return BREADCRUMB_PREFIX + breadcrumbKey + EXECUTION_SNAPSHOT_SPLITTER + conversation;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowExecutionKey parseFlowExecutionKey(String encodedKey) {
        // falls es sich um einen Breadcrumb-Key handelt:
        if (StringUtils.hasText(encodedKey) && encodedKey.startsWith(BREADCRUMB_PREFIX)) {
            // trenne snapshotId von conversationId
            String[] keys = encodedKey.split(EXECUTION_SNAPSHOT_SPLITTER);
            String sessionKey = keys[0];
            String executionId = keys[1];
            ConversationId conversationId = getConversationManager().parseConversationId(executionId);
            // gebe neuen Key aus geparsten Teilen zurück
            return new BreadcrumbAwareFlowExecutionKey(sessionKey, conversationId);
        }
        // andernfalls parse den Key wie gewohnt
        return super.parseFlowExecutionKey(encodedKey);
    }
}
