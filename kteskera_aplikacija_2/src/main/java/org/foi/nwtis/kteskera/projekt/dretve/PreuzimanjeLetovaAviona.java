package org.foi.nwtis.kteskera.projekt.dretve;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.AirplanesDAO;
import org.foi.nwtis.kteskera.projekt.podaci.MyAirportLog;
import org.foi.nwtis.kteskera.projekt.podaci.MyAirportsDAO;
import org.foi.nwtis.kteskera.projekt.podaci.MyAirportsLogDAO;
import org.foi.nwtis.podaci.MyAirport;
import org.foi.nwtis.rest.klijenti.OSKlijent;
import org.foi.nwtis.rest.podaci.AvionLeti;

public class PreuzimanjeLetovaAviona extends Thread {

    private PostavkeBazaPodataka pbp;
    private OSKlijent osk;
    private String icao;
    private boolean exit = false;
    private String mojKorisnik;
    private String mojaLozinka;
    private boolean preuzimanjeStatus;
    private String preuzimanjePocetak;
    private String preuzimanjeKraj;
    private int preuzimanjeCiklus;
    private int preuzimanjePauza;
    private List<AvionLeti> avionLeti;

    public PreuzimanjeLetovaAviona(PostavkeBazaPodataka pbp) {
        this.pbp = pbp;
    }

    @Override
    public void interrupt() {
        exit = true;
        super.interrupt();
    }

    @Override
    public void run() {
        System.out.println("Počinje preuzimanje podataka letova!");
        while (!exit) {
            System.out.println("Preuzimamo podatke letova!");
            osk = new OSKlijent(mojKorisnik, mojaLozinka);
            long preuzimanjePocetak1 = 0;
            long preuzimanjeKraj1 = 0;
            try {
                preuzimanjePocetak1 = new java.text.SimpleDateFormat("dd.MM.yyyy").parse(preuzimanjePocetak).getTime() / 1000;
                preuzimanjeKraj1 = new java.text.SimpleDateFormat("dd.MM.yyyy").parse(preuzimanjeKraj).getTime() / 1000;
                MyAirportsLogDAO maldao = new MyAirportsLogDAO();

                while (preuzimanjePocetak1 < preuzimanjeKraj1) {
                    Date d = new Date(preuzimanjePocetak1 * 1000);
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    MyAirportsDAO madao = new MyAirportsDAO();
                    List<MyAirport> myAirport = madao.dohvatiSveMyAirport(pbp);

                    for (MyAirport my : myAirport) {
                        MyAirportLog log = maldao.dohvatiMyAirportLog(my.getIdent(), dateFormat.format(d), pbp);
                        if (log == null) {
                            avionLeti = osk.getDepartures(my.getIdent(), preuzimanjePocetak1, preuzimanjePocetak1 + 86400);
                            AirplanesDAO adao = new AirplanesDAO();
                            for (AvionLeti a : avionLeti) {
                                if (a.getEstArrivalAirport() != null) {
                                    adao.dodajAvion(a, dateFormat.format(d), pbp);
                                }
                            }
                            java.sql.Date date = new java.sql.Date(preuzimanjePocetak1 * 1000);
                            MyAirportLog mal = new MyAirportLog(my.getIdent(), date, new Timestamp(System.currentTimeMillis()));
                            maldao.dodajLog(mal, pbp);
                        }
                        Thread.sleep(preuzimanjePauza * 1000);
                    }
                    preuzimanjePocetak1 += 86400;
                    Thread.sleep(preuzimanjeCiklus * 1000);
                }
                Thread.sleep(86400000);
            } catch (ParseException | InterruptedException ex) {
                Logger.getLogger(PreuzimanjeLetovaAviona.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Preuzimanje podataka zaustavljeno!");
        }
    }

    @Override
    public synchronized void start() {
        this.preuzimanjeStatus = Boolean.parseBoolean(pbp.dajPostavku("preuzimanje.status"));
        if (!preuzimanjeStatus) {
            System.out.println("Ne preuzimamo ništa!");
            return;
        }
        this.mojKorisnik = pbp.dajPostavku("OpenSkyNetwork.korisnik");
        this.mojaLozinka = pbp.dajPostavku("OpenSkyNetwork.lozinka");
        this.preuzimanjePocetak = pbp.dajPostavku("preuzimanje.pocetak");
        this.preuzimanjeKraj = pbp.dajPostavku("preuzimanje.kraj");
        this.preuzimanjeCiklus = Integer.parseInt(pbp.dajPostavku("preuzimanje.ciklus"));
        this.preuzimanjePauza = Integer.parseInt(pbp.dajPostavku("preuzimanje.pauza"));
        super.start();
    }

    public void stopp() {
        exit = true;
    }
}
