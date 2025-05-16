package de.bund.bva.isyfact.common.web.layout;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;

/**
 * Controller-Klasse fuer Operationen auf dem {@link DetailseiteModel}.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
@Controller
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DetailseiteController extends AbstractGuiController<DetailseiteModel> {

    /**
     * Zugriff auf den Basis-Controller.
     */
    private BasisController basisController;

    @Autowired
    public DetailseiteController(BasisController basisController) {
        this.basisController = basisController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialisiereModel(DetailseiteModel model) {

        // Basis Model anpassen
        BasisModel basisModel = this.basisController.getMaskenModelZuController();

        // Auf der Detailseite wird standardmäßig die Seitentoolbar und der Zurück-Button angzeigt.
        basisModel.getSeitentoolbarModel().setAnzeigen(true);
        basisModel.getSeitentoolbarModel().setZeigeZurueckButton(true);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Class<DetailseiteModel> getMaskenModelKlasseZuController() {
        return DetailseiteModel.class;
    }

    public void setBasisController(BasisController basisController) {
        this.basisController = basisController;
    }

}
