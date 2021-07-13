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
import org.foi.nwtis.rest.podaci.AvionLeti;

public class AirplanesDAO {

    /**
     *
     * @param pbp
     * @return airplanes
     */
    public List<AvionLeti> dohvatiSveAvione(PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * FROM `airplanes`";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<AvionLeti> airplanes = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    Statement s = con.createStatement();
                    ResultSet rs = s.executeQuery(upit)) {

                while (rs.next()) {
                    String icao24 = rs.getString("icao24");
                    int firstSeen = rs.getInt("firstSeen");
                    String estDepartureAirport = rs.getString("estDepartureAirport");
                    int lastSeen = rs.getInt("lastSeen");
                    String estArrivalAirport = rs.getString("estArrivalAirport");
                    String callsign = rs.getString("callsign");
                    int estDepartureAirportHorizDistance = rs.getInt("estDepartureAirportHorizDistance");
                    int estDepartureAirportVertDistance = rs.getInt("estDepartureAirportVertDistance");
                    int estArrivalAirportHorizDistance = rs.getInt("estArrivalAirportHorizDistance");
                    int estArrivalAirportVertDistance = rs.getInt("estArrivalAirportVertDistance");
                    int departureAirportCandidatesCount = rs.getInt("departureAirportCandidatesCount");
                    int arrivalAirportCandidatesCount = rs.getInt("arrivalAirportCandidatesCount");
                    Timestamp stored = rs.getTimestamp("stored");

                    AvionLeti a = new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount);
                    airplanes.add(a);
                }
                return airplanes;

            } catch (SQLException ex) {
                Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     *
     * @param a AvionLeti
     * @param pbp PostavkeBazaPodataka
     * @return true/false
     */
    public boolean dodajAvion(AvionLeti a, String date, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "INSERT INTO `airplanes` (icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount, `stored`,flightDate)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {
                s.setString(1, a.getIcao24());
                s.setInt(2, a.getFirstSeen());
                s.setString(3, a.getEstDepartureAirport());
                s.setInt(4, a.getLastSeen());
                s.setString(5, a.getEstArrivalAirport());
                s.setString(6, a.getCallsign());
                s.setInt(7, a.getEstDepartureAirportHorizDistance());
                s.setInt(8, a.getEstDepartureAirportVertDistance());
                s.setInt(9, a.getEstArrivalAirportHorizDistance());
                s.setInt(10, a.getEstArrivalAirportVertDistance());
                s.setInt(11, a.getDepartureAirportCandidatesCount());
                s.setInt(12, a.getArrivalAirportCandidatesCount());
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                s.setTimestamp(13, timestamp);
                s.setString(14, date);
                //System.out.println(upit);
                int brojAzuriranja = s.executeUpdate();
                return brojAzuriranja == 1;

            } catch (SQLException ex) {
                Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AirplanesDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public List<AvionLeti> dohvatiAvionePoDatumu(String datum, String icao, PostavkeBazaPodataka pbp) {
        String url = pbp.getServerDatabase() + pbp.getUserDatabase();
        String bpkorisnik = pbp.getUserUsername();
        String bplozinka = pbp.getUserPassword();
        String upit = "SELECT * from `airplanes` where `estDepartureAirport`=? AND `flightDate`= ?";

        try {
            Class.forName(pbp.getDriverDatabase(url));
            List<AvionLeti> airplanes = new ArrayList<>();

            try (
                    Connection con = DriverManager.getConnection(url, bpkorisnik, bplozinka);
                    PreparedStatement s = con.prepareStatement(upit)) {

                s.setString(1, icao);
                s.setString(2, datum);
                ResultSet rs = s.executeQuery();
                while (rs.next()) {
                    String icao24 = rs.getString("icao24");
                    int firstSeen = rs.getInt("firstSeen");
                    String estDepartureAirport = rs.getString("estDepartureAirport");
                    int lastSeen = rs.getInt("lastSeen");
                    String estArrivalAirport = rs.getString("estArrivalAirport");
                    String callsign = rs.getString("callsign");
                    int estDepartureAirportHorizDistance = rs.getInt("estDepartureAirportHorizDistance");
                    int estDepartureAirportVertDistance = rs.getInt("estDepartureAirportVertDistance");
                    int estArrivalAirportHorizDistance = rs.getInt("estArrivalAirportHorizDistance");
                    int estArrivalAirportVertDistance = rs.getInt("estArrivalAirportVertDistance");
                    int departureAirportCandidatesCount = rs.getInt("departureAirportCandidatesCount");
                    int arrivalAirportCandidatesCount = rs.getInt("arrivalAirportCandidatesCount");
                    Timestamp stored = rs.getTimestamp("stored");
                    AvionLeti a = new AvionLeti(icao24, firstSeen, estDepartureAirport, lastSeen, estArrivalAirport, callsign, estDepartureAirportHorizDistance, estDepartureAirportVertDistance, estArrivalAirportHorizDistance, estArrivalAirportVertDistance, departureAirportCandidatesCount, arrivalAirportCandidatesCount);
                    airplanes.add(a);
                }
                return airplanes;

            } catch (SQLException ex) {
                Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MyAirportsLogDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
