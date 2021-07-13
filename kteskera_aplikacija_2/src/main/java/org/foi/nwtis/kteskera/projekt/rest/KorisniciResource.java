package org.foi.nwtis.kteskera.projekt.rest;

import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikPodaciSocket;

import org.foi.nwtis.podaci.Korisnik;

@Path("korisnici")
public class KorisniciResource {

    @Inject
    ServletContext context;

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnike(@HeaderParam("korisnik") String korisnik,
            @HeaderParam("lozinka") String lozinka) {

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
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
        return Response
                .status(Response.Status.OK)
                .entity(korisnici)
                .build();
    }

    @GET
    @Path("/{korisnik}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dajKorisnika(@PathParam("korisnik") String userName, @HeaderParam("korisnik") String korisnik, @HeaderParam("lozinka") String lozinka) {

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.authen(korisnik, lozinka);

        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }
        String odg2 = kps.list(korisnik, odg.split(" ")[1], userName);
        if (!odg.split(" ")[0].equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg2)
                    .build();
        }

        Korisnik k = null;
        for (String s : odg2.split(" ")) {
            if (!s.equals("OK")) {
                String korime = s.split("\t")[0].substring(1);
                String ime = s.split("\t")[2].substring(0, s.split("\t")[2].length() - 1);
                String prezime = s.split("\t")[1];

                k = new Korisnik();
                k.setKorisnik(korime);
                k.setIme(ime);
                k.setPrezime(prezime);
                break;
            }
        }
        return Response
                .status(Response.Status.OK)
                .entity(k)
                .build();
    }

   
    @POST
    @Consumes("application/json")
    @Produces({MediaType.APPLICATION_JSON})
    public Response dodajKorisnika(Korisnik k1) {

        KorisnikPodaciSocket kps = new KorisnikPodaciSocket(context);
        String odg = kps.add(k1.getKorisnik(), k1.getLozinka(), k1.getPrezime(), k1.getIme());

        if (!odg.equals("OK")) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(odg)
                    .build();
        }

        return Response
                .status(Response.Status.OK)
                .entity("Uspješno dodan korisnik!")
                .build();
    }
}
