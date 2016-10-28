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

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Locale;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.webflow.core.Annotated;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.definition.FlowDefinition;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.definition.TransitionDefinition;
import org.springframework.webflow.engine.ViewState;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.FlowSession;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.common.konstanten.EreignisSchluessel;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;

/**
 * Stellt zusammen mit breadcrumbleiste.xhtml den Breadcrumb, den Title und die Headline bereit.
 * <p>
 * Ein TitlesListener muss im org.springframework.webflow.config.FlowExecutionListenerLoaderFactoryBean in der
 * Property listeners bekannt gemacht werden.
 * <p>
 * Ein Eintrag in der Sitemap wird mit Annotation des Attributs {@value #BREADCRUMB_ATTRIBUT_TRANSITION_NAME}
 * an eine Transition im Webflow erzeugt. Diese Annotation entält den Schlüssel, der zum Auflösen des
 * Anzeigetextes des Eintrages verwendet wird.
 * <p>
 * ViewStates können mit dem Attribut {@value #BREADCRUMB_ATTRIBUT_GRUPPE} annotiert werden. Im Sitemap darf
 * nur ein einziger Eintrag je Gruppe vorkommen. Unter anderem kann dies benutzt werden, wenn mehrere
 * ViewStates nur ein Breadcrumb verwenden sollen. Wenn ein neuer Breadcrumb-Eintrag mit einer Gruppenangabe
 * hinzugefügt wird, wird der existierende Eintrag von derselben Gruppe (wenn vorhanden) inkl. aller
 * nachfolgenden Einträgen entfernt.
 *
 * @author Capgemini, Artun Subasi
 * @author Capgemini, Tobias Waller
 * @version $Id: TitlesListener.java 143397 2015-07-30 08:49:32Z sdm_apheino $
 */
public class TitlesListener extends FlowExecutionListenerAdapter {

    /**
     * Log4j Logger.
     */
    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(TitlesListener.class);

    /**
     * Enthält den Schlüssel zum Finden der Sitemap in den Attribut-Maps der Scopes.
     * */
    private static final String BREADCRUMB_ATTRIBUT = "breadcrumb";

    /**
     * Enthält den Namen zum Finden des Breadcumbs in den Nachrichten.
     */
    private static final String BREADCRUMB_PROPERTY = "Breadcrumb";

    /**
     * Enthält den Schlüssel zum Finden der verkürzten Sitemap in den Attribut-Maps der Scopes.
     * */
    private static final String BREADCRUMB_ATTRIBUT_ABGESCHNITTEN = "breadcrumbAbgeschnitten";

    /**
     * Enthält den Schlüssel, mit dem in der Flowdefinition der Breadcrumb überschrieben werden kann.
     */
    public static final String BREADCRUMB_ATTRIBUT_TRANSITION_NAME = "breadcrumbKey";

    /**
     * Enthält den Namen des Attributs des Schlüssels für einen Namen eines Sitemap-Eintrages.
     */
    public static final String BREADCRUMB_ATTRIBUT_GRUPPE = "breadcrumbGruppe";

    /**
     * Enthält den Namen des Attributs für den Startseitenlink.
     */
    private static final String BREADCRUMB_STARTSEITENLINK = "breadcrumbStartseitenLink";

    /**
     * Enthält den Namen der Applikation. Dies wird als erster Eintrag verwendet.
     */
    private static final String BREADCRUMB_APPLIKATION_NAME = "MAS_Global_Breadcrumb_Applikationsname";

    /**
     * Enthält den Schlüssel für den Title, mit dem er in den Scope geschrieben wird.
     */
    private static final String TITLE_ATTRIBUT = "title";

    /**
     * Enthält den Namen zum Finden des Title in den Nachrichten.
     */
    private static final String TITLE_PROPERTY = "Title";

    /**
     * Enthält den Schlüssel, mit dem in der Flowdefinition der Titel überschrieben werden kann.
     */
    public static final String TITLE_ATTRIBUT_TRANSITION_NAME = "titleKey";

