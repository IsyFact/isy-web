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
package de.bund.bva.isyfact.common.web.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import com.google.common.base.Strings;
import com.google.common.base.Splitter;

/**
 * Diese Klasse enthält Utility-Methoden für isy-web.
 */
public final class IsyWebRequestUtil {

    /**
     * Trennzeichen zwischen Benutzerkennung und Kennwort bei der HTTP-BASIC-Authentifizierung.
     */
    private static final String BASIC_AUTH_DELIMITER = ":";


    /**
     * Codiert eine URL.
     *
     * @param url ist die zu codierende Url
     * @return die codierte Url
     * @throws UnsupportedEncodingException sofern das Encoding nicht unterstuetzt wird.
     */
    private static String encodeUrl(String url) throws UnsupportedEncodingException {
        String result = URLEncoder.encode(url, "UTF-8");
        return result;
    }

    /**
     * Decodiert eine URL.
     *
     * @param url ist die zu decodierende Url
     * @return die decodierte Url
     * @throws UnsupportedEncodingException sofern das Encoding nicht unterstuetzt wird.
     */
    private static String decodeUrl(String url) throws UnsupportedEncodingException {
        String result = URLDecoder.decode(url, "UTF-8");
        return result;
    }

    /**
     * Ermittelt den gesamten aktuellen Anfrage-Url-Pfad einschliesslich HTTP-GET-Parametern.
     *
     * @param request ist der {@link HttpServletRequest}
     * @return ist der gesamte aktuelle Anfrage-Url-Pfad.
     */
    private static String getGesamteRequestUrl(HttpServletRequest request) {
        String gesamteUri = request.getRequestURI().toString();
        if (!Strings.isNullOrEmpty(request.getQueryString())) {
            gesamteUri += "?" + request.getQueryString();
        }
        return gesamteUri;
    }



}

