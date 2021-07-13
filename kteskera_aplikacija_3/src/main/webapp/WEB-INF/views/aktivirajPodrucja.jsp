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
                let podrucjaKorisnika = $("#korisnici").DataTable({

                });
            });
        </script>  

    </head>
    <body>
        <div><a href="${pageContext.servletContext.contextPath}/mvc/korisnik/izbornik">Početna</a></div>
        <h1>Pregled ovlasti</h1>

        <div>${greska}</div>

        <table id="korisnici" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>Korisničko ime</th>                  
                    <th>Aktiviraj</th>
                    <th>Deaktiviraj</th>

                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${korisnici}" >
                    <tr>
                        <td><label name="korisnik">${item.korisnik}</label></td>

                        <td>
                            <form action="${pageContext.servletContext.contextPath}/mvc/podrucja/aktivirajPodrucja?korisnik=${item.korisnik}" method="POST">
                                <select name="podrucje" id="podrucje">
                                    <c:forEach var="item2" items="${podrucja}" >
                                        <option value="${item2}">${item2}</option>
                                    </c:forEach>
                                </select>
                                <input style="text-align: center;vertical-align: middle;" type="submit" value="AKTIVIRAJ" />

                            </form>
                        </td>


                        <td>
                            <form action="${pageContext.servletContext.contextPath}/mvc/podrucja/deaktivirajPodrucja?korisnik=${item.korisnik}" method="POST">
                                <select name="podrucje" id="podrucje">
                                    <c:forEach var="item2" items="${podrucja}" >
                                        <option value="${item2}">${item2}</option>
                                    </c:forEach>
                                </select>
                                <input style="text-align: center;vertical-align: middle;" type="submit" value="DEAKTIVIRAJ" />
                            </form>
                        </td>
                       
                      

                    </tr>
                </c:forEach>

            </tbody>
            <tfoot>
                <tr>
                    <th>Korisničko ime</th>                  
                    <th>Aktiviraj</th>
                    <th>Deaktiviraj</th>                   
                </tr>
            </tfoot>
        </table>



    </body>
</html>
