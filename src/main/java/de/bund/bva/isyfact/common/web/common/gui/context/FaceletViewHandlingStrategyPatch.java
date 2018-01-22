package de.bund.bva.isyfact.common.web.common.gui.context;

import static javax.faces.application.StateManager.IS_BUILDING_INITIAL_STATE;
import static java.lang.Boolean.TRUE;

/**
 * Dieser Patch behebt einen Fehler in Mojarra, der dazu führt, dass in bestimmten Fällen keine Parameter
 * an einen Controller-Aufruf übergeben werden.
 * <p>Details siehe
 * <a href="https://stackoverflow.com/questions/44633039/jsf-composite-component-target-actions-fail-within-the-cforeach-tag">hier</a>
 * </p>
 */
public class FaceletViewHandlingStrategyPatch extends javax.faces.view.ViewDeclarationLanguageFactory {

    /** Original ViewDevlarationLanguageFactory, */
    private javax.faces.view.ViewDeclarationLanguageFactory wrapped;

    /**
     * Erzeugt eine Instanz des Patchsm der die original ViewDeclatationLanguage verpackt.
     * @param wrapped Original ViewDevlarationLanguageFactory,
     */
    public FaceletViewHandlingStrategyPatch(javax.faces.view.ViewDeclarationLanguageFactory wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public javax.faces.view.ViewDeclarationLanguage getViewDeclarationLanguage(String viewId) {
        return new FaceletViewHandlingStrategyWithRetargetMethodExpressionsPatch(getWrapped().getViewDeclarationLanguage(viewId));
    }

    @Override
    public javax.faces.view.ViewDeclarationLanguageFactory getWrapped() {
        return wrapped;
    }

    /**
     * Eine erweiterte ViewDeclarationLanguage, die die original ViewDeclarationLanguage umverpackt
     * und bei der Auswertung von EL-Ausdrücken den richtigen Kontext übergibt.
     */
    private static class FaceletViewHandlingStrategyWithRetargetMethodExpressionsPatch extends
        javax.faces.view.ViewDeclarationLanguageWrapper {

        /** original ViewDeclarationLanguage */
        private javax.faces.view.ViewDeclarationLanguage wrapped;

        /**
         * Konstruktor mit der zu verpackenden original ViewDeclarationLanguage
         * @param wrapped original ViewDeclarationLanguage
         */
        public FaceletViewHandlingStrategyWithRetargetMethodExpressionsPatch(
            javax.faces.view.ViewDeclarationLanguage wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public void retargetMethodExpressions(javax.faces.context.FacesContext context, javax.faces.component.UIComponent topLevelComponent) {
            super.retargetMethodExpressions(new FacesContextWithFaceletContextAsELContext(context), topLevelComponent);
        }

        @Override
        public javax.faces.view.ViewDeclarationLanguage getWrapped() {
            return wrapped;
        }
    }


    /**
     * Ein erweiterter FacesContext, der in der Build View Phase bevorzugt den aktuellen FaceletKontext ausliefert,
     * anstelle des globalen FacesContext.elContext. Dabei wird ausgenutzt, dass der aktuelle FaceletContext von
     * Mojarra immer in einem Attribut des FacesContext vermerkt wird.
     */
    private static class FacesContextWithFaceletContextAsELContext extends javax.faces.context.FacesContextWrapper {

        /** original FacesContext */
        private javax.faces.context.FacesContext wrapped;

        /**
         * Konstruktor mit den original FacesContext
         * @param wrapped original FacesContext
         */
        public FacesContextWithFaceletContextAsELContext(javax.faces.context.FacesContext wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public javax.el.ELContext getELContext() {
            boolean isViewBuildTime  = TRUE.equals(getWrapped().getAttributes().get(IS_BUILDING_INITIAL_STATE));
            javax.faces.view.facelets.FaceletContext
                faceletContext = (javax.faces.view.facelets.FaceletContext) getWrapped().getAttributes().get(
                javax.faces.view.facelets.FaceletContext.FACELET_CONTEXT_KEY);
            return (isViewBuildTime && faceletContext != null) ? faceletContext : super.getELContext();
        }

        @Override
        public javax.faces.context.FacesContext getWrapped() {
            return wrapped;
        }
    }
}
