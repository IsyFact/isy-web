package de.bund.bva.isyfact.common.web.filetransfer;

import static org.mockito.Mockito.RETURNS_DEFAULTS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.sun.faces.config.WebConfiguration;
import com.sun.faces.context.FacesContextImpl;
import com.sun.faces.lifecycle.LifecycleImpl;

@RunWith(MockitoJUnitRunner.class)
public class DownloadHelperTest {

    static {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    private final DownloadHelper sut = new DownloadHelper();

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private ExternalContext externalContext;

    @Before
    public void mockFacesContext() {
        when(externalContext.getRequest())
                .thenReturn(mock(HttpServletRequest.class, RETURNS_DEFAULTS));
        when(externalContext.getApplicationMap().get("com.sun.faces.config.WebConfiguration"))
                .thenReturn(mock(WebConfiguration.class, RETURNS_DEFAULTS));

        FactoryFinder.setFactory(FactoryFinder.RENDER_KIT_FACTORY, "com.sun.faces.renderkit.RenderKitFactoryImpl");
        new FacesContextImpl(externalContext, new LifecycleImpl());
    }

    @Test
    public void starteDownload_emptyStringInFilename() {
        // prepare
        final FileModel input = new FileModel();
        input.setDateiname("a b.txt");
        input.setInhalt(new byte[0]);
        input.setMimeType("plain/text");

        // execute
        sut.starteDownload(input);

        // verify
        verify(externalContext).setResponseHeader(
                "Content-Disposition", "attachment;filename=\"a%20b.txt\"; filename*=UTF-8''a%20b.txt");
    }
}
