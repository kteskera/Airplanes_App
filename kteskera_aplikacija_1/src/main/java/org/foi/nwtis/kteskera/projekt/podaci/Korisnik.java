/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt.podaci;

/**
 *
 * @author NWTiS_1
 */
public class Korisnik {
    
      String korisnik;
    String lozinka;
    String prezime;
    String ime;

    public Korisnik(String korisnik, String lozinka, String prezime, String ime) {
        this.korisnik = korisnik;
        this.lozinka = lozinka;
        this.prezime = prezime;
        this.ime = ime;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }
    
}
