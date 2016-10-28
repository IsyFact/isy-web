/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * The Federal Office of Administration (Bundesverwaltungsamt, BVA)
 * licenses this file to you under the Apache License, Version 2.0 (the
 * License). You may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package de.bund.bva.isyfact.common.web.layout;

import org.springframework.beans.factory.annotation.Required;

import de.bund.bva.isyfact.common.web.global.AbstractGuiController;

/**
 * Controller-Klasse fuer Operationen auf dem {@link DetailseiteModel}.
 *
 * @author Capgemini, Jonas Zitz
 * @version $Id: DetailseiteController.java 136137 2015-05-06 13:01:17Z sdm_skern $
 */
public class DetailseiteController extends AbstractGuiController<DetailseiteModel> {

    /**
     * Zugriff auf den Basis-Controller.
     */
    private BasisController basisController;

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

    @Required
    public void setBasisController(BasisController basisController) {
        this.basisController = basisController;
    }

}
