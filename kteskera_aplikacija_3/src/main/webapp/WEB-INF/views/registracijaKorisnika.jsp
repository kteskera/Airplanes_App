<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div><a href="${pageContext.servletContext.contextPath}">Početna</a></div>
        <h1>Registracija</h1>
        <hr>
        <div>${greska}</div>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnik/registracija">
            <table>
                <tr>
                    <td>Ime</td>
                    <td><input type="text" name="imeReg"/></td>
                </tr>
                <tr>
                    <td>Prezime</td>
                    <td><input type="text" name="prezimeReg"/></td>
                </tr>

                <tr>
                    <td>Korisničko ime</td>
                    <td><input type="text" name="korime"/></td>
                </tr>
                <tr>
                    <td>Lozinka</td>
                    <td><input type="password" name="lozinkaReg"/></td>
                </tr>
               
                <tr>
                    <td><input type="submit" name="submit" value="REGISTRACIJA"/></td>
                </tr>

            </table>
        </form>
    </body>
</html>
