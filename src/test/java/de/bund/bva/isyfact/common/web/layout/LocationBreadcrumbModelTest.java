package de.bund.bva.isyfact.common.web.layout;

import org.junit.Test;

import static org.junit.Assert.*;

public class LocationBreadcrumbModelTest {

    @Test
    public void getHierarchieebenen() {
        LocationBreadcrumbModel model = new LocationBreadcrumbModel();

        assertEquals("", model.getHierarchieebene());

        model.pushHierarchiebene("Ebene 1");

        assertEquals("Ebene 1", model.getHierarchieebene());

        model.pushHierarchiebene("Ebene 2");

        assertEquals("Ebene 1 - Ebene 2", model.getHierarchieebene());

        assertEquals("Ebene 2", model.popHierarchieebene());
        assertEquals("Ebene 1", model.getHierarchieebene());

        model.clearHierarchieebenen();

        assertEquals("", model.getHierarchieebene());
    }
}