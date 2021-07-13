package org.foi.nwtis.kteskera.projekt.dretve;

import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.kteskera.projekt.podaci.Dnevnik;

import org.foi.nwtis.kteskera.projekt.podaci.DnevnikKlijent;
import org.foi.nwtis.kteskera.projekt.podaci.Korisnik;
import org.foi.nwtis.kteskera.projekt.podaci.KorisnikDAO;
import org.foi.nwtis.kteskera.projekt.podaci.Ovlasti;
import org.foi.nwtis.kteskera.projekt.podaci.OvlastiDAO;
import org.foi.nwtis.kteskera.projekt.podaci.Sjednica;
import org.foi.nwtis.kteskera.projekt.slusaci.SlusacAplikacije;
import org.foi.nwtis.kteskera.vjezba_03.konfiguracije.Konfiguracija;

public class ServerPristupa extends Thread {

    private int port;
    protected static int maxBrojDretvi = 0;
    protected int maksDretvi;
    private Socket uticnica;
    ServerSocket ss;
    int maksBrojZahtjeva;
    int trajanjeSjednice;

    PostavkeBazaPodataka pbp;
    Konfiguracija konf;
    
    private boolean kraj=false;

    public static synchronized void updateCounter() {
        maxBrojDretvi--;
    }

    public void inicijalizirajVarijable() {
        this.maksDretvi = Integer.parseInt(konf.dajPostavku("maks.dretvi"));
        this.maksBrojZahtjeva = Integer.parseInt(konf.dajPostavku("maks.zahtjeva"));
        this.trajanjeSjednice = Integer.parseInt(konf.dajPostavku("sjednica.trajanje"));
        this.port = Integer.parseInt(konf.dajPostavku("port"));
    }

    public ServerPristupa(PostavkeBazaPodataka pbp, Konfiguracija konf) {
        this.pbp = pbp;
        this.konf = konf;
    }

    public void pošaljiGrešku() {
        try (OutputStream os = uticnica.getOutputStream()) {
            String odgovorKorisniku = "ERROR 01 Nema slobodne dretve";
            os.write(odgovorKorisniku.getBytes());
            os.flush();
            uticnica.shutdownOutput();
            uticnica.close();
        } catch (IOException ex) {
            System.out.println("Greška u slanju odgovora");
        }
    }

