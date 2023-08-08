package de.bund.bva.isyfact.common.web.jsf.components.charpickerdinnorm91379;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

/**
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CharPickerDinNorm91379Controller {

    /**
     * Repository for getting all supported characters.
     */
    private final ZeichenObjektRepository zeichenObjektRepository = new ZeichenObjektRepository();

    public List<String> getBaseSymbols() {
        return zeichenObjektRepository.getZeichenliste().stream()
                .map(ZeichenObjekt::getGrundzeichen)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> getGroups() {
        return zeichenObjektRepository.getZeichenliste().stream()
                .map(ZeichenObjekt::getSchriftzeichengruppe)
                .map(Object::toString)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<ZeichenObjekt> getSymbols() {
        return zeichenObjektRepository.getZeichenliste();
    }

}
