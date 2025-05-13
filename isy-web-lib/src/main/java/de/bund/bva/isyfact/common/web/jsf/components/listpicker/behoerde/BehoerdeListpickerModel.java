package de.bund.bva.isyfact.common.web.jsf.components.listpicker.behoerde;

import java.util.List;

import com.google.common.collect.Lists;

import de.bund.bva.isyfact.common.web.jsf.components.listpicker.ListpickerModel;

/**
 * Das Listpicker-Model für Behörden.
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class BehoerdeListpickerModel extends ListpickerModel<BehoerdeListpickerItem> {

    /**
     * Die Serial-Version UID.
     */
    private static final long serialVersionUID = 1L;

    @Override
    public List<String> getColumnPropertyKeys() {
        return Lists.newArrayList("kennzeichen", "name");
    }

}
