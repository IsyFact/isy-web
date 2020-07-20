package de.bund.bva.isyfact.common.web.layout;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.common.web.global.AbstractGuiController;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.konfiguration.common.Konfiguration;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * Controller-Klasse für den Hilfe-Button.
 * Bietet Funktionen zum Ein- und Ausblenden des Hilfebuttons und zum generieren der URL zum Hilfe-Wiki.
 *
 * @author Florian Mallmann, msg
 * @author Andreas Schubert, msg
 */
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class HilfeController extends AbstractGuiController<HilfeModel> {

    private static final String XWIKI_PATH = "/bin/view";

    private static final String HILFE_DEFAULT_AKTIV = "hilfe.default.aktiv";

    private static final String ISY_PORTALHILFE_URL = "portalhilfe.url";

    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(HilfeController.class);

    private static final FehlertextProvider FEHLERTEXT_PROVIDER = new FehlertextProviderImpl();

    private ServletContext servletContext;

    private Konfiguration konfiguration;

    @Autowired
    public HilfeController(ServletContext servletContext, Konfiguration konfiguration) {
        this.servletContext = servletContext;
        this.konfiguration = konfiguration;
    }

    @Override
    public void initialisiereModel(HilfeModel model) {

        boolean hilfeDefaultAktiv = konfiguration.getAsBoolean(HILFE_DEFAULT_AKTIV, true);
        String isyPortalhilfeUrl = konfiguration.getAsString(ISY_PORTALHILFE_URL, "");
        if (isyPortalhilfeUrl == null || isyPortalhilfeUrl.isEmpty()) {
            model.setHilfeButtonAvailable(false);
            LOG.debug(FEHLERTEXT_PROVIDER
                    .getMessage(FehlerSchluessel.INFO_PARAMETER_HILFEPORTAL_URL_NICHT_GESETZT));
        } else {
            model.setHilfeButtonAvailable(hilfeDefaultAktiv);
        }
    }

    /**
     * Gibt an, ob der Hilfe-Button angezeigt werden soll.
     *
     * @param model Das HilfeModel
     * @return true, wenn der Hilfe-Button in der GUI angezeigt werden soll.
     */
    public boolean isHilfeButtonActive(HilfeModel model) {

        return model.isHilfeButtonAvailable();
    }

    /**
     * Methode zum aktivieren des Hilfe-Icons.
     *
     * @param model Das HilfeModel
     */
    public void aktiviereHilfe(HilfeModel model) {

        model.setHilfeButtonAvailable(true);
    }

    /**
     * Methode zum deaktivieren des Hilfe-Icons.
     *
     * @param model Das HilfeModel
     */
    public void deaktiviereHilfe(HilfeModel model) {

        model.setHilfeButtonAvailable(false);
    }

    /**
     * Generiert die Hilfe URL anhand des aktuellen Flow States
     *
     * @return URL die auf die jeweilige Hilfeseite in der isy-portalhilfe verweist.
     */
    public String ermittleHilfeUrl() {

        String viewState = macheViewStateIdZuUeberschrift(ermittleViewStateId());
        String flow = macheFlowIdZuUeberschrift(ermittleFlowId());
        String isyPortalhilfeUrl = konfiguration.getAsString(ISY_PORTALHILFE_URL, "");
        return isyPortalhilfeUrl + XWIKI_PATH + ermittleContextPath() + "/" + flow + "/" + viewState;
    }

    @Override
    protected Class<HilfeModel> getMaskenModelKlasseZuController() {

        return HilfeModel.class;
    }

    private String ermittleFlowId() {

        RequestContext requestContext = RequestContextHolder.getRequestContext();
        return requestContext.getActiveFlow().getId();
    }

    private String ermittleContextPath() {

        return servletContext.getContextPath();
    }

    private String ermittleViewStateId() {

        RequestContext requestContext = RequestContextHolder.getRequestContext();
        StateDefinition currentState = requestContext.getCurrentState();
        return currentState.getId();
    }

    private String macheFlowIdZuUeberschrift(String flowId) {

        String ueberschrift = macheCamalCaseZuText(flowId);
        return ueberschrift.replace("+flow", "");
    }

    private String macheViewStateIdZuUeberschrift(String viewStateId) {

        String ueberschrift = macheCamalCaseZuText(viewStateId);
        return ueberschrift.replace("+view+state", "");
    }

    private String macheCamalCaseZuText(String text) {

        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1+$2";
        String viewStateUeberschrift = text.replaceAll(regex, replacement).toLowerCase();
        viewStateUeberschrift =
                Character.toUpperCase(viewStateUeberschrift.charAt(0)) + viewStateUeberschrift.substring(1);
        return viewStateUeberschrift;
    }

    public void setKonfiguration(Konfiguration konfiguration) {
        this.konfiguration = konfiguration;
    }

    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}