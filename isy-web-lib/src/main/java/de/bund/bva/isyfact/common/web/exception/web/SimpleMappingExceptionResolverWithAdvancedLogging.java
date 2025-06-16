package de.bund.bva.isyfact.common.web.exception.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * {@link SimpleMappingExceptionResolver}, der Ausnahmen in AJAX-Requests behandelt und ins Log schreibt.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class SimpleMappingExceptionResolverWithAdvancedLogging extends SimpleMappingExceptionResolver {

    private static final IsyLogger LOG =
        IsyLoggerFactory.getLogger(SimpleMappingExceptionResolverWithAdvancedLogging.class);

    /** Mapper für Fehlerschlüssel von Ausnahmen. */
    private AusnahmeIdMapper ausnahmeIdMapper;

    public SimpleMappingExceptionResolverWithAdvancedLogging(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

    @Override
    protected void logException(Exception ex, HttpServletRequest request) {
        FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(ex, this.ausnahmeIdMapper, LOG);
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response,
        Object handler, Exception ex) {
        // Falls dieser Request über AJAX aufgerufen wurde, führe ein Redirect aus.
        if ("partial/ajax".equals(request.getHeader("Faces-Request"))) {
            // It's a JSF ajax request
            response.setContentType("text/xml");
            // Gib eine View zurück, die so nicht gerendert werden kann
            // Dadurch erfolgt ein Redirect auf die allgemeine Fehlerseite
            return new ModelAndView("common/flow/error/ajaxErrorRedirect").addObject("exception", ex);
        }

        return super.doResolveException(request, response, handler, ex);
    }

    public void setAusnahmeIdMapper(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

}
