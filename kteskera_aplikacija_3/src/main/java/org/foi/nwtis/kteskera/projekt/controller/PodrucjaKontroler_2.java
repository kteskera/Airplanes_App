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
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.kteskera.projekt.podaci.KorisniciKlijent_1;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.podaci.Korisnik;

@Path("podrucja")
@Controller
public class PodrucjaKontroler_2 {

    @Inject
    private Models model;
    @Inject
    HttpSession session;

    @Inject
    ServletContext context;

    @FormParam("podrucje")
    String podrucje;

    @Path("deaktivirajPodrucja")
    @POST
    @View("aktivirajPodrucja.jsp")
    public String podrucjaKorisnikaDeaktivacija(@QueryParam("korisnik") String korisnik) throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
            String odgovor = "";
            odgovor = kps.revoke(k.getKorisnik(), k.getIdSjednice(), podrucje, korisnik);
            if (odgovor.equals("OK")) {
                model.put("greska", "Uspjesna deaktivacija!");
                dodajView(k.getKorisnik(), k.getLozinka());

                return "aktivirajPodrucja.jsp";
            } else if (!odgovor.equals("")) {
                model.put("greska", odgovor);
                dodajView(k.getKorisnik(), k.getLozinka());
                return "aktivirajPodrucja.jsp";
            }
        } else {
            return "../../index.jsp";

        }
        return null;
    }

    @Path("aktivirajPodrucja")
    @POST
    @View("aktivirajPodrucja.jsp")
    public String podrucjaKorisnikaAktivacija(@QueryParam("korisnik") String korisnik) throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
            String odgovor = "";
            odgovor = kps.grant(k.getKorisnik(), k.getIdSjednice(), podrucje, korisnik);
            if (odgovor.equals("OK")) {
                model.put("greska", "Uspjesna aktivacija!");
                dodajView(k.getKorisnik(), k.getLozinka());
                return "aktivirajPodrucja.jsp";
            } else if (!odgovor.equals("")) {
                model.put("greska", odgovor);
                dodajView(k.getKorisnik(), k.getLozinka());
                return "aktivirajPodrucja.jsp";
            }
        } else {
            return "../../index.jsp";

        }
        return null;

    }

    public void dodajView(String korisnik, String lozinka) throws JsonProcessingException {
        KorisniciKlijent_1 kk1 = new KorisniciKlijent_1();
        Response res = kk1.dajKorisnike(Response.class, korisnik, lozinka);
        List<Object> response = res.readEntity(List.class);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String korisnici = gson.toJson(response);
        final ObjectMapper objectMapper = new ObjectMapper();
        Korisnik[] korisniciArr = objectMapper.readValue(korisnici, Korisnik[].class);
        List<String> podrucja = new ArrayList<>();
        podrucja.add("administracija");
        podrucja.add("administracijaAerodroma");
        podrucja.add("pregledKorisnik");
        podrucja.add("pregledJMS");
        podrucja.add("pregledDnevnik");
        podrucja.add("pregledAktivnihKorisnika");
        podrucja.add("pregledAerodroma");
        model.put("podrucja", podrucja);
        model.put("korisnici", korisniciArr);

    }

}