    /**
     * Enthält den Text, der als globaler Präfix für den Title verwendet werden soll.
     */
    public static final String TITLE_PREFIX_GLOBAL = "MAS_Global_Title_Prefix";

    /**
     * Enthält den Schlüssel für die Headline, mit der sie in den Scope geschrieben wird.
     */
    private static final String HEADLINE_ATTRIBUT = "headline";

    /**
     * Enthält den Namen zum Finden der Headline in den Nachrichten.
     */
    private static final String HEADLINE_PROPERTY = "Headline";

    /**
     * Enthält den Schlüssel, mit dem in der Flowdefinition die Headline überschrieben werden kann.
     */
    public static final String HEADLINE_ATTRIBUT_TRANSITION_NAME = "headlineKey";

    /**
     * Enthält das Präfix für Maskentexte.
     */
    public static final String PRAEFIX_TEXTS = "MAS";

    /**
     * Enthält den Schlüssel, der für Globale Angaben verwendet wird.
     */
    public static final String GLOBAL = "Global";

    /**
     * Wenn true, wird der StartseitenLink von dem zuerst geöffneten ViewState ermittelt und automatisch als
     * erster Eintrag in die Sitemap hinzugefügt.
     */
    private boolean dynamischerStartseitenLink;

    /** Zum Abschneiden von zu langen Breadcrumb-Listen. */
    private BreadcrumbAbschneider breadcrumbAbschneider;

    /**
     * Die MessageSource.
     */
    private MessageSource messageSource;

    /**
     * Der Name eines ViewStates.
     */
    private String viewStateName;

    /**
     * {@inheritDoc}
     */
    @Override
    public void stateEntering(RequestContext context, StateDefinition state) {
        super.stateEntering(context, state);
        this.viewStateName = state.getId();
        verarbeiteAktion(context, state);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void transitionExecuting(RequestContext context, TransitionDefinition transition) {
        super.transitionExecuting(context, transition);
        this.viewStateName = transition.getId();
        verarbeiteAktion(context, transition);

    }

    /**
     * {@inheritDoc}
     *
     * Füge beim Starten eines neuen Flows den Link zu der Startseite hinzu.
     */
    @Override
    public void sessionStarted(RequestContext context, FlowSession session) {
        super.sessionStarted(context, session);
        String startseitePath = context.getExternalContext().getContextPath();
        if (!startseitePath.endsWith("/")) {
            startseitePath += "/";
        }
        context.getFlowScope().put(BREADCRUMB_STARTSEITENLINK, startseitePath);
    }

    /**
     * Gibt die Message aus den Message-Sources zurück.
     * @param key
     *            Der Schlüssel.
     * @return Die Message.
     */
    protected String getMessage(String key) {
        String result = this.messageSource.getMessage(key, null, null, Locale.getDefault());
        return result;
    }

    /**
     * Verarbeitet eine Aktion.
     * @param context
     *            der Context
     * @param aktion
     *            die Aktion
     */
    private void verarbeiteAktion(RequestContext context, Annotated aktion) {
        if (bestimmeBreadcrumbTitel(aktion) != null) {
            breadcrumbStateEntering(context, aktion);
        } else {
            fuegeSitemapInFlowScopeHinzu(context, getSitemap(context));
        }

        pageTitleStateEntering(context, aktion);
        pageHeadlineStateEntering(context, aktion);

    }

    /**
     * Liest das Attribut {@value #BREADCRUMB_ATTRIBUT_TRANSITION_NAME} und stellt einen entsprechenden
     * Eintrag in der Sitemap bereit.
     * <p>
     * Wird aufgerufen, wenn eine Änderung stattfindet, die sich auf die Sitemap auswirkt.
     *
     * @param context
     *            der AufrufKontext, in welchem etwas interessantes passiert ist.
     * @param stateOrTransition
     *            der annotierte ViewState
     */
    private void breadcrumbStateEntering(RequestContext context, Annotated stateOrTransition) {

        final Deque<BreadcrumbEintrag> sitemap = getSitemap(context);
        final BreadcrumbEintrag neuerBreadcrumbEintrag =
            erzeugeBreadcrumbEintragAusViewState(stateOrTransition);

        if (sitemap.isEmpty()) {
            sitemap.add(neuerBreadcrumbEintrag);
        } else {
            aktualisiereSitemapMitNeuemBreadcrumbEintrag(context, sitemap, neuerBreadcrumbEintrag);
        }

        fuegeSitemapInFlowScopeHinzu(context, sitemap);
    }

    /**
     * Bestimmt den konkreten Titel.
     * @param context
     *            der Context
     * @param stateOrTransition
     *            der annotierte ViewState
     */
    private void pageTitleStateEntering(RequestContext context, Annotated stateOrTransition) {

        String pageTitle = bestimmePageTitleSchluessel(stateOrTransition);

        if (pageTitle == null) {
            // Flow-Name
            FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();
            String flowName = flowDefinition.getId();

            // Angabe mit ViewState-Name wird ausgelesen
            pageTitle =
                getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + this.viewStateName + "_" + TITLE_PROPERTY);

            if (pageTitle == null) {
                // Angabe ohne ViewState-Name wird ausgelesen
                pageTitle = getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + TITLE_PROPERTY);
                if (pageTitle == null) {
                    pageTitle = getMessage(PRAEFIX_TEXTS + "_" + GLOBAL + "_" + TITLE_PROPERTY);
                }
            }

            pageTitle = getMessage(TITLE_PREFIX_GLOBAL) + pageTitle;
        }

