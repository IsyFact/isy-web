package de.bund.bva.isyfact.common.web.jsf.converter;

import de.bund.bva.isyfact.common.web.jsf.components.datatable.SortDirection;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SortDirectionJsfConverterTest {

    @Test
    public void getAsObject() {
        SortDirectionJsfConverter sortDirectionJsfConverter = new SortDirectionJsfConverter();

        assertNull(sortDirectionJsfConverter.getAsObject(null, null, null));
        assertNull(sortDirectionJsfConverter.getAsObject(null, null, ""));
        assertEquals(SortDirection.ASCENDING,
            sortDirectionJsfConverter.getAsObject(null, null, SortDirection.ASCENDING.toString()));
        assertEquals(SortDirection.DESCENDING,
            sortDirectionJsfConverter.getAsObject(null, null, SortDirection.DESCENDING.toString()));
    }

    @Test
    public void getAsString() {
        SortDirectionJsfConverter sortDirectionJsfConverter = new SortDirectionJsfConverter();

        assertEquals("", sortDirectionJsfConverter.getAsString(null, null, null));
        assertEquals(SortDirection.ASCENDING.toString(),
            sortDirectionJsfConverter.getAsString(null, null, SortDirection.ASCENDING));
        assertEquals(SortDirection.DESCENDING.toString(),
            sortDirectionJsfConverter.getAsString(null, null, SortDirection.DESCENDING));
    }
}