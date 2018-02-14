package de.bund.bva.isyfact.common.web.jsf.components.navigationmenu;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.isyfact.common.web.global.GlobalFlowModel;

/**
 * Der NavigationMenuController wird verwendet um das NavigationMenu zu befüllen und gegebenenfalls auch zu
 * ändern.
 *
 *
 * @author Capgemini, Lars Chojnowska
 * @version $Id:$
 */
public class NavigationMenuController {

    /**
     * Ein Holder, welcher alle Informationen zum erstellen eineres Navigationsmenüs besitzt.
     */
    private NavigationMenuModelHolder navigationMenuModelHolder;

    /**
     * Schreibt das NavigationMenuModel in das vorgesehene FlowModel
     *
     * @param model
     *            Das verwendete GlobalFlowModel
     *
     */
    public void initialisiereModel(GlobalFlowModel model) {

        NavigationMenuModel navModel = this.navigationMenuModelHolder.getNavigationMenuModel();
        model.setNavigationMenuModel(navModel);
    }

    @Required
    public void setNavigationMenuModelHolder(NavigationMenuModelHolder navigationMenuModelHolder) {
        this.navigationMenuModelHolder = navigationMenuModelHolder;
    }
}