    @Override
    public void run() {
        try {
            this.ss = new ServerSocket(this.port);
            while (!kraj) {
                uticnica = ss.accept();
                if (maxBrojDretvi <= maksDretvi) {
                    Autentifikacija au = new Autentifikacija(uticnica);
                    au.start();
                    maxBrojDretvi++;
                } else {
                    pošaljiGrešku();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServerPristupa.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void interrupt() {
        kraj=true;
        try {
            this.ss.close();           
        } catch (IOException ex) {
            Logger.getLogger(ServerPristupa.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @Override
    public void start() {
        inicijalizirajVarijable();
        super.start();

    }

    public class Autentifikacija extends Thread {

        private Socket uticnica;

        public Autentifikacija(Socket ss) {
            this.uticnica = ss;
        }

        public boolean dodajZapisDnevnik(String sadrzaj, String status, String korisnik) {
            DnevnikKlijent dk = new DnevnikKlijent();
            Timestamp sad = new Timestamp(System.currentTimeMillis());
            Dnevnik d = new Dnevnik(sad.toString(), sadrzaj, status, korisnik);
            Response rez2 = dk.dodajZapis(d, Response.class);
            if (rez2.getStatus() == 200) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public void run() {
            obradiZahtjev();
            ServerPristupa.updateCounter();
        }

        public void obradiZahtjev() {
            try (InputStream is = uticnica.getInputStream();
                    OutputStream os = uticnica.getOutputStream();) {

                StringBuilder tekst = new StringBuilder();

                while (true) {
                    int i = is.read();
                    if (i == -1) {
                        break;
                    }
                    tekst.append((char) i);
                }
                uticnica.shutdownInput();
                String[] argumenti = tekst.toString().split(" ");
                String odgovorKorisniku = odradiOdgovor(argumenti, tekst.toString());
                os.write(odgovorKorisniku.getBytes());
                os.flush();
                uticnica.shutdownOutput();
                uticnica.close();
            } catch (IOException | NumberFormatException | InterruptedException ex) {
                System.out.println("Greška u obradi zahtjeva");
            }
        }

        public String odradiOdgovor(String[] argumenti, String zahtjev)
                throws NumberFormatException, InterruptedException {
            String sintaksaAdd = "^ADD\\s([\\w-]{3,10})\\s([\\w#-]{3,10})\\s(\"[\\w-]{3,10}\")\\s(\"[\\w-]{3,10}\")$";
            String sintaksaAuthen = "^AUTHEN\\s([\\w-]{3,10})\\s([\\w#-]{3,10})$";
            String sintaksaLogout = "^LOGOUT\\s([\\w-]{3,10})\\s([\\d]{1,3})$";
            String sintaksaGrant = "^GRANT\\s([\\w-]{3,10})\\s([\\d]{1,3})\\s([\\w-]{3,50})\\s([\\w-]{3,10})$";
            String sintaksaRevoke = "^REVOKE\\s([\\w-]{3,10})\\s([\\d]{1,3})\\s([\\w-]{3,50})\\s([\\w-]{3,10})$";
            String sintaksaRights = "^RIGHTS\\s([\\w-]{3,10})\\s([\\d]{1,3})\\s([\\w-]{3,10})$";
            String sintaksaAuthor = "^AUTHOR\\s([\\w-]{3,10})\\s([\\d]{1,3})\\s([\\w-]{3,50})$";
            String sintaksaList = "^LIST\\s([\\w-]{3,10})\\s([\\d]{1,3})\\s([\\w-]{3,10})$";
            String sintaksaListAll = "^LISTALL\\s([\\w-]{3,10})\\s([\\d]{1,3})$";
            String odgovorKorisniku = "";
            if (testirajRegex(sintaksaAdd, argumenti)) {
                odgovorKorisniku = add(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaAuthen, argumenti)) {
                odgovorKorisniku = authen(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaLogout, argumenti)) {
                odgovorKorisniku = logout(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaGrant, argumenti)) {
                odgovorKorisniku = grant(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaRevoke, argumenti)) {
                odgovorKorisniku = revoke(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaRights, argumenti)) {
                odgovorKorisniku = rights(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaAuthor, argumenti)) {
                odgovorKorisniku = author(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaList, argumenti)) {
                odgovorKorisniku = list(argumenti, odgovorKorisniku, zahtjev);

            } else if (testirajRegex(sintaksaListAll, argumenti)) {
                odgovorKorisniku = listall(argumenti, odgovorKorisniku, zahtjev);

            } else {
                odgovorKorisniku = "ERROR 10 Format komande nije ispravan!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, "N/A");
            }
            return odgovorKorisniku;
        }
        // <editor-fold defaultstate="collapsed" desc=" Metode za zahtjeve ">

        private String listall(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        List<Korisnik> listaKorisnika = null;
                        listaKorisnika = kdao.dohvatiSveKorisnike(pbp);
                        String odg = "";
                        for (Korisnik kor : listaKorisnika) {
                            odg += "\"" + kor.getKorisnik() + "\t" + kor.getPrezime() + "\t" + kor.getIme() + "\"" + " ";

                        }

                        odgovorKorisniku = "OK " + odg;
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                    }

                } else {

                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String list(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        Korisnik k2 = null;
                        k2 = kdao.dohvatiKorisnika(argumenti[3], "", Boolean.FALSE, pbp);
                        if (k2 == null) {
                            odgovorKorisniku = "ERROR 17 Korisnik pod nazivom" + argumenti[3] + "ne postoji!";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                        } else {
                            odgovorKorisniku = "OK " + "\"" + k2.getKorisnik() + "\t" + k2.getPrezime() + "\t" + k2.getIme() + "\"";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                        }

                    }

                } else {

                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String author(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        OvlastiDAO odao = new OvlastiDAO();
                        Ovlasti o = null;
                        o = odao.dohvatiOvlast(argumenti[1], argumenti[3], pbp);
                        if (o == null) {
                            odgovorKorisniku = "ERROR 18 korisniku nije dodijeljeno podrucje!";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                        } else {
                            if (!o.isStatus()) {
                                odgovorKorisniku = "ERROR 14 ne postoji aktivno podrucje!";
                                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                            } else {

                                odgovorKorisniku = "OK";
                                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                            }
                        }

                    }

                } else {

                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String rights(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        OvlastiDAO odao = new OvlastiDAO();
                        List<Ovlasti> listaOvlasti = null;
                        listaOvlasti = odao.dohvatiSveOvlasti(argumenti[3], pbp);
                        String odg = "";
                        for (Ovlasti o : listaOvlasti) {
                            if (o.isStatus()) {
                                odg += o.getPodrucjeRada() + " ";
                            }
                        }
                        if (odg.trim().equals("")) {
                            odgovorKorisniku = "ERROR 18 Korisnik nema aktivnih podrucja!";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                        } else {
                            odgovorKorisniku = "OK " + odg;
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                        }
                    }
                } else {

                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String revoke(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        OvlastiDAO odao = new OvlastiDAO();
                        Ovlasti o = null;
                        o = odao.dohvatiOvlast(argumenti[4], argumenti[3], pbp);
                        if (o == null) {
                            odgovorKorisniku = "ERROR 18 korisniku nije dodijeljeno podrucje!";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                        } else {
                            if (!o.isStatus()) {
                                odgovorKorisniku = "ERROR 14 ne postoji aktivno podrucje!";
                                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                            } else {
                                Ovlasti o1 = new Ovlasti(argumenti[4], argumenti[3], false);
                                odao.azurirajOvlast(o1, pbp);
                                odgovorKorisniku = "OK";
                            }
                        }
                    }
                } else {
                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String grant(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    if (dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).getMaksBrojZahtjeva() == 0) {
                        odgovorKorisniku = "ERROR 16 broj preostalih zahtjeva u sjednici = 0";
                        dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                    } else {
                        smanjiBrZahtjeva(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]));
                        OvlastiDAO odao = new OvlastiDAO();
                        Ovlasti o = null;
                        o = odao.dohvatiOvlast(argumenti[4], argumenti[3], pbp);
                        if (o == null) {
                            Ovlasti o1 = new Ovlasti(argumenti[4], argumenti[3], true);
                            odao.dodajOvlasti(o1, pbp);
                            odgovorKorisniku = "OK";
                            dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);

                        } else {
                            if (o.isStatus()) {
                                odgovorKorisniku = "ERROR 13 zadano podrucje rada je aktivno!";
                                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                            } else {
                                Ovlasti o1 = new Ovlasti(argumenti[4], argumenti[3], true);
                                odao.azurirajOvlast(o1, pbp);
                                odgovorKorisniku = "OK";
                                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                            }
                        }

                    }
                } else {
                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String logout(String[] argumenti, String odgovorKorisniku, String zahtjev) throws NumberFormatException {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], "", Boolean.FALSE, pbp);
            if (k == null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                if (provjeraVazeceSjedniceId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2]))) {
                    Date date = new Date();
                    long trenutnoVrijeme = date.getTime();
                    dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).setMaksBrojZahtjeva(0);
                    dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])).setVrijemeDoKadaVrijedi(trenutnoVrijeme);
                    SlusacAplikacije.sjednice.removeIf(x -> x == dohvatiSjednicuId(SlusacAplikacije.sjednice, argumenti[1], Integer.parseInt(argumenti[2])));
                    odgovorKorisniku = "OK";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                } else {

                    odgovorKorisniku = "ERROR 15 Korisnik nema vazecu sjednicu";
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            }
            return odgovorKorisniku;
        }

        private String authen(String[] argumenti, String odgovorKorisniku, String zahtjev) {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], argumenti[2], Boolean.TRUE, pbp);
            if (k != null) {
                if (provjeraSjednice(SlusacAplikacije.sjednice, argumenti[1])) {
                    produljiPostojecuSjednicu(SlusacAplikacije.sjednice, argumenti[1], trajanjeSjednice);

                    int idSjednice = dohvatiSjednicu(SlusacAplikacije.sjednice, argumenti[1]).getId();
                    long doKadVrijedi = dohvatiSjednicu(SlusacAplikacije.sjednice, argumenti[1]).getVrijemeDoKadaVrijedi();
                    int maksZahtjeva = dohvatiSjednicu(SlusacAplikacije.sjednice, argumenti[1]).getMaksBrojZahtjeva();
                    odgovorKorisniku = "OK " + idSjednice + " " + doKadVrijedi + " " + maksZahtjeva;
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                } else {
                    Date date = new Date();
                    long trenutnoVrijeme = date.getTime();
                    long vrijemeDoKadaVrijedi = trenutnoVrijeme + trajanjeSjednice;
                    Sjednica sjednica = new Sjednica(SlusacAplikacije.sjednice.size() + 1, argumenti[1], trenutnoVrijeme,
                            vrijemeDoKadaVrijedi, Sjednica.StatusSjednice.Aktivna, maksBrojZahtjeva);
                    SlusacAplikacije.sjednice.add(sjednica);
                    odgovorKorisniku = "OK " + sjednica.getId() + " " + sjednica.getVrijemeDoKadaVrijedi() + " " + sjednica.getMaksBrojZahtjeva();
                    dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
                }
            } else {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " ne postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            }
            return odgovorKorisniku;
        }

        private String add(String[] argumenti, String odgovorKorisniku, String zahtjev) {
            KorisnikDAO kdao = new KorisnikDAO();
            Korisnik k = null;
            k = kdao.dohvatiKorisnika(argumenti[1], argumenti[2], Boolean.TRUE, pbp);
            if (k != null) {
                odgovorKorisniku = "ERROR 11 Korisnik pod nazivom " + argumenti[1] + " postoji!";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            } else {
                k = new Korisnik(argumenti[1], argumenti[2], argumenti[3].replaceAll("\"", ""), argumenti[4].replaceAll("\"", ""));
                kdao.dodajKorisnika(k, pbp);
                odgovorKorisniku = "OK";
                dodajZapisDnevnik(zahtjev, odgovorKorisniku, argumenti[1]);
            }
            return odgovorKorisniku;
        }
        // </editor-fold>

        public boolean testirajRegex(String sintaksa, String args[]) {

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            String p = sb.toString().trim();
            Pattern pattern = Pattern.compile(sintaksa);
            Matcher m = pattern.matcher(p);
            boolean status = m.matches();
            if (status) {
                int poc = 0;
                int kraj = m.groupCount();
                return true;
            } else {
                return false;
            }
        }
        // <editor-fold defaultstate="collapsed" desc=" Metode za sjednice ">
        /**
         * Metoda koja provjerava postoji li sjednica za dano korisnicko ime
         *
         * @param korisnickoIme argumenti komande
         * @return listaKorisnika
         */
        public boolean provjeraSjednice(List<Sjednica> sjednice, String korisnickoIme) {

            return sjednice.stream().anyMatch(a -> (a.getKorisnik().equals(korisnickoIme)));
        }

        public boolean provjeraVazeceSjedniceId(List<Sjednica> sjednice, String korisnickoIme, int id) {

            Date date = new Date();
            long trenutnoVrijeme = date.getTime();
            if (sjednice.stream().anyMatch(a -> (a.getKorisnik().equals(korisnickoIme) && a.getId() == id))) {

                if (dohvatiSjednicuId(sjednice, korisnickoIme, id).getVrijemeDoKadaVrijedi() >= trenutnoVrijeme) {
                    return true;
                }
            }

            return false;
        }

        public boolean provjeraSjedniceId(List<Sjednica> sjednice, String korisnickoIme, int id) {

            return sjednice.stream().anyMatch(a -> (a.getKorisnik().equals(korisnickoIme) && a.getId() == id));

        }

        /**
         * Metoda koja provjerava postoji li sjednica za dano korisnicko ime i id sjednice
         *
         * @param korisnickoIme korisnicko ime korisnika
         * @param id id sjednice
         * @return bool
         */
        /**
         * Metoda koja produljuje postojecu sjednicu
         *
         * @param korisnickoIme korisnicko ime korisnika
         * @param doKadVrijedi vrijeme za koliko se produljuje sjednica
         */
        public void produljiPostojecuSjednicu(List<Sjednica> sjednice, String korisnickoIme, long doKadVrijedi) {
            for (Sjednica s : sjednice) {
                if (s.getKorisnik().equals(korisnickoIme)) {
                    s.produljiVrijemeDoKadaVrijedi(doKadVrijedi);

                }
            }
        }

        /**
         * Metoda koja dohvaca sjednicu iz liste sjednica
         *
         * @param korisnickoIme korisnicko ime korisnika
         */
        public Sjednica dohvatiSjednicu(List<Sjednica> sjednice, String korisnickoIme) {
            Sjednica sjednica = null;
            for (Sjednica s : sjednice) {
                if (s.getKorisnik().equals(korisnickoIme)) {
                    sjednica = s;
                }
            }
            return sjednica;
        }

        public Sjednica dohvatiSjednicuId(List<Sjednica> sjednice, String korisnickoIme, int id) {
            Sjednica sjednica = null;
            for (Sjednica s : sjednice) {
                if (s.getKorisnik().equals(korisnickoIme) && s.getId() == id) {
                    sjednica = s;
                }
            }
            return sjednica;
        }

        public void smanjiBrZahtjeva(List<Sjednica> sjednice, String korisnickoIme, int id) {

            for (Sjednica s : sjednice) {
                if (s.getKorisnik().equals(korisnickoIme) && s.getId() == id) {

                    s.maksBrojZahtjeva--;

                }
            }

        }

        // </editor-fold>
    }
}
