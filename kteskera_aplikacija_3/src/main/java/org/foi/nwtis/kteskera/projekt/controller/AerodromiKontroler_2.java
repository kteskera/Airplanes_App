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
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromiKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromiKlijent_2;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromKlijent_2;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromKlijent_3;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromiKlijent_4;
import org.foi.nwtis.kteskera.projekt.podaci.MojAerodromiKlijent_5;
import org.foi.nwtis.kteskera.projekt_lib.Poruka;
import org.foi.nwtis.kteskera.projekt.sb.PosiljateljPoruka;

import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Korisnik;

@Path("aerodromi")
@Controller
public class AerodromiKontroler_2 {

    @Inject
    private Models model;
    @Inject
    HttpSession session;

    @Context
    ServletContext context;

    @Inject
    PosiljateljPoruka posiljateljPoruka;

    @FormParam("icao")
    String icaoPreplata;

    @Path("aerodromiKorisnik/{icao}")
    @POST
    @View("aerodromiKorisnikPrikaz.jsp")
    public String aerodromiKorisnik(@PathParam("icao") String icao, @QueryParam("icao") String icaoPreplata) throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");

            MojAerodromiKlijent_4 mak = new MojAerodromiKlijent_4(icao);
            Response res2 = mak.dajKorisnikePrate(Response.class, k.getKorisnik(), k.getLozinka());
            List<Object> response = res2.readEntity(List.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String korisnici = gson.toJson(response);
            final ObjectMapper objectMapper = new ObjectMapper();
            Korisnik[] korisniciArr = objectMapper.readValue(korisnici, Korisnik[].class);
            model.put("korisnici", korisniciArr);

            AerodromiKlijent_2 ak = new AerodromiKlijent_2(icao);
            Response odg = ak.dajAerodrom(Response.class, k.getKorisnik(), k.getLozinka());
            Object o = odg.readEntity(Aerodrom.class);
            String aerodromJson = gson.toJson(o);
            Aerodrom aerodrom_ = objectMapper.readValue(aerodromJson, Aerodrom.class);
            model.put("icao", aerodrom_.getIcao());

        } else {
            return "../../index.jsp";

        }
        return null;
    }

    @Path("aerodromiPrekiniPreplatu/{icao}")
    @POST
    @View("aerodromiKorisnik.jsp")
    public String aerodromiKorisnikObrisi(@PathParam("icao") String icao) throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");
            MojAerodromKlijent_2 mak = new MojAerodromKlijent_2(k.getKorisnik(), icao);
            Response res2 = mak.obrisiPreplatu(Response.class, k.getKorisnik(), k.getLozinka());
            if (res2.getStatus() == 200) {
                posiljateljPoruka.saljiPoruku("Korisnik:" + k.getKorisnik() + " - Aerodrom: " + icao + " - Brisanje preplate");
                Poruka poruka = new Poruka();
                poruka.setKorisnik(k);
                poruka.setNaslov("Brisanje preplate");
                poruka.setSadrzaj("Aerodrom:" + icao);
                posiljateljPoruka.saljiPoruk(poruka);

                model.put("greska", "Uspješno obrisan aerodrom!");
            } else {
                model.put("greska", "Korisnik ne prati odabrani aerodrom!");
            }

            MojAerodromiKlijent_5 mak2 = new MojAerodromiKlijent_5();
            Response res = mak2.dajAerodrome(Response.class, k.getKorisnik(), k.getLozinka());
            List<Aerodrom> response = res.readEntity(List.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String aerodromi = gson.toJson(response);
            final ObjectMapper objectMapper = new ObjectMapper();
            Aerodrom[] aerodromiPolje = objectMapper.readValue(aerodromi, Aerodrom[].class);
            model.put("aerodromiKojiPratiKorisnik", aerodromiPolje);
            return "aerodromiKorisnik.jsp";
        } else {
            return "../../index.jsp";

        }
    }

    @Path("aerodromiDodajPreplatu")
    @POST
    @View("aerodromiKorisnik.jsp")
    public String aerodromiDodajPreplatu() throws JsonProcessingException {
        if (session.getAttribute("korisnik") != null) {
            Korisnik k = (Korisnik) session.getAttribute("korisnik");

            AerodromiKlijent aerodromiKlijent = new AerodromiKlijent();
            Response odgovor = aerodromiKlijent.dajAerodrome(Response.class, null, null, k.getKorisnik(), k.getLozinka());
            List<Object> odgovorLista = odgovor.readEntity(List.class);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            final ObjectMapper objectMapper = new ObjectMapper();
            String aerodromi = gson.toJson(odgovorLista);
            Aerodrom[] sviAerodromiArr = objectMapper.readValue(aerodromi, Aerodrom[].class);
            List<Aerodrom> sviAerodromiLista = new ArrayList<>(Arrays.asList(sviAerodromiArr));
            Aerodrom a = null;
            for (Aerodrom b : sviAerodromiLista) {
                if (b.getIcao().equals(icaoPreplata)) {
                    a = b;
                    break;
                }
            }
            if (a != null) {
                MojAerodromKlijent_3 mak3 = new MojAerodromKlijent_3(k.getKorisnik());
                Response odg = mak3.dodajKorisnika(a, Response.class, k.getKorisnik(), k.getLozinka());

                if (odg.getStatus() == 200) {

                    model.put("greska", "Uspješno dodan aerodrom: " + a.getNaziv());
                    Poruka poruka = new Poruka();
                    poruka.setKorisnik(k);
                    poruka.setNaslov("Dodavanje preplate");
                    poruka.setSadrzaj("Aerodrom:" + a.getIcao());
                    posiljateljPoruka.saljiPoruk(poruka);

                } else {

                    model.put("greska", "Korisnik je već pretplaćen!");
                }
            } else {
                model.put("greska", "Nepostoji aerodrom za zadani ICAO");
            }
            MojAerodromiKlijent_5 mak2 = new MojAerodromiKlijent_5();
            Response res = mak2.dajAerodrome(Response.class, k.getKorisnik(), k.getLozinka());
            List<Aerodrom> response = res.readEntity(List.class);
            String aerodromi_ = gson.toJson(response);
            Aerodrom[] aerodromiPolje = objectMapper.readValue(aerodromi_, Aerodrom[].class);
            model.put("aerodromiKojiPratiKorisnik", aerodromiPolje);

            return "aerodromiKorisnik.jsp";
        } else {
            return "../../index.jsp";

        }
    }

}
