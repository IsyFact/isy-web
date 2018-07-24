package de.bund.bva.isyfact.common.web.jsf.components.datatable;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataTableFilterModelTest {

    @Test
    public void leeresFilterModel() {
        Map<String, String> filter = new HashMap<>();
        DataTableFilterModel filterModel = new DataTableFilterModel();

        assertTrue(filterModel.isEmpty());

        filter.put("filter1", "");
        filterModel.setFilters(filter);
        assertTrue(filterModel.isEmpty());

        filter.put("filter1", "    ");
        assertTrue(filterModel.isEmpty());

        filter.put("filter2", "wert");
        assertFalse(filterModel.isEmpty());

        assertEquals(filter, filterModel.getFilters());
    }

}