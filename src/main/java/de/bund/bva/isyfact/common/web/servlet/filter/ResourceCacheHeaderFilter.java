package de.bund.bva.isyfact.common.web.servlet.filter;

import java.io.IOException;
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
import org.springframework.stereotype.Component;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;

/**
 * This class re-sets the HTTP headers for resources to be cached, overriding the no-cache pragma
 * and the analogous cache control values. The expiration time is configurable.
 */
@Component
public class ResourceCacheHeaderFilter implements Filter {

    private Log log = LogFactory.getLog(ResourceCacheHeaderFilter.class);

    /**
     * Access to the configuration.
     */
    private Konfiguration konfiguration;

    /** Is a list of url paths to cache, relative to the application context root. */
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
        // parameter "urlsToCache"
        String urlsToCacheWert =
            filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URLS_TO_CACHE);

        if (!Strings.isNullOrEmpty(urlsToCacheWert)) {
            this.urlsToCache =
                Lists.newArrayList(Splitter.on(",").omitEmptyStrings().trimResults().split(urlsToCacheWert));
        }
    }

    /**
     * Checks if the request url is to be cached.
     *
     * @param requestUrlGesamt
     *            request url
     * @param applicationContextPfad
     *            application context path
     * @return <code>true</code>, if the request url is to be cached. Otherwise, <code>false</code>.
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
            // this is a url that is to be cached
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
                                    System.currentTimeMillis() + (Long.parseLong(werte[1]) * 86400000));
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

        // continues the filter chain
        chain.doFilter(request, response);
    }

    /**
     * Gets the entire current request url path including HTTP GET parameters.
     *
     * @param request
     *            is the {@link HttpServletRequest}
     * @return the entire current request url path.
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
    public void destroy() {
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

}
