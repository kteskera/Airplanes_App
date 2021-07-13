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
import org.foi.nwtis.kteskera.projekt.podaci.KorisniciKlijent_1;
import org.foi.nwtis.podaci.Korisnik;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;


@Path("podrucja")
@Controller
public class PodrucjaKontroler {

    @Inject
    private Models model;
    @Inject
    HttpSession session;

    @Inject
    ServletContext context;

    @Path("aktivirajPodrucja")
    @GET
    @View("aktivirajPodrucja.jsp")
    public String podrucjaKorisnika() throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
            String odgovor = kps.author(k.getKorisnik(), k.getIdSjednice(), "administracija");
            if (odgovor.equals("OK")) {
                KorisniciKlijent_1 kk1 = new KorisniciKlijent_1();
                Response res = kk1.dajKorisnike(Response.class, k.getKorisnik(), k.getLozinka());
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

            } else {
                return "redirect:/korisnik/izbornik";

            }
        } else {
            return "../../index.jsp";

        }
        return null;
    }

}
