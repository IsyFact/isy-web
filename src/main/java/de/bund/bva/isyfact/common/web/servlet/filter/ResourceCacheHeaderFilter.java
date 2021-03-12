package de.bund.bva.isyfact.common.web.servlet.filter;

import java.io.IOException;
import java.time.Duration;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * Dieser Klasse setzt für zu cachende Ressourcen die HTTP-Header erneut, sodass das Pragma "no-cache" und die
 * analogen Werte für Cache-Control überschrieben werden. Der Ablaufzeitpunkt ist konfigurierbar.
 */
@Component
public class ResourceCacheHeaderFilter implements Filter {

    private Log log = LogFactory.getLog(ResourceCacheHeaderFilter.class);

    /**
     * Zugriff auf die Konfiguration.
     */
    private Konfiguration konfiguration;

    /** Ist eine Liste mit zu cachenden Url-Pfaden, relativ zum ApplicationContext-Root. */
    private List<String> urlsToCache = new ArrayList<>();

    @Autowired
    public ResourceCacheHeaderFilter(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Parameter "urlsToCache"
        String urlsToCacheWert =
            filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URLS_TO_CACHE);

        if (!Strings.isNullOrEmpty(urlsToCacheWert)) {
            this.urlsToCache =
                Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(urlsToCacheWert));
        }
    }

    /**
     * Prueft, ob die Anfrage-Url zu cachen ist.
     *
     * @param requestUrlGesamt
     *            ist die gesamte Anfrage-Url
     * @param applicationContextPfad
     *            ist der ApplicationContext-Pfad
     * @return <code>true</code>, falls die Url zu cachen ist. <code>false</code> ansonsten.
     */
    private boolean ermittleUrlIstZuCachen(String requestUrlGesamt, String applicationContextPfad) {
        if (this.urlsToCache == null) {
            return false;
        }

        for (String urlToCache : this.urlsToCache) {
            String urlToCacheMitApplicationContextPfad = applicationContextPfad + urlToCache;
            if (requestUrlGesamt.startsWith(urlToCacheMitApplicationContextPfad)) {
                return true;
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestUrlGesamt = getGesamteRequestUrl(httpRequest);
        String applicationContextPfad = httpRequest.getContextPath();

        boolean urlIstZuCachen = ermittleUrlIstZuCachen(requestUrlGesamt, applicationContextPfad);
        if (urlIstZuCachen) {
            // Es handelt sich um eine zu Url, die gecacht werden soll
            List<String> cacheWerte = new ArrayList<>();
            for (String key : this.konfiguration.getSchluessel()) {
                if (key.startsWith("caching.resourcen")) {
                    cacheWerte.add(this.konfiguration.getAsString(key, null));
                }
            }
            if (!cacheWerte.isEmpty()) {
                for (String wert : cacheWerte) {
                    if (wert != null && wert.length() > 0 && wert.contains(":")) {
                        String[] werte = wert.split(":", -1);
                        try {
                            if (werte[0].equals("Expires")) {
                                httpResponse.setDateHeader(werte[0],
                                    System.currentTimeMillis() + Duration.ofDays(Long.parseLong(werte[1])).toMillis());
                            } else {
                                httpResponse.setHeader(werte[0], werte[1]);
                            }
                        } catch (Exception e) {
                            this.log.warn("Der Header " + werte[0] + " konnte nicht gesetzt werden: ", e);
                        }
                    }
                }
            }
        }

        // Setze die Filterkette fort
        chain.doFilter(request, response);
    }

    /**
     * Ermittelt den gesamten aktuellen Anfrage-Url-Pfad einschliesslich HTTP-GET-Parametern.
     *
     * @param request
     *            ist der {@link HttpServletRequest}
     * @return ist der gesamte aktuelle Anfrage-Url-Pfad.
     */
    private String getGesamteRequestUrl(HttpServletRequest request) {
        String gesamteUri = request.getRequestURI().toString();
        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            gesamteUri += "?" + request.getQueryString();
        }
        return gesamteUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

}
