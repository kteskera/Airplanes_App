<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Karlo Teskera -Aplikacija 3</title>
    </head>
    <body>
        <h1>Karlo Teskera - Aplikacija 3</h1>

        <ul>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/korisnik/odjava">Odjava korisnika</a></li>
            <br>
            <li><a href="${pageContext.servletContext.contextPath}/mvc/podrucja/aktivirajPodrucja">Dodavanje/aktiviranje područja</a></li>
             <br>           
            <li><a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiKorisnik">Pregled aerodroma</a></li>
            <br>           
            <li><a href="${pageContext.servletContext.contextPath}/mvc/komanda/slanjeKomande">Slanje komande</a></li>
        </ul>
    </body>
</html>
