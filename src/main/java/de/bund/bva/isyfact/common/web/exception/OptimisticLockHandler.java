package de.bund.bva.isyfact.common.web.exception;

import de.bund.bva.isyfact.common.web.global.GlobalFlowController;
import de.bund.bva.isyfact.common.web.konstanten.FehlerSchluessel;
import de.bund.bva.pliscommon.util.spring.MessageSourceHolder;

import javax.persistence.OptimisticLockException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.engine.SubflowState;
import org.springframework.webflow.engine.support.TransitionExecutingFlowExecutionExceptionHandler;
import org.springframework.webflow.execution.RequestContext;

/**
 * Diese Klasse dient der Behandlung von {@link ObjectOptimisticLockingFailureException}'s, insbesondere wenn diese
 * durch eine {@link OptimisticLockException} hervorgerufen wurde. In diesem Fall wird eine entsprechende
 * Meldung ausgegeben und eine Transition zu einem anderen State durchgeführt. In allen anderen Fällen wird
 * zum "fehler"-Flow navigiert.
 * <p>
 * Die Weiterleitung bei einer OptimisticLockExceptions versucht zuerst zu einem State mit der ID
 * "abbrechenEndState" zu navigieren. Dieser wird in modalen Dialogen auch häufig verwendet, um diesen zu
 * schließen und zum aufrufenden Flow zurück zu kehren. Gibt es keinen entsprechenden Zustand im Flow, so gibt
 * es einen Fallback: Es wird der Start-Zustand des aktuellen Flows aktiviert, oder - falls dieser ein
 * Subflow-State ist und nur eine Transition hat - zu dem Ziel-Zustand der einen Transition navigiert.
 * <p>
 * Eingebunden wird dieser Handler durch eine Definition im "parentFlow", der einen
 * {@link TransitionExecutingFlowExecutionExceptionHandler} erzeugt.
 *
 * @author Timo Brandes, msg
 */
@Component
public class OptimisticLockHandler {

    private static final String STATE_FEHLER_ALLGEMEIN = "fehler";

    private static final String STATE_FEHLER_KONKURRIERENDER_ZUGRIFF = "abbrechenEndState";

    @Autowired
    GlobalFlowController globalFlowController;

    /**
     * Sucht im übergebenen {@link Throwable} rekursiv nach einer {@link ObjectOptimisticLockingFailureException} und
     * falls es sich um einen konkurrierenden Zugriff handelt, wird dieser gesondert behandelt.
     *
     * @param t Throwable
     * @param context der aktuelle {@link RequestContext}
     * @return die ID des Flow-States, zu dem gewechselt werden soll
     */
    public String behandleException(Throwable t, RequestContext context) {

        ObjectOptimisticLockingFailureException objectOptimisticLockingFailureException = findObjectOptimisticLockingFailureException(t);

        if (objectOptimisticLockingFailureException != null) {
            return behandleKonkurrierendenZugriffFehler(context);
        }

        return STATE_FEHLER_ALLGEMEIN;
    }

    /**
     * Sucht nach der ersten {@link ObjectOptimisticLockingFailureException} im Stacktrace des übergebenen {@link
     * Throwable}s.
     *
     * @param t Throwable
     * @return {@link ObjectOptimisticLockingFailureException} oder <code>null</code>, falls keine gefunden wurde
     */
    private ObjectOptimisticLockingFailureException findObjectOptimisticLockingFailureException(Throwable t) {

        if (t == null || t.equals(t.getCause())) {
            return null;
        }
        if (t instanceof ObjectOptimisticLockingFailureException) {
            return (ObjectOptimisticLockingFailureException) t;
        }
        return findObjectOptimisticLockingFailureException(t.getCause());
    }

    /**
     * Gibt eine Meldung aus und die ID des Ziel-Zustand zurück. Dies ist entweder
     * <ul>
     * <li>der Zustand "abbrechenEndState", falls dieser definiert ist oder</li>
     * <li>der Ziel-Zustand des Start-Zustands des Flows, wenn dieser Start-Zustand ein Subflow-State ist und
     * nur eine Transition besitzt oder</li>
     * <li>der Start-Zustand des Flows</li>
     * </ul>
     *
     * @param context der aktuelle {@link RequestContext}
     * @return die ID des Ziel-Zustands
     */
    private String behandleKonkurrierendenZugriffFehler(RequestContext context) {

        globalFlowController.getMessageController().writeErrorMessage(MessageSourceHolder
                        .getMessage(FehlerSchluessel.KONKURRIERENDER_ZUGRIFF),
                MessageSourceHolder.getMessage(FehlerSchluessel.FEHLERTEXT_GUI_TECHNISCH_UEBERSCHRIFT));

        boolean abbrechenEndStateVorhanden = true;
        try {
            context.getActiveFlow().getState(STATE_FEHLER_KONKURRIERENDER_ZUGRIFF);
        } catch (IllegalArgumentException e) {
            abbrechenEndStateVorhanden = false;
        }

        if (abbrechenEndStateVorhanden) {
            return STATE_FEHLER_KONKURRIERENDER_ZUGRIFF;
        }

        StateDefinition startState = context.getActiveFlow().getStartState();
        if (startState instanceof SubflowState) {
            SubflowState subflowState = (SubflowState) startState;
            if (subflowState.getTransitionSet().size() == 1) {
                return subflowState.getTransitionSet().iterator().next().getTargetStateId();
            }
        }

        return startState.getId();
    }

}