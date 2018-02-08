package de.bund.bva.isyfact.common.web;

import javax.faces.context.FacesContext;

import org.mockito.Mockito;

public abstract class MockFacesContext extends FacesContext {

    public static FacesContext mockFacesContext() {
        FacesContext context = Mockito.mock(FacesContext.class);
        setCurrentInstance(context);

        return context;
    }
}
