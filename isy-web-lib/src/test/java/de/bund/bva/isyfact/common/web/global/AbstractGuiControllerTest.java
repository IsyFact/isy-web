package de.bund.bva.isyfact.common.web.global;

import java.util.Objects;

import org.junit.Before;
import org.junit.Test;
import org.springframework.webflow.core.collection.LocalAttributeMap;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.RequestContextHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class AbstractGuiControllerTest {

    static class TestAbstractMaskenModel extends AbstractMaskenModel {
        int id = 1;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            TestAbstractMaskenModel that = (TestAbstractMaskenModel) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {

            return Objects.hash(id);
        }
    }

    private AbstractGuiController controller = mock(AbstractGuiController.class, CALLS_REAL_METHODS);

    private RequestContext requestContext = mock(RequestContext.class);

    private static final MutableAttributeMap<Object> flowScope = new LocalAttributeMap<>();

    @Before
    public void setup() {
        RequestContextHolder.setRequestContext(requestContext);
        when(controller.getMaskenModelKlasseZuController()).thenReturn(TestAbstractMaskenModel.class);
        flowScope.clear();
    }

    @Test
    public void getMaskenModelZuController() {
        TestAbstractMaskenModel testAbstractMaskenModel = new TestAbstractMaskenModel();

        flowScope.put("1", new Object());
        flowScope.put("2", "test");
        flowScope.put("3", testAbstractMaskenModel);
        flowScope.put("4", null);

        when(requestContext.getFlowScope()).thenReturn(flowScope);

        assertEquals(testAbstractMaskenModel, controller.getMaskenModelZuController());
    }

    @Test
    public void getMaskenModelZuControllerNull() {
        flowScope.put("1", new Object());
        flowScope.put("2", "test");
        flowScope.put("3", new Integer(1));

        when(requestContext.getFlowScope()).thenReturn(flowScope);

        assertNull(controller.getMaskenModelZuController());
    }
}