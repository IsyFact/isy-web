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
package de.bund.bva.isyfact.common.web.tempwebresource.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

import de.bund.bva.isyfact.common.web.GuiController;
import de.bund.bva.isyfact.common.web.tempwebresource.TempWebResourceRo;

/**
 * Controller-Klasse zur Darstellung von Bildern.
 *
 * @deprecated This module is deprecated and will be removed in a future release.
 * It is recommended to use isy-angular-widgets instead.
 */
@Deprecated
public class BildController extends ResourceController implements GuiController {
    /**
     * Diese Methode stellt Bilder zur direkten Anzeige in der GUI bereit.
     *
     * @param request
     *            der aktuelle http request.
     * @param response
     *            die aktuelle http response.
     * @throws IOException
     *             Fehler beim Schreiben der Datei.
     * @return Rückgabe ist immer null.
     *
     */
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        TempWebResourceRo webResource = ladeTempWebResource(request);
        if (webResource != null) {
            byte[] content = webResource.getInhalt();
            response.setBufferSize(content.length);
            response.setContentLength(content.length);
            response.getOutputStream().write(content);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        return null;
    }
}
