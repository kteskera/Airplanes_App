package org.foi.nwtis.kteskera.projekt.podaci;

public class Sjednica {

    static public enum StatusSjednice {
        Aktivna, Neaktivna

    }
    public int id;
    public String korisnik;
    public long vrijemeKreiranja;
    public long vrijemeDoKadaVrijedi;
    public StatusSjednice status;
    public int maksBrojZahtjeva;

    public Sjednica() {
    }

    public Sjednica(int id,String korisnik, long vrijemeKreiranja, long vrijemeDoKadaVrijedi, StatusSjednice status, int maksBrojZahtjeva) {
        this.id=id;
        this.korisnik = korisnik;
        this.vrijemeKreiranja = vrijemeKreiranja;
        this.vrijemeDoKadaVrijedi = vrijemeDoKadaVrijedi;
        this.status = status;
        this.maksBrojZahtjeva=maksBrojZahtjeva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public long getVrijemeKreiranja() {
        return vrijemeKreiranja;
    }

    public void setVrijemeKreiranja(long vrijemeKreiranja) {
        this.vrijemeKreiranja = vrijemeKreiranja;
    }

    public long getVrijemeDoKadaVrijedi() {
        return vrijemeDoKadaVrijedi;
    }

    public void setVrijemeDoKadaVrijedi(long vrijemeDoKadaVrijedi) {
        this.vrijemeDoKadaVrijedi = vrijemeDoKadaVrijedi;
    }

    public void produljiVrijemeDoKadaVrijedi(long vrijemeDoKadaVrijedi) {
        this.vrijemeDoKadaVrijedi += vrijemeDoKadaVrijedi;
    }

    public StatusSjednice getStatus() {
        return status;
    }

    public void setStatus(StatusSjednice status) {
        this.status = status;
    }

    public int getMaksBrojZahtjeva() {
        return maksBrojZahtjeva;
    }

    public void setMaksBrojZahtjeva(int maksBrojZahtjeva) {
        this.maksBrojZahtjeva = maksBrojZahtjeva;
    }
    
   

}
