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
import org.foi.nwtis.kteskera.projekt.podaci.KorisniciKlijent_2;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.podaci.Korisnik;

@Named(value = "popisKorisnika")
@SessionScoped

public class PopisKorisnika implements Serializable {

    @Inject
    ServletContext context;

    @Getter
    @Setter
    private String poruka = "";

    @Getter
    @Setter
    List<Korisnik> lista = new ArrayList<>();

    @Inject
    HttpSession session;

    public PopisKorisnika() {
    }

    public String dajKorisnike() {
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "pregledKorisnik");
        if (odgovor.equals("OK")) {
            KorisniciKlijent_2 kk2 = new KorisniciKlijent_2();
            Response rez2 = kk2.dajKorisnike(Response.class, k.getKorisnik(), k.getLozinka());
            if (rez2.getStatus() == 200) {
                try {
                    Object response = rez2.readEntity(Object.class);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String kor = gson.toJson(response);
                    final ObjectMapper objectMapper = new ObjectMapper();
                    this.lista = objectMapper.readValue(kor, List.class);
                    poruka ="";
                    return "korisnici.xhtml?faces-redirect=true";
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(PrijavaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            poruka = "Korisnik nema pravo!";
        }
        return null;

    }

}
