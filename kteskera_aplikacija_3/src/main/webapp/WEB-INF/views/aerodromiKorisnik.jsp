<%-- 
    Document   : aerodromiKorisnikPrati
    Created on : May 7, 2021, 12:02:15 PM
    Author     : NWTiS_1
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="https://code.jquery.com/jquery-1.11.3.min.js" type="text/javascript"></script>  
        <script src="https://cdn.datatables.net/1.10.9/js/jquery.dataTables.min.js" type="text/javascript"></script>  
        <link rel="stylesheet" href="https://cdn.datatables.net/1.10.9/css/jquery.dataTables.min.css" />  
        <script type="text/javascript">
            $(document).ready(function ()
            {
                let aerodromi = $("#aerodromi").DataTable({

                });
            });
        </script>  

    </head>
    <body>
        <div><a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Početna</a></div>
        <h1>Aerodromi koje korisnici prate</h1>

        <table id="aerodromi" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>ICAO</th>
                    <th>Država</th>
                    <th>Naziv</th>  
                    <th>Latitude</th>
                    <th>Longitude</th>
                  <th>Prekini preplatu</th>
                    <th>Prikaži detalje</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${aerodromiKojiPratiKorisnik}" >
                    <tr>
                
                    <td>${item.icao}</td>
                    <td>${item.drzava}</td>
                    <td>${item.naziv}</td>
                    <td>${item.lokacija.latitude}</td>
                    <td>${item.lokacija.longitude}</td>
                    <form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiPrekiniPreplatu/${item.icao}" method="POST">
                    <td style="text-align: center;vertical-align: middle;"><input type="submit" value="UKLONI" /></td>
                </form>
                    <form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiKorisnik/${item.icao}" method="POST">
                    <td style="text-align: center;vertical-align: middle;"><input type="submit" value="ODABERI" /></td>
                </form>
            </tr>
        </c:forEach>

    </tbody>
    <tfoot>
        <tr>
            <th>ICAO</th>
            <th>Država</th>
            <th>Naziv</th>
            <th>Latitude</th>
            <th>Longitude</th>
            <th>Prekini preplatu</th>
            <th>Prikaži detalje</th>
        </tr>
    </tfoot>
</table>
        
        <hr>
        <h3>Dodaj preplatu na aerodrom</h3>
        <div>${greska}</div>
        <form action="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiDodajPreplatu/" method="POST">
            <input type="text" name="icao"/>
            <td style="text-align: center;vertical-align: middle;"><input type="submit" value="PREPLATA" /></td>
        </form>
</body>
</html>
