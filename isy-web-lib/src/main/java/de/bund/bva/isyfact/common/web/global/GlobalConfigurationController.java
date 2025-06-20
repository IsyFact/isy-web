package de.bund.bva.isyfact.common.web.global;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContextHolder;

import com.google.common.base.Strings;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;

/**
 * Der Controller zum Verwalten von globalen Einstellungen.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class GlobalConfigurationController implements GuiController {

    /**
     * Initialsiert das GlobalConfigurationModel.
     * @return Das GlobalConfigurationModel.
     */
    public GlobalConfigurationModel initializeConfigurationModel() {
        GlobalConfigurationModel globalConfigurationModel = new GlobalConfigurationModel();

        // Registriere JavaScript: de-/aktiviert
        registriereJavascriptAktiviert(globalConfigurationModel);

        return globalConfigurationModel;
    }

    /**
     * Registiert die De-/Aktivierung von Javascript im {@link GlobalConfigurationModel}.
     *
     * @param globalConfigurationModel
     *            it das {@link GlobalConfigurationModel}
     */
    private void registriereJavascriptAktiviert(GlobalConfigurationModel globalConfigurationModel) {
        Map<String, Object> sessionMap =
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
        Object jsEnabledObject = sessionMap.get(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL);
        if (jsEnabledObject instanceof Boolean) {
            Boolean jsEnabled = (Boolean) jsEnabledObject;
            globalConfigurationModel.setJsEnabled(jsEnabled);
        }
    }

    /**
     * Extrahiert ein Model vom Typ {@link GlobalConfigurationModel} aus dem FlowScope.
     * @return das {@link GlobalConfigurationModel} oder <code>null</code>, falls dieses nicht gefunden wurde.
     */
    public GlobalConfigurationModel getModelZuController() {

        MutableAttributeMap<Object> mutableAttributeMap =
            RequestContextHolder.getRequestContext().getFlowScope();

        return (GlobalConfigurationModel) mutableAttributeMap.get("globalConfigurationModel");
    }

}
