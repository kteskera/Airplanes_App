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
        <h1>Slanje slobodne komande</h1>

        <hr>

        <div>${poruka}</div>
        <form action="${pageContext.servletContext.contextPath}/mvc/komanda/slanjeKomande/" method="POST">
            <input type="text" name="komanda"/>
            <td style="text-align: center;vertical-align: middle;"><input type="submit" value="POŠALJI" /></td>
        </form>
    </body>
</html>
