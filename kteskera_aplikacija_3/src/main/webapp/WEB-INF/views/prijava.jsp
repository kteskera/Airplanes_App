<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <div><a href="${pageContext.servletContext.contextPath}">Početna</a></div>
        <h1>Prijava</h1>
        <hr>
        <div>${greska}</div>
        <form method="POST" action="${pageContext.servletContext.contextPath}/mvc/korisnik/prijava">
            <table>
                <tr>
                    <td>Korisničko ime</td>
                    <td><input type="text" name="korisnik"/></td>
                </tr>
                <tr>
                    <td>Lozinka</td>
                    <td><input type="password" name="lozinka"/></td>
                </tr>
                <tr>
                    <td></td>
                    <td><input type="submit" name="submit" value="PRIJAVA"/></td>
                </tr>
            </table>
        </form>
    </body>
</html>
