package org.foi.nwtis.kteskera.projekt.zrna;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.kteskera.projekt.podaci.Dnevnik;
import org.foi.nwtis.kteskera.projekt.podaci.DnevnikKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.Konfiguracija;
import org.foi.nwtis.podaci.Korisnik;

@Named(value = "pregledDnevnika")
@SessionScoped

public class PregledDnevnika implements Serializable {

    @Inject
    ServletContext context;

    @Getter
    @Setter
    private String poruka = "";

    @Getter
    @Setter
    private String odKad = null;

    @Getter
    @Setter
    private String doKad = null;

    @Getter
    @Setter
    private String pomak = null;

    @Getter
    @Setter
    private String stranica = null;

    @Getter
    @Setter
    private int stranicenje;

    @Getter
    @Setter
    List<Dnevnik> lista = new ArrayList<>();

    @Inject
    HttpSession session;

    public PregledDnevnika() {

    }

    private void inicijalizirajVarijable() throws NumberFormatException {
        Konfiguracija konf = (Konfiguracija) context.getAttribute("PostavkeApp");
        this.stranicenje = Integer.parseInt(konf.dajPostavku("stranicenje"));
    }

    public String dajDnevnik() {
        inicijalizirajVarijable();
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "pregledDnevnik");
        if (odgovor.equals("OK")) {
            azurirajListu();
            poruka ="";
            return "pregledDnevnika.xhtml?faces-redirect=true";

        } else {
            poruka = "Korisnik nema pravo!";
        }
        return null;

    }

    public void azurirajListu() {
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        DnevnikKlijent dk = new DnevnikKlijent(k.getKorisnik());
        Response res = dk.dohvatiZapise(Response.class, odKad, doKad, stranica, pomak, k.getKorisnik(), k.getLozinka());
        if (res.getStatus() == 200) {
            Object response = res.readEntity(Object.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String dnevnik = gson.toJson(response);
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.lista = null;
                this.lista = objectMapper.readValue(dnevnik, List.class);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(PregledDnevnika.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }
}
