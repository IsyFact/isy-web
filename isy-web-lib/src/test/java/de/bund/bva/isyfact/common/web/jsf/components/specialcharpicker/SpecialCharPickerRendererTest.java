package de.bund.bva.isyfact.common.web.jsf.components.specialcharpicker;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// The tests in this class fail in the pipeline with a NullPointerException (Cannot invoke "java.util.Map.get(Object)" because "this.descriptors" is null")
// They work locally. As isy-web is deprecated, this will not be fixed anymore.
@Ignore
public class SpecialCharPickerRendererTest {

    @Test
    public void testIstBasisZeichen() {
        final SpecialCharPickerRenderer specialCharPickerRenderer = new SpecialCharPickerRenderer();

        final SpecialCharPickerWidget specialCharPickerWidget = new SpecialCharPickerWidget();
        final Collection<Character> basiszeichenListe = specialCharPickerWidget.getBasiszeichenListe();

        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "A", basiszeichenListe));
        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "a", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","ā", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","Ă", basiszeichenListe));

        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "I", basiszeichenListe));
        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "i", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","İ", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","ı", basiszeichenListe));

        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "Z", basiszeichenListe));
        assertTrue(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen", "z", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","Ž", basiszeichenListe));
        assertFalse(ReflectionTestUtils.invokeMethod(specialCharPickerRenderer, "istBasisZeichen","ź", basiszeichenListe));
    }
}