        fuegePageTitleInFlowScopeHinzu(context, pageTitle);

    }

    /**
     * Bestimmt die konkrete Headline.
     * @param context
     *            der Context
     * @param stateOrTransition
     *            der annotierte ViewState
     */
    private void pageHeadlineStateEntering(RequestContext context, Annotated stateOrTransition) {

        String pageHeadline = bestimmePageHeadlineSchluessel(stateOrTransition);

        if (pageHeadline == null) {
            // Flow-Name
            FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();
            String flowName = flowDefinition.getId();

            // Angabe mit ViewState-Name wird ausgelesen
            pageHeadline =
                getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + this.viewStateName + "_"
                    + HEADLINE_PROPERTY);

            if (pageHeadline == null) {
                // Angabe ohne ViewState-Name wird ausgelesen
                pageHeadline = getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + HEADLINE_PROPERTY);
            }
        }

        fuegePageHeadlineInFlowScopeHinzu(context, pageHeadline);
    }

    /**
     * Aktualisiert den Link vom letzten Breadcrumb, schneidet die Breadcrumbs wenn nötig ab und fügt den
     * letzten Breadcrumb-Eintrag am Ende hinzu.
     *
     * @param context
     *            SWF-Context
     * @param sitemap
     *            Sitemap
     * @param neuerBreadcrumbEintrag
     *            der neue {@link BreadcrumbEintrag}
     */
    private void aktualisiereSitemapMitNeuemBreadcrumbEintrag(final RequestContext context,
        final Deque<BreadcrumbEintrag> sitemap, final BreadcrumbEintrag neuerBreadcrumbEintrag) {

        int entfernteEintraege = 0;
        if (istBreadcrumbGruppeInSitemapVorhanden(neuerBreadcrumbEintrag.getGruppe(), sitemap)) {
            entfernteEintraege = entferneBreadcrumbsAbDerGruppe(sitemap, neuerBreadcrumbEintrag.getGruppe());

        } else if (sitemap.contains(neuerBreadcrumbEintrag)) {
            entfernteEintraege =
                entferneBreadrumbsAbDemGleichenBreadcrumbEintrag(sitemap, neuerBreadcrumbEintrag);
        }

        if (entfernteEintraege == 0) {
            // wenn entfernt wurde keine aktualisierung vornehmen. Der letzte Eintrag ist weg.
            // Muss nach entfernen vorgenommen werden, da sonst Annotationen innerhalb von Transitionen nicht
            // funktionieren, wenn diese in einen neunen Subflow führen.
            aktualisiereLetztenBreadcrumbLink(context.getFlowExecutionUrl(), sitemap.getLast());
        }

        sitemap.addLast(neuerBreadcrumbEintrag);
    }

    /**
     * Prüft, ob die übergebene Breadcrumb-Gruppe in einem Eintrag des Sitemaps existiert.
     *
     * @param breadcrumbGruppe
     *            die Breadcrumb-Gruppe, nach der gesucht wird
     * @param sitemap
     *            Sitemap
     * @return true, wenn übergebene Breadcrumb-Gruppe in einem Eintrag des Sitemaps existiert
     */
    private boolean istBreadcrumbGruppeInSitemapVorhanden(String breadcrumbGruppe,
        Deque<BreadcrumbEintrag> sitemap) {

        if (breadcrumbGruppe == null) {
            return false;
        }
        for (BreadcrumbEintrag breadcrumbEintrag : sitemap) {
            if (breadcrumbGruppe.equals(breadcrumbEintrag.getGruppe())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Aktualisiert den Link des letzten Sitemap-Eintrages mit einem neuen Schlüssel. Dann legt das
     * Schlüssel-Titel Paar in das FlowExecutionRepository.
     *
     * @param link
     *            der Link der aktuellen FlowExecution
     * @param letzterEintrag
     *            der letzte {@link BreadcrumbEintrag} in der Sitemap
     */
    private void aktualisiereLetztenBreadcrumbLink(String link, BreadcrumbEintrag letzterEintrag) {

        if (letzterEintrag.getLink() == null && link != null) {
            LOG.info(LogKategorie.JOURNAL, EreignisSchluessel.E_SITEMAP_DATEN_ERHALT,
                "behalte Sitemap Daten für: {}", link);
            letzterEintrag.setLink(link);
        }
    }

    /**
     * Entfernt alle Breadcrumb-Einträge ab dem Eintrag mit der übergebenen Gruppe aus Sitemap, inkl. dieses
     * letzten Elementes.
     *
     * @param sitemap
     *            Sitemap
     * @param breadcrumbGruppe
     *            die Breadcrumb-Gruppe, ab der das Sitemap abgeschnitten werden soll
     * @return Anzahl der entfernten Einträge
     */
    private int entferneBreadcrumbsAbDerGruppe(final Deque<BreadcrumbEintrag> sitemap,
        final String breadcrumbGruppe) {
        int anzEntfernt = 0;

        Iterator<BreadcrumbEintrag> breadcrumbIterator = sitemap.descendingIterator();
        boolean gruppeErreicht = false;
        while (breadcrumbIterator.hasNext() && !gruppeErreicht) {
            BreadcrumbEintrag eintrag = breadcrumbIterator.next();
            breadcrumbIterator.remove();
            anzEntfernt++;
            gruppeErreicht = breadcrumbGruppe.equals(eintrag.getGruppe());
        }
        return anzEntfernt;
    }

    /**
     * Entfernt alle Breadcrumb-Einträge ab dem übergebenen Eintrag aus Sitemap, inkl. dieses letzten
     * Elementes.
     *
     * @param sitemap
     *            Sitemap
     * @param breadcrumbEintrag
     *            der Breadcrumb-Eintrag, ab dem das Sitemap abgeschnitten werden soll
     * @return die Anzahl de rentfernten Einträge
     */
    private int entferneBreadrumbsAbDemGleichenBreadcrumbEintrag(final Deque<BreadcrumbEintrag> sitemap,
        final BreadcrumbEintrag breadcrumbEintrag) {

        int anzahlEntfernt = 0;
        Iterator<BreadcrumbEintrag> breadcrumbIterator = sitemap.descendingIterator();
        boolean eintragErreicht = false;
        while (breadcrumbIterator.hasNext() && !eintragErreicht) {
            BreadcrumbEintrag eintrag = breadcrumbIterator.next();
            breadcrumbIterator.remove();
            anzahlEntfernt++;
            eintragErreicht = eintrag.equals(breadcrumbEintrag);
        }
        return anzahlEntfernt;
    }

    /**
     * Fügt das Sitemap in FlowScope hinzu. Wenn das Sitemap bereits in FlowScope existiert, wird es
     * überschrieben.
     *
     * @param context
     *            SWF-Context
     * @param sitemap
     *            das Sitemap
     */
    private void fuegeSitemapInFlowScopeHinzu(RequestContext context, final Deque<BreadcrumbEintrag> sitemap) {
        final MutableAttributeMap<Object> flowScope = context.getFlowScope();

        flowScope.put(BREADCRUMB_ATTRIBUT, sitemap);
        if (this.breadcrumbAbschneider != null) {
            flowScope.put(BREADCRUMB_ATTRIBUT_ABGESCHNITTEN,
                this.breadcrumbAbschneider.schneideBreadcrumbsAb(sitemap));
        }
    }

    /**
     * Fügt den Title in den FlowScope hinzu.
     * @param context
     *            Context
     * @param pageTitle
     *            der Title
     */
    private void fuegePageTitleInFlowScopeHinzu(RequestContext context, String pageTitle) {

        final MutableAttributeMap<Object> flowScope = context.getFlowScope();
        flowScope.put(TITLE_ATTRIBUT, pageTitle);
    }

    /**
     * Fügt die Headline in den FlowScope hinzu.
     * @param context
     *            Context
     * @param pageHeadline
     *            die Headline
     */
    private void fuegePageHeadlineInFlowScopeHinzu(RequestContext context, String pageHeadline) {

        final MutableAttributeMap<Object> flowScope = context.getFlowScope();
        flowScope.put(HEADLINE_ATTRIBUT, pageHeadline);
    }

    /**
     * Erzeugt anhand der annotierten Attribute vom {@link ViewState} einen {@link BreadcrumbEintrag}. Der
     * Titel und (wenn vorhanden) die Gruppe wird gesetzt. Der Link wird zu diesem Zeitpunkt nicht gesetzt.
     *
     * @param stateOrTransition
     *            {@link StateDefinition} vom {@link ViewState}
     * @return der {@link BreadcrumbEintrag}
     */
    private BreadcrumbEintrag erzeugeBreadcrumbEintragAusViewState(Annotated stateOrTransition) {

        String breadcrumbTitel = bestimmeBreadcrumbTitel(stateOrTransition);

        final String breadcrumbGruppe = bestimmeBreadcrumbGruppe(stateOrTransition);
        return new BreadcrumbEintrag(breadcrumbTitel, breadcrumbGruppe);
    }

    /**
     * Ermittelt die Sitemap von dem Flowscope. Wenn die Sitemap nicht existiert, erstellt eine neue Sitemap,
     * die auf dem Parent-Flow basiert ist. Wenn der Parent-Flow nicht existiert, wird eine frische Sitemap
     * erstellt.
     *
     * @param context
     *            der RequestContext, aus dem der ParentFlow bzw. der Scope gelesen wird
     * @return die ermittelte oder neue Sitemap
     */
    @SuppressWarnings("unchecked")
    private Deque<BreadcrumbEintrag> getSitemap(RequestContext context) {

        final FlowSession parentFlow = context.getFlowExecutionContext().getActiveSession().getParent();
        final MutableAttributeMap<Object> flowScope = context.getFlowScope();

        Deque<BreadcrumbEintrag> sitemap = (Deque<BreadcrumbEintrag>) flowScope.get(BREADCRUMB_ATTRIBUT);
        if (sitemap == null) {
            sitemap = new LinkedList<BreadcrumbEintrag>();
            if (parentFlow != null) {
                Deque<BreadcrumbEintrag> parentSitemap =
                    (Deque<BreadcrumbEintrag>) parentFlow.getScope().get(BREADCRUMB_ATTRIBUT);
                if (parentSitemap != null) {
                    sitemap.addAll(parentSitemap);
                }
            }
        }
        if (this.dynamischerStartseitenLink && sitemap.isEmpty()) {
            sitemap.add(new BreadcrumbEintrag(BREADCRUMB_APPLIKATION_NAME));
        }
        return sitemap;
    }

    /**
     * Bestimmt den Breadcrumb-Titel des Sitemap-Eintrages. Falls der Attribut-Wert mit "#{xxx}" umgeben ist,
     * wird der Wert "xxx" aufgeloest. So ist bspw. eine Aufloesung von "bc" durch Angabe des Attribut-Werts
     * "#{bc}" gegen "flowScope.bc" moeglich.
     *
     * @param annotatedStateOrTransition
     *            der betretene Zustand bzw die ausgelöste Transition.
     * @return der Breadcrumb-Titel, der an state annotiert wurde, aus den Nachrichten oder <code>null</code>
     *         falls keine Annotation gefunden werden konnte bzw. wenn der Name leer ist
     */
    private String bestimmeBreadcrumbTitel(Annotated annotatedStateOrTransition) {

        if (annotatedStateOrTransition == null) {
            return null;
        }
        String flow = null;
        if (annotatedStateOrTransition instanceof ViewState) {
            flow = ((ViewState) annotatedStateOrTransition).getFlow().getId();
        }
        MutableAttributeMap<Object> attributes = annotatedStateOrTransition.getAttributes();
        String transitionName = (String) attributes.get(BREADCRUMB_ATTRIBUT_TRANSITION_NAME);
        String breadcrumbTitel = null;
        if (StringUtils.hasText(transitionName)) {
            // Falls eine Expression mit "#{xxx}" umgeben ist, wird diese Expression aufgeloest.
            if (transitionName.contains("#{")) {
                return resolveElExpression(transitionName, flow);
            }
        }
        breadcrumbTitel = getMessage(transitionName);

        // Breadcrumb wird aus den Nachrichten ausgelesen, wenn wir uns in einem ViewState befinden und kein
        // Breadcrumb bisher ermittelt wurde.
        if (breadcrumbTitel == null && (annotatedStateOrTransition.getClass() == ViewState.class)) {

            // Flow-Name
            FlowDefinition flowDefinition = RequestContextHolder.getRequestContext().getActiveFlow();

            String flowName = flowDefinition.getId();

            // Angabe mit ViewState-Name wird ausgelesen
            breadcrumbTitel =
                getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + this.viewStateName + "_"
                    + BREADCRUMB_PROPERTY);

            if (breadcrumbTitel == null) {
                // Angabe ohne ViewState-Name wird ausgelesen
                breadcrumbTitel = getMessage(PRAEFIX_TEXTS + "_" + flowName + "_" + BREADCRUMB_PROPERTY);
            }
        }

        return breadcrumbTitel;
    }

    /**
     * Bestimmt den Title (Schlüssel). Falls der Attribut-Wert mit "#{xxx}" umgeben ist, wird der Wert "xxx"
     * aufgeloest. So ist bspw. eine Aufloesung von "bc" durch Angabe des Attribut-Werts "#{bc}" gegen
     * "flowScope.bc" moeglich.
     * @param annotatedStateOrTransition
     *            der betretene Zustand bzw die ausgelöste Transition.
     * @return der Title (Schlüssel), der an state annotiert wurde, oder <code>null</code> falls keine
     *         Annotation gefunden werden konnte bzw. wenn der Name leer ist
     */
    private String bestimmePageTitleSchluessel(Annotated annotatedStateOrTransition) {

        if (annotatedStateOrTransition == null) {
            return null;
        }
        String flow = null;
        if (annotatedStateOrTransition instanceof ViewState) {
            flow = ((ViewState) annotatedStateOrTransition).getFlow().getId();
        }

        MutableAttributeMap<Object> attributes = annotatedStateOrTransition.getAttributes();
        String transitionName = (String) attributes.get(TITLE_ATTRIBUT_TRANSITION_NAME);
        if (StringUtils.hasText(transitionName)) {
            // Falls eine Expression mit "#{xxx}" umgeben ist, wird diese Expression aufgeloest.
            if (transitionName.contains("#{")) {
                return resolveElExpression(transitionName, flow);
            }
        }
        return null;
    }

    /**
     * Bestimmt die Headline (Schlüssel). Falls der Attribut-Wert mit "#{xxx}" umgeben ist, wird der Wert
     * "xxx" aufgeloest. So ist bspw. eine Aufloesung von "bc" durch Angabe des Attribut-Werts "#{bc}" gegen
     * "flowScope.bc" moeglich.
     * @param annotatedStateOrTransition
     *            der betretene Zustand bzw die ausgelöste Transition.
     * @return die Headlien (Schlüssel), der an state annotiert wurde, oder <code>null</code> falls keine
     *         Annotation gefunden werden konnte bzw. wenn der Name leer ist
     */
    private String bestimmePageHeadlineSchluessel(Annotated annotatedStateOrTransition) {

        if (annotatedStateOrTransition == null) {
            return null;
        }
        String flow = null;
        if (annotatedStateOrTransition instanceof ViewState) {
            flow = ((ViewState) annotatedStateOrTransition).getFlow().getId();
        }

        MutableAttributeMap<Object> attributes = annotatedStateOrTransition.getAttributes();
        String transitionName = (String) attributes.get(HEADLINE_ATTRIBUT_TRANSITION_NAME);
        if (StringUtils.hasText(transitionName)) {
            // Falls eine Expression mit "#{xxx}" umgeben ist, wird diese Expression aufgeloest.
            if (transitionName.contains("#{")) {
                return resolveElExpression(transitionName, flow);
            }
        }
        return null;
    }

    /**
     * Bestimmt die Breadcrumb-Gruppe des Sitemap-Eintrages.
     *
     * @param stateOrTransition
     *            der betretene Zustand.
     * @return die Breadcrumb-Gruppe, die an state annotiert wurde, oder <code>null</code> falls keine
     *         Annotation gefunden werden konnte bzw. wenn die Gruppe leer ist
     */
    private String bestimmeBreadcrumbGruppe(Annotated stateOrTransition) {
        if (stateOrTransition == null) {
            return null;
        }
        MutableAttributeMap<Object> attributes = stateOrTransition.getAttributes();
        String gruppe = (String) attributes.get(BREADCRUMB_ATTRIBUT_GRUPPE);
        if (StringUtils.hasText(gruppe)) {
            return gruppe;
        }
        return null;
    }

    /**
     * Löst die EL-Expression als String auf.
     * @param elExpression
     *            Die EL-Expression.
     * @param nextFlow
     *            Der Name des neuen Flows.
     * @return Der String
     */
    private String resolveElExpression(String elExpression, String nextFlow) {

        // EL-Context auflösen
        FacesContext context = FacesContext.getCurrentInstance();
        ExpressionFactory expressionFactory = context.getApplication().getExpressionFactory();
        ELContext elContext = context.getELContext();

        ValueExpression vex = expressionFactory.createValueExpression(elContext, elExpression, String.class);
        return (String) vex.getValue(elContext);
    }

    public void setDynamischerStartseitenLink(boolean dynamischerStartseitenLink) {
        this.dynamischerStartseitenLink = dynamischerStartseitenLink;
    }

    public void setBreadcrumbAbschneider(BreadcrumbAbschneider breadcrumbAbschneider) {
        this.breadcrumbAbschneider = breadcrumbAbschneider;
    }

    @Required
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
