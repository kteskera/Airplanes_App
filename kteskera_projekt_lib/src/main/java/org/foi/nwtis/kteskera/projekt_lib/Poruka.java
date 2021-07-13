/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.kteskera.projekt_lib;

import java.io.Serializable;
import org.foi.nwtis.podaci.Korisnik;


public class Poruka implements Serializable{
    private String naslov;
    private String sadrzaj;
    private Korisnik korisnik;

    public Poruka(String naslov, String sadrzaj, Korisnik korisnik) {
        this.naslov = naslov;
        this.sadrzaj = sadrzaj;
        this.korisnik = korisnik;
    }

    public Poruka() {
    }
   

    public String getNaslov() {
        return naslov;
    }

    public void setNaslov(String naslov) {
        this.naslov = naslov;
    }

    public String getSadrzaj() {
        return sadrzaj;
    }

    public void setSadrzaj(String sadrzaj) {
        this.sadrzaj = sadrzaj;
    }

    public Korisnik getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }

    @Override
    public String toString() {
        return "Poruka{" + "naslov=" + naslov + ", sadrzaj=" + sadrzaj + ", korisnik=" + korisnik + '}';
    }

   
    
    
    
    
}
