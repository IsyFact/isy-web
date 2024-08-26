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
package de.bund.bva.isyfact.common.web.servlet.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;

/**
 * Dieser Filter forciert die Existenz des HttpSession-Attributs
 * {@link GuiParameterSchluessel#JAVASCRIPT_AKTIVIERT_SCHLUESSEL "javascriptAktiviert"}. Falls dieses Attribut
 * nicht vorhanden ist und auch kein HTTP-GET-Parameter mit gleichem Namen und dem Wert "true"/"false"
 * uebermittelt wurde, wird zur angefragten Url eine Application-Initialisierungsseite ausgeliefert. Diese
 * Seite sorgt dafuer, dass ein Anwendungsaufruf mittels des Url-Get-Parameters
 * {@link GuiParameterSchluessel#JAVASCRIPT_AKTIVIERT_SCHLUESSEL "javascriptAktiviert"} und dem Wert
 * "true"/"false" erfolgt. Sofern die Anwendung mit diesem HttpGet-Attribut aufgerufen wird, wird der Wert
 * dieses Attributs ausgewertet und im gueltigen Falle in der HttpSession hinterlegt. Ist das
 * HttpSession-Attribut vorhanden oder handelt es sich um eine konfigurierte {@link #urlsToSkip zu
 * ueberspringende Url}, so wird die Anfrage nicht gefiltert.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class ApplicationInitialisierungFilter implements Filter {

    /** Ist eine Liste mit auszulassenden Url-Pfaden, relativ zum ApplicationContext-Root. */
    private List<String> urlsToSkip = new ArrayList<String>();

    /** Ist die relative Url zur Initialisierungsseite der Anwendung. */
    private String urlApplicationInitialisierung;

    /**
     * Ist die URL auf welche Initialisierunganfragen umgeleitet werden, sofern diese über AJAX ausgeführt
     * werden.
     */
    private String ajaxRedirectUrl;

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestUrlGesamt = getGesamteRequestUrl(request);
        String applicationContextPfad = request.getContextPath();

        // Fall 0: Es handelt sich um eine zu ueberspringende Url, bspw. auf eine Ressource.
        boolean urlIstZuUeberspringen =
            ermittleUrlIstZuUeberspringen(requestUrlGesamt, applicationContextPfad);
        if (urlIstZuUeberspringen) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Zugriff auf die Session erst, wenn sie benötigt wird.
        HttpSession httpSession = request.getSession();

        // Fall 1: In der Session ist das Attribut "javascriptAktiviert" gesetzt, d.h. es hat bereits eine
        // Auswertung stattgefunden, ob browserseitig Javascript aktiviert ist oder nicht.
        boolean httpSessionJavascriptAktivierungAusgewertet =
            httpSession.getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL) != null;
        if (httpSessionJavascriptAktivierungAusgewertet) {
            chain.doFilter(servletRequest, servletResponse);
            return;
        }

        // Fall 2: Es wurde ein HTTP-GET-Parameter "javascriptAktiviert=true/false" uebergeben. Dieser soll
        // ausgewertet und in die HttpSession geschrieben werden. Anschliessend wird das HTTP-GET-Attribut
        // "urspruenglicheUrl=xxx" ausgewertet und eine Weiterleitung an diese Url durchgefuehrt.
        // Werte den HTTP-GET-Parameter aus
        Boolean requestParameterJavascriptAktiviert =
            ermittleRequestParameterWertJavascriptAktiviert(request);

        // Falls der Request-Parameter ausgewertet werden konnte, hinterlege die Information in der
        // HTTP-Session und fuehre einen Redirect auf die urspruenglich angefragte Seite durch.
        if (requestParameterJavascriptAktiviert != null) {
            httpSession.setAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL,
                requestParameterJavascriptAktiviert);

            String redirectUrlPfad = ermittleUrspruenglicheAufrufUrl(request, applicationContextPfad);
            response.sendRedirect(redirectUrlPfad);
            return;
        }

        // Fall 3: Keine der angegebenen Faelle trifft zu. Es wird eine Initialisierungsseite ausgeliefert,
        // ueber die Fall 2 erstellt wird. Erstelle einen Dispatcher-Redirect.

        // Falls dieser Request über AJAX aufgerufen wurde, führe ein Redirect aus.
        if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
            // It's a JSF ajax request.
            response.setContentType("text/xml");
            response
                .getWriter()
                .append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .printf("<partial-response><redirect url=\"%s\"></redirect></partial-response>",
                    this.ajaxRedirectUrl);
            return;

        }

        // Stelle die urspruengliche Url als Request-Parameter bereit.
        String urspruenglicheAnfrageUrlEncoded = encodeUrl(requestUrlGesamt);
        request.setAttribute(GuiParameterSchluessel.URSPRUENGLICHE_ANFRAGE_URL,
            urspruenglicheAnfrageUrlEncoded);
        request.getRequestDispatcher(this.urlApplicationInitialisierung).forward(request, response);
        return;
    }

    /**
     * Prueft, ob die Anfrage-Url zu ueberspringen ist.
     *
     * @param requestUrlGesamt
     *            ist die gesamte Anfrage-Url
     * @param applicationContextPfad
     *            ist der ApplicationContext-Pfad
     * @return <code>true</code>, falls die Url zu ueberspringen ist. <code>false</code> ansonsten.
     */
    private boolean ermittleUrlIstZuUeberspringen(String requestUrlGesamt, String applicationContextPfad) {
        if (this.urlsToSkip == null) {
            return false;
        }

        for (String urlToSkip : this.urlsToSkip) {
            String urlToSkipMitApplicationContextPfad = applicationContextPfad + urlToSkip;
            if (requestUrlGesamt.startsWith(urlToSkipMitApplicationContextPfad)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Diese Funktion ermittelt, ob der Anfrage-Parameter
     * {@link GuiParameterSchluessel#JAVASCRIPT_AKTIVIERT_SCHLUESSEL "javascriptAktiviert"} angegenen wurde.
     * Falls ja, wird der Wert extrahiert. Ist dieser gueltig, d.h. er entspricht case-insensitive "true" bzw.
     * "false", wird {@link Boolean#TRUE} bzw. {@link Boolean#FALSE} zurueckgeliefert. Ansonsten
     * <code>null</code>.
     *
     * @param request
     *            ist der {@link HttpServletRequest}
     * @return den {@link Boolean}-Wert, falls dieser ermittelt werden konnte. Ansonsten <code>null</code>.
     */
    private Boolean ermittleRequestParameterWertJavascriptAktiviert(HttpServletRequest request) {
        String requestParameterJavascriptAktivierung =
            request.getParameter(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL);
        if (Strings.isNullOrEmpty(requestParameterJavascriptAktivierung)) {
            return null;
        }

        // Werte den HTTP-GET-Parameter aus
        // Der Parameter besitzt den Wert "true"
        if ("true".equalsIgnoreCase(requestParameterJavascriptAktivierung)) {
            return Boolean.TRUE;
        }

        // Der Parameter besitzt den Wert "false"
        if ("false".equalsIgnoreCase(requestParameterJavascriptAktivierung)) {
            return Boolean.FALSE;
        }

        // Der Parameter-Wert ist ungueltig.
        return null;
    }

    /**
     * Ermittelt die urspruengliche Aufruf-Url mit allen HTTP-GET-Parametern aus dem HTTP-GET-Parameter
     * {@link GuiParameterSchluessel#URSPRUENGLICHE_ANFRAGE_URL "urspruenglicheAnfrageUrl"}.
     *
     * @param request
     *            ist der {@link HttpServletRequest}
     * @param applicationContextPfad
     *            ist der aktuelle ApplicationContextPfad
     * @return die urspruengliche AnfrageUrl.
     * @throws UnsupportedEncodingException
     *             sofern das Encoding nicht unterstuetzt wird.
     */
    private String ermittleUrspruenglicheAufrufUrl(HttpServletRequest request, String applicationContextPfad)
        throws UnsupportedEncodingException {
        String urspruenglicheAnfrageUrl =
            request.getParameter(GuiParameterSchluessel.URSPRUENGLICHE_ANFRAGE_URL);
        if (!Strings.isNullOrEmpty(urspruenglicheAnfrageUrl)) {
            String result = decodeUrl(urspruenglicheAnfrageUrl);
            return result;
        }

        // Fallback
        return applicationContextPfad;
    }

    /**
     * Codiert eine URL.
     *
     * @param url
     *            ist die zu codierende Url
     * @return die codierte Url
     * @throws UnsupportedEncodingException
     *             sofern das Encoding nicht unterstuetzt wird.
     */
    private String encodeUrl(String url) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(url, "UTF-8");
        return result;
    }

    /**
     * Decodiert eine URL.
     *
     * @param url
     *            ist die zu decodierende Url
     * @return die decodierte Url
     * @throws UnsupportedEncodingException
     *             sofern das Encoding nicht unterstuetzt wird.
     */
    private String decodeUrl(String url) throws UnsupportedEncodingException {
        String result = URLDecoder.decode(url, "UTF-8");
        return result;
    }

    /**
     * Ermittelt den gesamten aktuellen Anfrage-Url-Pfad einschliesslich HTTP-GET-Parametern.
     *
     * @param request
     *            ist der {@link HttpServletRequest}
     * @return ist der gesamte aktuelle Anfrage-Url-Pfad.
     */
    private String getGesamteRequestUrl(HttpServletRequest request) {
        String gesamteUri = request.getRequestURI();
        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            gesamteUri += "?" + request.getQueryString();
        }
        return gesamteUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Parameter "urlsToSkip"
        String urlsToSkipWert =
            filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URLS_TO_SKIP);
        if (!Strings.isNullOrEmpty(urlsToSkipWert)) {
            this.urlsToSkip =
                Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(urlsToSkipWert));
        }

        // Parameter "urlApplicationInitialisierung" (Pflicht)
        String urlConfig =
            filterConfig
                .getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URL_APPLICATION_INITIALISIERUNG);
        if (!Strings.isNullOrEmpty(urlConfig)) {
            this.urlApplicationInitialisierung = urlConfig;
        } else {
            throw new ApplicationInitialisierungFilterException(
                FehlerSchluessel.FEHLER_INITIALISIERUNGSFILTER_PARAMETER_FEHLT_ODER_UNGUELTIG,
                GuiParameterSchluessel.FILTER_PARAMETER_URL_APPLICATION_INITIALISIERUNG);
        }

        // Parameter "ajaxRedirectUrl" (Pflicht)
        this.ajaxRedirectUrl =
            filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_AJAX_REDIRECT_URL);
        if (Strings.isNullOrEmpty(this.ajaxRedirectUrl)) {
            throw new ApplicationInitialisierungFilterException(
                FehlerSchluessel.FEHLER_INITIALISIERUNGSFILTER_PARAMETER_FEHLT_ODER_UNGUELTIG,
                GuiParameterSchluessel.FILTER_PARAMETER_AJAX_REDIRECT_URL);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        // Nichts zutun.
    }
}
