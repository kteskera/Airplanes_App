package org.foi.nwtis.kteskera.projekt.podaci;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.kteskera.konfiguracije.bazaPodataka.PostavkeBazaPodataka;
import org.foi.nwtis.podaci.MyAirport;

public class MyAirportsDAO {

    /**
     *
     * @param ident
     * @param username
     * @param pbp
     * @return ma MyAirport
     */
    public MyAirport dohvatiMyAirport(String ident, String username, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * "
                + "FROM `myairports` WHERE `username` = ? AND `ident` = ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, username);
                s.setString(2, ident);

                ResultSet rs = s.executeQuery();

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ident2 = rs.getString("ident");
                    String username2 = rs.getString("username");
                    MyAirport ma = new MyAirport(ident2, username2, true);
                    return ma;
                }

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param pbp
     * @return
     */
    public List<MyAirport> dohvatiSveMyAirport(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `myairports`";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            List<MyAirport> myAirports = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String ident = rs.getString("ident");
                    String username = rs.getString("username");
                    Timestamp stored = rs.getTimestamp("stored");

                    MyAirport ma = new MyAirport(ident, username, true);

                    myAirports.add(ma);
                }
                return myAirports;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param mya
     * @param pbp
     * @return
     */
    public boolean dodajMyAirportZapis(MyAirport mya, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO `myairports` (ident, username, `stored`) "
                + "VALUES (?, ?, ?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, mya.getIdent());
                s.setString(2, mya.getUsername());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                s.setTimestamp(3, timestamp);
                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     *
     * @param ident
     * @param username
     * @param pbp
     * @return
     */
    public boolean obrisiZapis(String ident, String username, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "DELETE FROM `myairports` WHERE `ident`=? AND `username`=?";

        try {
            Class.forName(pbp.getDriverDatabase(url));

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, ident);
                s.setString(2, username);

                int brojAzuriranja = s.executeUpdate();

                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
