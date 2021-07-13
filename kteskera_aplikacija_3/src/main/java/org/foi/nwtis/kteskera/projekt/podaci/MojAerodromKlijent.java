/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.podaci;

/**
 * Jersey REST client generated for REST resource:mojiAerodromi [mojiAerodromi/{korisnik}/prati]<br>
 * USAGE:
 * <pre>
 *        MojAerodromKlijent client = new MojAerodromKlijent();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author NWTiS_1
 */
public class MojAerodromKlijent {

    private jakarta.ws.rs.client.WebTarget webTarget;
    private jakarta.ws.rs.client.Client client;
    private static final String BASE_URI = "http://localhost:8084/kteskera_aplikacija_2/rest/";

    public MojAerodromKlijent(String korisnik) {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prati", new Object[]{korisnik});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    public void setResourcePath(String korisnik) {
        String resourcePath = java.text.MessageFormat.format("mojiAerodromi/{0}/prati", new Object[]{korisnik});
        webTarget = client.target(BASE_URI).path(resourcePath);
    }

    /**
     * @param responseType Class representing the response
     * @return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T dajKorisnikePrati(Class<T> responseType, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).get(responseType);
    }

    /**
     * @param responseType Class representing the response
     * @param requestEntity request data@return response object (instance of responseType class)@param korisnik header parameter[REQUIRED]
     * @param lozinka header parameter[REQUIRED]
     */
    public <T> T dodajKorisnika(Object requestEntity, Class<T> responseType, String korisnik, String lozinka) throws jakarta.ws.rs.ClientErrorException {
        return webTarget.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).header("korisnik", korisnik).header("lozinka", lozinka).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    public void close() {
        client.close();
    }
    
}
