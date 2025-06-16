package de.bund.bva.isyfact.common.web.exception.web;

import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerFactory;
import javax.faces.context.FacesContext;

import org.springframework.web.jsf.FacesContextUtils;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;

/**
 * Die Factory zum Erzeugen eines JSF-Exception Handlers.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class JsfExceptionHandlerFactory extends ExceptionHandlerFactory {

    /**
     * Die Parent-Factory.
     */
    private ExceptionHandlerFactory parent;

    /**
     * Konstruktor. Wird durch JSF aufgerufen.
     *
     * @param parent
     *            Die Parent-Factory
     */
    public JsfExceptionHandlerFactory(ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {

        AusnahmeIdMapper ausnahmeIdMapper =
            FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance()).getBean(
                AusnahmeIdMapper.class);

        // Erzeugt einen neuen Exception-Handler
        ExceptionHandler result =
            new JsfExceptionHandler(this.parent.getExceptionHandler(), ausnahmeIdMapper);
        return result;
    }
}
