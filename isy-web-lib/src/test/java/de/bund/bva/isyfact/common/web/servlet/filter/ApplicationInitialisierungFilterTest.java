package de.bund.bva.isyfact.common.web.servlet.filter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLEncoder;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.bund.bva.isyfact.common.web.konstanten.GuiParameterSchluessel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationInitialisierungFilterTest {

    @Mock
    private FilterConfig filterConfig;

    @Mock
    private HttpServletRequest httpRequest;

    @Mock
    private HttpServletResponse httpResponse;

    @Mock
    private HttpSession httpSession;

    @Mock
    private FilterChain filterChain;

    private static final String initialisierungSeite = "/init";

    private static final String ajaxRedirectUrl = "/ajaxRedirect";

    @Before
    public void setup() {
        when(filterConfig
            .getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URL_APPLICATION_INITIALISIERUNG))
            .thenReturn(initialisierungSeite);
        when(filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_AJAX_REDIRECT_URL))
            .thenReturn(ajaxRedirectUrl);

        when(httpRequest.getSession()).thenReturn(httpSession);
    }

    @Test
    public void doFilterSkipUrls() throws Exception {

        when(filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URLS_TO_SKIP))
            .thenReturn("/url1,/url2");

        when(httpRequest.getContextPath()).thenReturn("/contextPath");
        when(httpRequest.getRequestURI()).thenReturn("/contextPath/url1");

        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        filter.init(filterConfig);

        filter.doFilter(httpRequest, httpResponse, filterChain);

        verifyNoInteractions(httpResponse);
        verify(filterChain).doFilter(httpRequest, httpResponse);
    }

    @Test(expected = ApplicationInitialisierungFilterException.class)
    public void parameterUrlApplicationInitialisierungNichtVorhanden() throws Exception {
        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        when(filterConfig
            .getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_URL_APPLICATION_INITIALISIERUNG))
            .thenReturn("");

        filter.init(filterConfig);
    }

    @Test(expected = ApplicationInitialisierungFilterException.class)
    public void parameterajaxRedirectUrlNichtVorhanden() throws Exception {
        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        when(filterConfig.getInitParameter(GuiParameterSchluessel.FILTER_PARAMETER_AJAX_REDIRECT_URL))
            .thenReturn("");

        filter.init(filterConfig);
    }

    @Test
    public void doFilterParameterLiegtBereitsInSession() throws Exception {
        when(httpSession.getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(Boolean.TRUE);

        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        filter.init(filterConfig);

        filter.doFilter(httpRequest, httpResponse, filterChain);

        verify(httpSession).getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL);
        verifyNoInteractions(httpResponse);
        verify(filterChain).doFilter(httpRequest, httpResponse);
    }

    @Test
    public void doFilterParameterTrueUeberUrl() throws Exception {
        final String urspruenglicheUrl = "/urspruenglicheUrl";

        when(httpSession.getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(null);
        when(httpRequest.getParameter(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn("true");
        when(httpRequest.getParameter(GuiParameterSchluessel.URSPRUENGLICHE_ANFRAGE_URL))
            .thenReturn(urspruenglicheUrl);

        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        filter.init(filterConfig);

        filter.doFilter(httpRequest, httpResponse, filterChain);

        verify(httpSession).setAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL, true);
        verify(httpRequest).getParameter(GuiParameterSchluessel.URSPRUENGLICHE_ANFRAGE_URL);
        verify(httpResponse).sendRedirect(urspruenglicheUrl);
    }

    @Test
    public void doFilterKeinParameterInSessionOderUrl() throws Exception {
        final String urspruenglicheUrl = "/urspruenglicheUrl";

        when(httpRequest.getRequestURI()).thenReturn(urspruenglicheUrl);
        when(httpSession.getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(null);
        when(httpRequest.getParameter(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(null);
        RequestDispatcher requestDispatcher = mock(RequestDispatcher.class);
        when(httpRequest.getRequestDispatcher(initialisierungSeite)).thenReturn(requestDispatcher);

        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        filter.init(filterConfig);

        filter.doFilter(httpRequest, httpResponse, filterChain);

        String urspruenglicheUrlEncoded = URLEncoder.encode(urspruenglicheUrl, "UTF-8");
        verify(httpRequest)
            .setAttribute(GuiParameterSchluessel.URSPRUENGLICHE_ANFRAGE_URL, urspruenglicheUrlEncoded);
        verify(httpRequest).getRequestDispatcher(initialisierungSeite);
        verify(requestDispatcher).forward(httpRequest, httpResponse);
    }

    @Test
    public void doFilterKeineParameterInSessionOderUrlAjaxRequest() throws Exception {
        when(httpSession.getAttribute(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(null);
        when(httpRequest.getParameter(GuiParameterSchluessel.JAVASCRIPT_AKTIVIERT_SCHLUESSEL))
            .thenReturn(null);
        when(httpRequest.getHeader("Faces-Request")).thenReturn("partial/ajax");

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(httpResponse.getWriter()).thenReturn(printWriter);

        ApplicationInitialisierungFilter filter = new ApplicationInitialisierungFilter();

        filter.init(filterConfig);

        filter.doFilter(httpRequest, httpResponse, filterChain);

        verify(httpResponse).setContentType("text/xml");

        final String ajaxRedirectResponse =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><partial-response><redirect url=\"" + ajaxRedirectUrl
                + "\"></redirect></partial-response>";

        assertEquals(ajaxRedirectResponse, stringWriter.toString());
    }

}