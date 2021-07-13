package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Connection;
import java.sql.Date;
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

public class DnevnikDAO {

    public boolean dodajZapis(Dnevnik d, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO `dnevnik`(spremljeno, sadrzaj, status,korisnik) "
                + "VALUES (CURRENT_TIMESTAMP,?,?,?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {               
                s.setString(1, d.getSadrzaj());
                s.setString(2, d.getStatus());
                s.setString(3, d.getKorisnik());
                int brojAzuriranja = s.executeUpdate();
                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(DnevnikDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DnevnikDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<Dnevnik> dohvatiZapise(String korisnik, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * from `dnevnik` where `korisnik`=? ";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Dnevnik> dnevnikPodaci = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);

                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    Timestamp spremljeno = rs.getTimestamp("spremljeno");
                    String sadrzaj = rs.getString("sadrzaj");
                    String status = rs.getString("status");
                    String korisnik2 = rs.getString("korisnik");
                    Dnevnik d = new Dnevnik(spremljeno.toString(), sadrzaj, status, korisnik2);
                    dnevnikPodaci.add(d);
                }
                return dnevnikPodaci;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public int dohvatiBrojZapisa(String korisnik, Timestamp vrijemeOd, Timestamp vrijemeDo, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT COUNT(*) from `dnevnik` where `korisnik`=? AND `spremljeno`>=? AND `spremljeno` <=? ";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<Dnevnik> dnevnikPodaci = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, korisnik);
                s.setTimestamp(2, vrijemeOd);
                s.setTimestamp(3, vrijemeDo);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    int count = rs.getInt(1);

                    return count;
                }

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;

    }

}
