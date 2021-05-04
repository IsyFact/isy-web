package de.bund.bva.isyfact.common.web.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.webflow.core.FlowException;
import org.springframework.webflow.execution.FlowExecutionException;
import org.springframework.webflow.execution.repository.FlowExecutionRestorationFailureException;
import org.springframework.webflow.mvc.servlet.AbstractFlowHandler;
import org.springframework.webflow.mvc.servlet.FlowHandler;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextUtil;
import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;

/**
 * Standard-IsyFactFlowHandlerMapping der IsyFact, das im Gegensatz zum {@link FlowHandlerMapping} einige
 * grundlegende Ausnahmen behandelt.
 * Konkret werden {@link FlowExecutionRestorationFailureException} und {@link FlowExecutionException} mit
 * einer {@link AccessDeniedException} als Grund behandelt.
 * Für diese Ausnahmen können Flows definiert werden, die als Ergebnis des Handlings angesprungen werden.
 */
public class IsyFactFlowHandlerMapping extends FlowHandlerMapping {

    private static final IsyLogger LOG = IsyLoggerFactory.getLogger(IsyFactFlowHandlerMapping.class);

    /** Mapper für Fehlerschlüssel von Ausnahmen. */
    private AusnahmeIdMapper ausnahmeIdMapper;

    /** Flow, der im Falle einer {@link FlowExecutionRestorationFailureException} angesprungen wird. */
    private String snapshotNotFoundFlow;

    /**
     * Flow, der im Falle einer {@link FlowExecutionException} mit einer {@link AccessDeniedException} als
     * Grund angesprungen wird.
     */
    private String accessDeniedFlow;

    public IsyFactFlowHandlerMapping(AusnahmeIdMapper ausnahmeIdMapper, String snapshotNotFoundFlow,
                                     String accessDeniedFlow) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
        this.snapshotNotFoundFlow = snapshotNotFoundFlow;
        this.accessDeniedFlow = accessDeniedFlow;
    }

    @Override
    protected FlowHandler createDefaultFlowHandler(String flowId) {
        return new DefaultExceptionHandlingFlowHandler(flowId, snapshotNotFoundFlow, accessDeniedFlow);
    }

    public void setAusnahmeIdMapper(AusnahmeIdMapper ausnahmeIdMapper) {
        this.ausnahmeIdMapper = ausnahmeIdMapper;
    }

    public void setSnapshotNotFoundFlow(String snapshotNotFoundFlowId) {
        this.snapshotNotFoundFlow = snapshotNotFoundFlowId;
    }

    public void setAccessDeniedFlow(String accessDeniedFlowId) {
        this.accessDeniedFlow = accessDeniedFlowId;
    }

    /**
     * {@link FlowHandler}, der allgemeine Exceptions behandelt und auf die entsprechenden Flows weiterleitet.
     */
    private class DefaultExceptionHandlingFlowHandler extends AbstractFlowHandler {

        private final String flowId;

        private final String snapshotNotFoundFlowId;

        private final String accessDeniedFlowId;

        DefaultExceptionHandlingFlowHandler(String flowId, String snapshotNotFoundFlowId,
            String accessDeniedFlowId) {
            this.flowId = flowId;
            this.snapshotNotFoundFlowId = snapshotNotFoundFlowId;
            this.accessDeniedFlowId = accessDeniedFlowId;
        }

        @Override
        public String getFlowId() {
            return flowId;
        }

        @Override
        public String handleException(FlowException e, HttpServletRequest request,
            HttpServletResponse response) {
            if (e instanceof FlowExecutionRestorationFailureException) {
                FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(e, ausnahmeIdMapper, LOG);
                return this.snapshotNotFoundFlowId;
            } else if (e instanceof FlowExecutionException && e.getCause() != null && e
                .getCause() instanceof AccessDeniedException) {
                FehlertextUtil.schreibeLogEintragUndErmittleFehlerinformation(e, ausnahmeIdMapper, LOG);
                return this.accessDeniedFlowId;
            } else {
                return super.handleException(e, request, response);
            }
        }

    }
}
