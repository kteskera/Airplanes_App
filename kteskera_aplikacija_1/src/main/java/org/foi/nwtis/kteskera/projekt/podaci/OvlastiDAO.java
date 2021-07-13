package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;

public class OvlastiDAO {

    public List<Ovlasti> dohvatiSveOvlasti(String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM ovlasti WHERE korisnik = ?";
        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Ovlasti> listaOvlasti = new ArrayList<>();
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String podrucjeRada = rs.getString("podrucjerada");
                    boolean status = rs.getBoolean("status");

                    Ovlasti o = new Ovlasti(korisnik1, podrucjeRada, status);
                    listaOvlasti.add(o);
                }
                return listaOvlasti;
            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Ovlasti dohvatiOvlast(String korisnik, String podrucjerada, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM ovlasti WHERE korisnik = ? AND podrucjerada = ?";
        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                s.setString(2, podrucjerada);
                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    String korisnik1 = rs.getString("korisnik");
                    String podrucjeRada = rs.getString("podrucjerada");
                    boolean status = rs.getBoolean("status");

                    Ovlasti o = new Ovlasti(korisnik1, podrucjeRada, status);
                    return o;
                }

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean azurirajOvlast(Ovlasti o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "UPDATE ovlasti SET status = ? WHERE korisnik = ? AND podrucjerada = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setBoolean(1, o.isStatus());
                s.setString(2, o.getKorisnik());
                s.setString(3, o.getPodrucjeRada());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean dodajOvlasti(Ovlasti o, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO ovlasti VALUES (?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, o.getKorisnik());
                s.setString(2, o.getPodrucjeRada());
                s.setBoolean(3, o.isStatus());

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OvlastiDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
