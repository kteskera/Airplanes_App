/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.podaci;

/**
 * Jersey REST client generated for REST resource:aerodromi [aerodromi/]<br>
 * USAGE:
 * <pre>
 *        AerodromiKlijent client = new AerodromiKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author NWTiS_1
 */
public class AerodromiKlijent {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/kteskera_aplikacija_2/rest/";

    public AerodromiKlijent() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /**
     * @param responseType Class representing the response
     * @param name query parameter
     * @param state query parameter
     * @return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T dajAerodrome(Class<T> responseType, String name, String state, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        String[] queryParamNames = new String[]{"name", "state"};
        String[] queryParamValues = new String[]{name, state};
        
        jakarta.ws.rs.core.Form form = getQueryOrFormParams(queryParamNames, queryParamValues);
        jakarta.ws.rs.core.MultivaluedMap<String, String> map = form.asMap();
        for (java.util.Map.Entry<String, java.util.List<String>> entry : map.entrySet()) {
            java.util.List<String> list = entry.getValue();
            String[] values = list.toArray(new String[list.size()]);
            webTarget = webTarget.queryParam(entry.getKey(), (Object[]) values);
        }
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }

    private jakarta.ws.rs.core.Form getQueryOrFormParams(String[] paramNames, String[] paramValues) {
        jakarta.ws.rs.core.Form form = new jakarta.ws.rs.core.Form();
        for (int i = 0; i < paramNames.length; i++) {
            if (paramValues[i] != null) {
                form = form.param(paramNames[i], paramValues[i]);
            }
        }
        return form;
    }

    public void close() {
        client.close();
    }
    
}
