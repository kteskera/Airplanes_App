
package org.foi.nwtis.kteskera.projekt.podaci;

public class Dnevnik {

    public String spremljeno;

    public String sadrzaj;

    public String status;

    public String korisnik;

    public Dnevnik() {
    }

    public Dnevnik(String spremljeno, String sadrzaj, String status, String korisnik) {
        this.spremljeno = spremljeno;
        this.sadrzaj = sadrzaj;
        this.status = status;
        this.korisnik = korisnik;
    }

    public String getSpremljeno() {
        return spremljeno;
    }

    public void setSpremljeno(String spremljeno) {
        this.spremljeno = spremljeno;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

}
