package de.bund.bva.isyfact.common.web.exception.web;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import de.bund.bva.isyfact.common.web.MockFacesContext;
import de.bund.bva.isyfact.common.web.exception.common.AusnahmeIdMapper;
import de.bund.bva.isyfact.common.web.exception.common.FehlertextProviderImpl;
import de.bund.bva.isyfact.exception.FehlertextProvider;
import de.bund.bva.isyfact.exception.BaseException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ErrorControllerTest {

    private static final String REQUEST_PARAMETER_UNIQUE_ID = "uniqueId";

    private static final String UNIQUE_ID = "testUniqueId";

    private static final String REQUEST_PARAMETER_FEHLER_ID = "fehlerId";

    private static final String FEHLER_ID = "testFehlerId";

    private static final String ERGEBNIS_NACHRICHT = "testFehlerNachricht";

    private static final String ERGEBNIS_UEBERSCHRIFT = "testFehlerUeberschrift";

    private Map<String, String> requestParameterMap = new HashMap<>();

    private FehlertextProvider fehlertextProviderMock = new FehlertextProviderImpl();

    @Before
    public void setupRequestParameterMap() {
        FacesContext context = MockFacesContext.mockFacesContext();

        requestParameterMap.clear();
        ExternalContext externalContext = mock(ExternalContext.class);

        when(externalContext.getRequestParameterMap()).thenReturn(requestParameterMap);
        when(context.getExternalContext()).thenReturn(externalContext);
    }

    @Test
    public void initialisiereModel() {
        requestParameterMap.put(REQUEST_PARAMETER_UNIQUE_ID, UNIQUE_ID);
        requestParameterMap.put(REQUEST_PARAMETER_FEHLER_ID, FEHLER_ID);

        ErrorModel model = new ErrorModel();

        ErrorController errorController = new ErrorController();

        errorController.initialisiereModel(model, null);

        assertEquals(ERGEBNIS_NACHRICHT, model.getFehlerText());
        assertEquals(ERGEBNIS_UEBERSCHRIFT, model.getFehlerUeberschrift());
    }

    @Test
    public void initialisiereModelKeinFehlerImRequest() {

        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        AusnahmeIdMapper ausnahmeIdMapperMock = mock(AusnahmeIdMapper.class);

        BaseException exception = mock(BaseException.class);

        when(exception.getAusnahmeId()).thenReturn(FEHLER_ID);
        when(exception.getFehlertext()).thenReturn(ERGEBNIS_NACHRICHT);
        when(exception.getUniqueId()).thenReturn(UNIQUE_ID);

        when(applicationContextMock.getBean(AusnahmeIdMapper.class)).thenReturn(ausnahmeIdMapperMock);

        ErrorModel model = new ErrorModel();

        ErrorController errorController = new ErrorController();

        errorController.setApplicationContext(applicationContextMock);

        errorController.initialisiereModel(model, exception);

        assertEquals(ERGEBNIS_NACHRICHT, model.getFehlerText());
        assertEquals(ERGEBNIS_UEBERSCHRIFT, model.getFehlerUeberschrift());
    }

    @Test
    public void initialisiereModelSocketException() throws Exception {
        ApplicationContext applicationContextMock = mock(ApplicationContext.class);
        AusnahmeIdMapper ausnahmeIdMapperMock = mock(AusnahmeIdMapper.class);

        SocketException socketException = new SocketException();

        when(applicationContextMock.getBean(AusnahmeIdMapper.class)).thenReturn(ausnahmeIdMapperMock);
        when(ausnahmeIdMapperMock.getFehlertextProvider()).thenReturn(fehlertextProviderMock);
        when(ausnahmeIdMapperMock.getFallbackAusnahmeId()).thenReturn(FEHLER_ID);

        ErrorModel model = new ErrorModel();

        ErrorController errorController = new ErrorController();

        errorController.setApplicationContext(applicationContextMock);

        errorController.initialisiereModel(model, socketException);

        assertEquals(ERGEBNIS_NACHRICHT, model.getFehlerText());
        assertEquals(ERGEBNIS_UEBERSCHRIFT, model.getFehlerUeberschrift());
    }
}