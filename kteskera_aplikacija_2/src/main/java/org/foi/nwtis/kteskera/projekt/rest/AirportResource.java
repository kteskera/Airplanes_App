package org.foi.nwtis.kteskera.projekt.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.AerodromDAO;
import org.foi.nwtis.kteskera.projekt.podaci.AirplanesDAO;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;
import org.foi.nwtis.kteskera.projekt.podaci.Meteo;
import org.foi.nwtis.kteskera.projekt.podaci.MeteoDAO;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.AvionLeti;

@Path("aerodromi")
public class AirportResource {

    @Inject
    ServletContext context;

    /**
     * GET metoda vraća kolekciju aerodroma
     *
     * @param korisnik
     * @param lozinka
     * @param name
     * @param state
     * @return response
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrome(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka, @QueryParam("name") String name, @QueryParam("state") String state) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        if (name != null && state != null) {
            airports = adao.dohvatiSveAerodromePo(pbp, name, state);
        } else if (name == null && state != null) {
            airports = adao.dohvatiSveAerodromePoNazivuDrzave(pbp, state);
        } else if (name != null && state == null) {
            airports = adao.dohvatiSveAerodromePoNazivu(pbp, name);
        } else {
            airports = adao.dohvatiSveAerodrome(pbp);
        }
        return Response
                .status(Response.Status.OK)
                .entity(airports)
                .build();
    }

    /**
     * GET metoda - vraća podatke izabranog aerodroma
     *
     * @param icao
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @GET
    @Path("/{icao}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodrom(@PathParam("icao") String icao, @HeaderParam("korisnik") String korisnik,
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
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        airports = adao.dohvatiSveAerodrome(pbp);
        Aerodrom a2 = null;
        for (Aerodrom a : airports) {
            if (a.getIcao().equals(icao)) {
                a2 = a;
                break;
            }
        }
        if (a2 == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Ne postoji traženi aerodrom!")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(a2)
                .build();
    }

    /**
     * GET metoda - vraća broj prikupljenih letova aviona s izabranog aerodroma
     *
     * @param icao
     * @param korisnik
     * @param lozinka
     * @return response
     */
    @GET
    @Path("/{icao}/letovi")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodromLetovi(@PathParam("icao") String icao, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        int count = 0;
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        airports = adao.dohvatiSveAerodrome(pbp);
        Aerodrom a2 = null;
        for (Aerodrom a : airports) {
            if (a.getIcao().equals(icao)) {
                a2 = a;
                break;
            }
        }
        if (a2 == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Ne postoji traženi aerodrom!")
                    .build();
        } else {
            AirplanesDAO adao2 = new AirplanesDAO();
            List<AvionLeti> letovi = adao2.dohvatiSveAvione(pbp);

            for (AvionLeti a : letovi) {
                if (a.getEstDepartureAirport().equals(a2.getIcao())) {
                    count++;
                }
            }

        }
        return Response
                .status(Response.Status.OK)
                .entity(count)
                .build();
    }

    @GET
    @Path("/{icao}/letovi/{dan}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodromLetoviDan(@PathParam("icao") String icao, @PathParam("dan") String dan, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        List<AvionLeti> letovi = new ArrayList<>();
        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        airports = adao.dohvatiSveAerodrome(pbp);
        Aerodrom a2 = null;
        for (Aerodrom a : airports) {
            if (a.getIcao().equals(icao)) {
                a2 = a;
                break;
            }
        }
        if (a2 == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Ne postoji traženi aerodrom!")
                    .build();
        } else if (isValid(dan)) {
            AirplanesDAO adao2 = new AirplanesDAO();
            letovi = adao2.dohvatiAvionePoDatumu(dan, icao, pbp);
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Krivi format datuma!")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(letovi)
                .build();
    }

    @GET
    @Path("/{icao}/meteoDan/{dan}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodromVrijeme(@PathParam("icao") String icao, @PathParam("dan") String dan, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        List<Meteo> meteoPodaci = new ArrayList<>();

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        airports = adao.dohvatiSveAerodrome(pbp);
        Aerodrom a2 = null;
        for (Aerodrom a : airports) {
            if (a.getIcao().equals(icao)) {
                a2 = a;
                break;
            }
        }
        if (a2 == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Ne postoji traženi aerodrom!")
                    .build();
        } else if (isValid(dan)) {
            MeteoDAO mdao = new MeteoDAO();
            meteoPodaci = mdao.dohvatiMeteoPodatkeZaDan(dan, icao, pbp);
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Krivi format datuma!")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(meteoPodaci)
                .build();
    }
    
    @GET
    @Path("/{icao}/meteoVrijeme/{vrijeme}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajAerodromVrijemeLong(@PathParam("icao") String icao, @PathParam("vrijeme") String vrijeme, @HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        PostavkeBazaPodataka pbp = (PostavkeBazaPodataka) context.getAttribute("Postavke");
        List<Meteo> meteoPodaci = new ArrayList<>();

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        AerodromDAO adao = new AerodromDAO();
        List<Aerodrom> airports = new ArrayList<>();
        airports = adao.dohvatiSveAerodrome(pbp);
        Aerodrom a2 = null;
        for (Aerodrom a : airports) {
            if (a.getIcao().equals(icao)) {
                a2 = a;
                break;
            }
        }
        if (a2 == null) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Ne postoji traženi aerodrom!")
                    .build();
        } else if (vrijeme!=null) {
            MeteoDAO mdao = new MeteoDAO();            
            String date = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date (Long.parseLong(vrijeme)*1000));
            
            meteoPodaci = mdao.dohvatiMeteoPodatke(date, icao, pbp);
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Krivi format datuma!")
                    .build();
        }
        return Response
                .status(Response.Status.OK)
                .entity(meteoPodaci)
                .build();
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
