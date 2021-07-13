package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.rest.podaci.Lokacija;

public class AerodromDAO {

   
    public List<Aerodrom> dohvatiSveAerodrome(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airports`";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Aerodrom> airports = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String drzava = rs.getString("iso_country");
                    String naziv = rs.getString("name");

                    Lokacija lokacija = new Lokacija();
                    lokacija.setLongitude(rs.getString("coordinates").split(",")[0]);
                    lokacija.setLatitude(rs.getString("coordinates").split(",")[1]);

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    airports.add(a);
                }
                return airports;

            } catch (SQLException ex) {
                Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Aerodrom dohvatiAerodrom(PostavkeBazaPodataka pbp, String icao) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airports` WHERE `ident`=?";

        try {

            Class.forName(pbp.getDriverDatabase(url));
            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao2 = rs.getString("ident");
                    String drzava = rs.getString("iso_country");
                    String naziv = rs.getString("name");

                    Lokacija lokacija = new Lokacija();
                    lokacija.setLongitude(rs.getString("coordinates").split(",")[0]);
                    lokacija.setLatitude(rs.getString("coordinates").split(",")[1]);

                    Aerodrom a = new Aerodrom(icao2, naziv, drzava, lokacija);
                    return a;
                }

            } catch (SQLException ex) {
                Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
     public List<Aerodrom> dohvatiSveAerodromePo(PostavkeBazaPodataka pbp, String name2, String state) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airports` WHERE `name` LIKE ? AND `iso_country`=? ";

        try {

            List<Aerodrom> airports = new ArrayList<>();
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, "%" + name2 + "%");
                s.setString(2, state);

                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String drzava = rs.getString("iso_country");
                    String naziv = rs.getString("name");

                    Lokacija lokacija = new Lokacija();
                    lokacija.setLongitude(rs.getString("coordinates").split(",")[0]);
                    lokacija.setLatitude(rs.getString("coordinates").split(",")[1]);

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    airports.add(a);
                }
                return airports;

            } catch (SQLException ex) {
                Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Aerodrom> dohvatiSveAerodromePoNazivu(PostavkeBazaPodataka pbp, String name2) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airports` WHERE `name` LIKE ?";

        try {

            List<Aerodrom> airports = new ArrayList<>();
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, "%" + name2 + "%");
                

                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String drzava = rs.getString("iso_country");
                    String naziv = rs.getString("name");

                    Lokacija lokacija = new Lokacija();
                    lokacija.setLongitude(rs.getString("coordinates").split(",")[0]);
                    lokacija.setLatitude(rs.getString("coordinates").split(",")[1]);

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    airports.add(a);
                }
                return airports;

            } catch (SQLException ex) {
                Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
     public List<Aerodrom> dohvatiSveAerodromePoNazivuDrzave(PostavkeBazaPodataka pbp, String drzava2) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airports` WHERE `iso_country`=?";

        try {

            List<Aerodrom> airports = new ArrayList<>();
            Class.forName(pbp.getDriverDatabase(url));

            try (Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, drzava2);
                

                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao = rs.getString("ident");
                    String drzava = rs.getString("iso_country");
                    String naziv = rs.getString("name");
                    Lokacija lokacija = new Lokacija();
                    lokacija.setLongitude(rs.getString("coordinates").split(",")[0]);
                    lokacija.setLatitude(rs.getString("coordinates").split(",")[1]);

                    Aerodrom a = new Aerodrom(icao, naziv, drzava, lokacija);
                    airports.add(a);
                }
                return airports;

            } catch (SQLException ex) {
                Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AerodromDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
