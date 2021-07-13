package org.foi.nwtis.kteskera.projekt.zrna;

import org.foi.nwtis.kteskera.projekt.podaci.MeteoVrijemeKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.MojiAerodromiKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.Meteo;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromiKlijent;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.Setter;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.podaci.Aerodrom;

import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.podaci.AvionLeti;

@Named(value = "aerodromi")
@SessionScoped

public class PregledAerodroma implements Serializable {

    @Inject
    ServletContext context;

    @Getter
    @Setter
    private String poruka = "";

    @Getter
    @Setter
    private String datum;

    @Getter
    @Setter
    List<Aerodrom> lista = new ArrayList<>();

    @Getter
    @Setter
    List<AvionLeti> listaAviona = new ArrayList<>();

    @Getter
    @Setter
    List<Meteo> listaMeteo = new ArrayList<>();

    @Getter
    @Setter
    String icao;

    @Inject
    HttpSession session;

    public PregledAerodroma() {
    }

    public String dajAerodrome() {
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "pregledAerodroma");
        if (odgovor.equals("OK")) {
            MojiAerodromiKlijent mak = new MojiAerodromiKlijent(k.getKorisnik());
            Response res = mak.dajKorisnikePrati(Response.class, k.getKorisnik(), k.getLozinka());
            if (res.getStatus() == 200) {

                Object response = res.readEntity(Object.class);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String kor = gson.toJson(response);
                final ObjectMapper objectMapper = new ObjectMapper();
                try {
                    this.lista = objectMapper.readValue(kor, List.class);
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
                }
                poruka ="";
                return "aerodromi.xhtml?faces-redirect=true";

            }
        } else {
            poruka = "Korisnik nema pravo!";
        }
        return null;

    }

    public void prikaziLetove() {

        Korisnik k = (Korisnik) session.getAttribute("korisnik");
        AerodromiKlijent ak = new AerodromiKlijent(icao, datum);
        Response res = ak.dajAerodromLetoviDan(Response.class, k.getKorisnik(), k.getLozinka());

        if (res.getStatus() == 200) {
            Object response = res.readEntity(Object.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String avioni = gson.toJson(response);
            final ObjectMapper objectMapper = new ObjectMapper();
            try {
                this.listaAviona = objectMapper.readValue(avioni, List.class);
            } catch (JsonProcessingException ex) {
                Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void prikaziMeteo() {
        try {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
           
            long epoch = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(datum).getTime() / 1000;
            MeteoVrijemeKlijent akm = new MeteoVrijemeKlijent(icao, String.valueOf(epoch));
            Response res2 = akm.dajAerodromVrijemeLong(Response.class, k.getKorisnik(), k.getLozinka());
            
            if (res2.getStatus() == 200) {
                Object response = res2.readEntity(Object.class);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String meteoPodaci = gson.toJson(response);
                final ObjectMapper objectMapper = new ObjectMapper();
                try {
                    this.listaMeteo = objectMapper.readValue(meteoPodaci, List.class);
                } catch (JsonProcessingException ex) {
                    Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (ParseException ex) {
            Logger.getLogger(PregledAerodroma.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
