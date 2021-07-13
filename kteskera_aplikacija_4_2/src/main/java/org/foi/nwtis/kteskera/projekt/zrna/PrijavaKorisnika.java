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
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.podaci.Korisnik;

@Named(value = "prijavaKorisnika")
@SessionScoped

public class PrijavaKorisnika implements Serializable {

    @Getter
    @Setter
    private String lozinka;

    @Getter
    @Setter
    private String korisnik;

    @Inject
    ServletContext context;

    @Getter
    @Setter
    private String poruka = "";

    @Inject
    HttpSession session;

    public PrijavaKorisnika() {
    }

    public String prijaviKorisnika() {
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odgovor = kps.authen(korisnik, lozinka);

        if (odgovor.split(" ")[0].equals("OK")) {
            KorisnikKlijent kk2 = new KorisnikKlijent(korisnik);
            Response rez2 = kk2.dajKorisnika(Response.class, korisnik, lozinka);
            if (rez2.getStatus() == 200) {
                try {
                    Object response = rez2.readEntity(Object.class);
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String kor = gson.toJson(response);
                    final ObjectMapper objectMapper = new ObjectMapper();
                    Korisnik k = null;
                    k = objectMapper.readValue(kor, Korisnik.class);
                    k.setLozinka(lozinka);
                    k.setIdSjednice(odgovor.split(" ")[1]);
                    session.setAttribute("korisnik", k);
                    poruka ="";
                    return "izbornik.xhtml?faces-redirect=true";
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(PrijavaKorisnika.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        } else {
            poruka = "Korisničko ime ili lozinka nisu ispravni!";
        }
        return null;

    }

    public String odjaviKorisnika() {
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        String odgovor = kps.logout(k.getKorisnik(), k.getIdSjednice());
        if (odgovor.equals("OK")) {
            poruka = "Uspješna odjava!";
            return "prijava.xhtml?faces-redirect=true";
        }
        return null;

    }

}
