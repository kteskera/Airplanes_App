package org.foi.nwtis.kteskera.projekt.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromDAO;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt.podaci.MyAirportsDAO;
import org.foi.nwtis.podaci.Aerodrom;

import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.podaci.MyAirport;

@Path("mojiAerodromi")
public class MyAirportResource {

    @Inject
    ServletContext context;

    /**
     * GET metoda - vraća kolekciju aerodroma za koje se prikupljaju podaci o letovima aviona
     *
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }

        MyAirportsDAO madao = new MyAirportsDAO();
        List<MyAirport> myAirport = madao.dohvatiSveMyAirport(pbp);
        List<String> idents = new ArrayList<>();
        for (MyAirport s : myAirport) {
            idents.add(s.getIdent());
        }
        Set<String> set = new HashSet<>(idents);
        idents.clear();
        idents.addAll(set);

        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> lista1 = new ArrayList<>();
        List<Aerodrom> lista = adao.dohvatiSveAerodrome(pbp);
        for (String s : idents) {
            for (Aerodrom a : lista) {
                if (s.equals(a.getIcao())) {
                    lista1.add(a);
                }
            }

        }

        return Response
                .status(Response.Status.OK)
                .entity(lista1)
                .build();
    }

    /**
     * GET metoda - vraća kolekciju korisnika koji su pretplaćeni za podatke izabranog aerodroma.
     *
     * @param icao
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @GET
    @Path("/{icao}/prate")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnikePrate(@PathParam("icao") String icao, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }

        MyAirportsDAO madao = new MyAirportsDAO();
        List<MyAirport> myAirport = madao.dohvatiSveMyAirport(pbp);
        List<String> idents = new ArrayList<>();
        for (MyAirport s : myAirport) {
            if (s.getIdent().equals(icao)) {
                idents.add(s.getUsername());
            }

        }
        Set<String> set = new HashSet<>(idents);
        idents.clear();
        idents.addAll(set);

        
        List<Korisnik> lista1 = new ArrayList<>();
        List<Korisnik> korisnici = new ArrayList<>();
        String odg2 = kps.listAll(korisnik, odg.split(" ")[1]);
        for (String s : odg2.split(" ")) {
            if (!s.equals("OK")) {
                String korime = s.split("\t")[0].substring(1);
                String ime = s.split("\t")[2].substring(0, s.split("\t")[2].length() - 1);
                String prezime = s.split("\t")[1];

                Korisnik k = new Korisnik();
                k.setKorisnik(korime);
                k.setIme(ime);
                k.setPrezime(prezime);
                korisnici.add(k);
            }
        }
        
        for (String s : idents) {
            for (Korisnik k2 : korisnici) {
                if (s.equals(k2.getKorisnik())) {
                    lista1.add(k2);
                }
            }

        }

        return Response
                .status(Response.Status.OK)
                .entity(lista1)
                .build();
    }

    /**
     * GET metoda - vraća kolekciju aerodroma koje prati izabrani korisnik.
     *
     * @param username
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @GET
    @Path("/{korisnik}/prati")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnikePrati(@PathParam("korisnik") String username, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
       KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }

        MyAirportsDAO madao = new MyAirportsDAO();
        List<MyAirport> myAirport = madao.dohvatiSveMyAirport(pbp);
        List<String> idents = new ArrayList<>();
        for (MyAirport s : myAirport) {
            if (s.getUsername().equals(username)) {
                idents.add(s.getIdent());
            }

        }
        Set<String> set = new HashSet<>(idents);
        idents.clear();
        idents.addAll(set);

        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> lista1 = new ArrayList<>();
        List<Aerodrom> lista = adao.dohvatiSveAerodrome(pbp);
        for (String s : idents) {
            for (Aerodrom a : lista) {
                if (s.equals(a.getIcao())) {
                    lista1.add(a);
                }
            }

        }
        return Response
                .status(Response.Status.OK)
                .entity(lista1)
                .build();
    }

    /**
     * POST metoda - odabranom korisniku dodaje aerodrom kojeg će pratiti.
     *
     * @param username
     * @param korisnik
     * @param lozinka
     * @param a1
     * @return response
     */
    @POST
    @Consumes("application/json")
    @Path("/{korisnik}/prati")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(@PathParam("korisnik") String username, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, Aerodrom a1) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
      KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        MyAirportsDAO mydao = new MyAirportsDAO();

        MyAirport ma = new MyAirport(a1.getIcao(), username, true);

        if (!mydao.dodajMyAirportZapis(ma, pbp) || a1 == null || a1.getIcao() == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Poogreška u autentifikaciji ili u podacima za aerodrom.")
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Korisnik uspješno pretplaćen!")
                .build();
    }

    /**
     * DELETE metoda - odabranom korisniku briše aerodrom tako da ga više ne prati.
     *
     * @param username
     * @param icao
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @DELETE
    @Consumes("application/json")
    @Path("/{korisnik}/prati/{icao}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response obrisiPreplatu(@PathParam("korisnik") String username, @PathParam("icao") String icao, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
       KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        MyAirportsDAO mydao = new MyAirportsDAO();

        boolean proba = mydao.obrisiZapis(icao, username, pbp);
        
        if(proba){
        
        return Response
                .status(Response.Status.OK)
                .entity("Korisnik više ne prati zadani aerodrom!")
                .build();
        }
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity("Korisnik ne prati zadani aerodrom!")
                .build();
    }
}
