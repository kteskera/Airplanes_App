package org.foi.nwtis.kteskera.projekt.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromiKlijent_5;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;

@Path("aerodromi")
@Controller
public class AerodromiKontroler_1 {

    @Inject
    private Models model;
    @Inject
    HttpSession session;

    @Inject
    ServletContext context;

    @Path("aerodromiKorisnik")
    @GET
    @View("aerodromiKorisnik.jsp")
    public String aerodromiKorisnik() throws JsonProcessingException {

        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
            String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "administracijaAerodroma");
            if (odgovor.equals("OK")) {
                MojAerodromiKlijent_5 mak = new MojAerodromiKlijent_5();
                Response res = mak.dajAerodrome(Response.class, k.getKorisnik(), k.getLozinka());
                List<Aerodrom> response = res.readEntity(List.class);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String aerodromi = gson.toJson(response);
                final ObjectMapper objectMapper = new ObjectMapper();
                Aerodrom[] langs = objectMapper.readValue(aerodromi, Aerodrom[].class);
                model.put("aerodromiKojiPratiKorisnik", langs);
            } else {
                return "redirect:/korisnik/izbornik";
            }
        } else {
            return "../../index.jsp";
        }
        return null;
    }
}
