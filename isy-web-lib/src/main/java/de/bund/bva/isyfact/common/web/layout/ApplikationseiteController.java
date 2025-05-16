package de.bund.bva.isyfact.common.web.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;

/**
 * Controller-Klasse fuer Operationen auf dem {@link ApplikationseiteModel}.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
public class ApplikationseiteController extends AbstractGuiController<ApplikationseiteModel> {

    /**
     * Der Controller für die Linksnavigation.
     */
    protected LinksnavigationController linksnavigationController;

    /**
     * Der Controller für die Quicklinks.
     */
    protected QuicklinksController quicklinksController;

    @Autowired
    public ApplikationseiteController(LinksnavigationController linksnavigationController, QuicklinksController quicklinksController) {
        this.linksnavigationController = linksnavigationController;
        this.quicklinksController = quicklinksController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialisiereModel(ApplikationseiteModel model) {
        this.linksnavigationController.initialisiereModel(model);
        this.quicklinksController.initialisiereModel(model);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<ApplikationseiteModel> getMaskenModelKlasseZuController() {
        return ApplikationseiteModel.class;
    }

    public void setLinksnavigationController(LinksnavigationController linksnavigationController) {
        this.linksnavigationController = linksnavigationController;
    }

    public void setQuicklinksController(QuicklinksController quicklinksController) {
        this.quicklinksController = quicklinksController;
    }

}
