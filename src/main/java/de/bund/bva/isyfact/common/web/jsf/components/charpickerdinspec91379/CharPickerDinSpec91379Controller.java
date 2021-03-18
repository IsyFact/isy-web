package de.bund.bva.isyfact.common.web.jsf.components.charpickerdinspec91379;

import java.util.List;
import java.util.stream.Collectors;

public class CharPickerDinSpec91379Controller {

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
