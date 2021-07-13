package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

public class MeteoDAO {

    public boolean dodajMeteo(Meteo meteo, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO `meteo` (ident, temperatura, vlaga, tlak, brzina_vjetra, smjer_vjetra, date) "
                + "VALUES (?,?,?,?,?,?,?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {
                s.setString(1, meteo.getIdent());
                s.setFloat(2, meteo.getTemperatura());
                s.setFloat(3, meteo.getVlaga());
                s.setFloat(4, meteo.getTlak());
                s.setFloat(5, meteo.getBrzinaVjetra());
                s.setFloat(6, meteo.getBrzinaVjetra());
                s.setTimestamp(7, meteo.getDatum());

                int brojAzuriranja = s.executeUpdate();
                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeteoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Meteo> dohvatiMeteoPodatke(String datum, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * from `meteo` where `ident`=? AND `date`>= ? ORDER BY `date` ASC LIMIT 1";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Meteo> meteoPodaci = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, datum);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String ident = rs.getString("ident");
                    float temperatura = rs.getFloat("temperatura");
                    float vlaga = rs.getFloat("vlaga");
                    float tlak = rs.getFloat("tlak");
                    float brzina_vjetra = rs.getFloat("brzina_vjetra");
                    float smjer_vjetra = rs.getFloat("smjer_vjetra");
                    Timestamp date = rs.getTimestamp("date");

                    Meteo m = new Meteo(ident, temperatura, vlaga, tlak, brzina_vjetra, smjer_vjetra, date);
                    meteoPodaci.add(m);
                }
                return meteoPodaci;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public List<Meteo> dohvatiMeteoPodatkeZaDan(String datum, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * from `meteo` where `ident`=? AND `date`>=? AND `date`<? + INTERVAL 1 DAY";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Meteo> meteoPodaci = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, datum);
                s.setString(3, datum);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String ident = rs.getString("ident");
                    float temperatura = rs.getFloat("temperatura");
                    float vlaga = rs.getFloat("vlaga");
                    float tlak = rs.getFloat("tlak");
                    float brzina_vjetra = rs.getFloat("brzina_vjetra");
                    float smjer_vjetra = rs.getFloat("smjer_vjetra");
                    Timestamp date = rs.getTimestamp("date");

                    Meteo m = new Meteo(ident, temperatura, vlaga, tlak, brzina_vjetra, smjer_vjetra, date);
                    meteoPodaci.add(m);
                }
                return meteoPodaci;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
