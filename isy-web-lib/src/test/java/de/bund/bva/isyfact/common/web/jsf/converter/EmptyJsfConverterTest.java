package de.bund.bva.isyfact.common.web.jsf.converter;

import org.junit.Test;

import static org.junit.Assert.assertNull;

public class EmptyJsfConverterTest {

    @Test
    public void getAsObject() {
        EmptyJsfConverter emptyJsfConverter = new EmptyJsfConverter();

        assertNull(emptyJsfConverter.getAsObject(null, null, null));
        assertNull(emptyJsfConverter.getAsObject(null, null, "someValue"));
    }

    @Test
    public void getAsString() {
        EmptyJsfConverter emptyJsfConverter = new EmptyJsfConverter();

        assertNull(emptyJsfConverter.getAsString(null, null, null));
        assertNull(emptyJsfConverter.getAsString(null, null, new Object()));
    }
}