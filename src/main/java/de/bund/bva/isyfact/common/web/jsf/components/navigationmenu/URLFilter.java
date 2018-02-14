package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

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

import org.springframework.beans.factory.annotation.Required;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;

/**
 *
 * Der URLFilter überprüft zu bei jedem Request in welchem Menüpunkt man sich befindet
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public class URLFilter implements Filter {

    /**
     * Ein Holder, welcher alle Informationen zum erstellen eineres Navigationsmenüs besitzt.
     */
    private NavigationMenuModelHolder navigationMenuModelHolder;

    /**
     * Liste von url-requests welche nicht berücksichtigt werden. (Requests auf Ressourcen)
     */
    private List<String> urlsToSkip = new ArrayList<>();

    /**
     * Initialisierung des Filters. Erstellen der Liste urlsToSkip
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
    }

    /**
     * Ruft die Methode checkActivApplication auf
     *
     **/
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        checkActivApplication(req.getRequestURI());
        chain.doFilter(request, response);
    }

    /**
     * Überprüft ob die URI einer Request einer Applikation (oder einer ihrer Anwendungen) zugeordnet werden
     * kann und setzt die Applikation mit dem ersten Treffer auf Aktiv und alle anderen auf false.
     *
     * @param URI
     *            Die URI einer Request
     *
     */
    public void checkActivApplication(String URI) {
        boolean check = checkURL(URI);
        boolean firstfound = false;
        if (check) {
            NavigationMenuModel model = this.navigationMenuModelHolder.getNavigationMenuModel();
            if (model != null) {
                for (Applikation app : model.getApplikationsListe()) {
                    app.setAktiv(false);
                    if (!firstfound) {
                        if (URI.contains(app.getLink())) {
                            app.setAktiv(true);
                            firstfound = true;
                        }
                        for (Anwendung anw : app.getAnwendungen()) {
                            if (URI.contains(anw.getLink())) {
                                app.setAktiv(true);
                                firstfound = true;
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Überprüft ob die Request überprüft werden soll oder nicht. (Zum Beispiel Request auf Ressourcen)
     *
     * @return true= soll überprüft werden false= soll nicht überprüft werden
     */
    private boolean checkURL(String URI) {
        for (String urls : this.urlsToSkip) {
            if (URI.contains(urls)) {
                return false;
            }
        }
        return true;
    }

    @Required
    public void setNavigationMenuModelHolder(NavigationMenuModelHolder navigationMenuModelHolder) {
        this.navigationMenuModelHolder = navigationMenuModelHolder;
    }

    @Override
    public void destroy() {

    }

}
