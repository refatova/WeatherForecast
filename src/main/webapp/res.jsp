<%@ page language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--@ page import="com.weather.servlet.Weather" --%>
<%@ page import="java.util.*" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Weather</title>

<style>
td,th{
padding: 5px;
padding-right: 5px;
border: 1px solid black;
}
</style>
</head>

<%--
List<Weather> resultList=(List<Weather>)request.getAttribute("resultList");
--%>




<body>
<table><caption style="margin-bottom: 10px;font-size: 180%; font-family: Arial, Helvetica, sans-serif; ">Weather for today</caption>
<tr><th>Country</th>
    <th>City</th>
    <th>General information</th>
    <th>Temeperature</th>
    <th>Wind speed</th>
    <th>Source</th>
    <th>Latitude</th>
    <th>Longitude</th>
</tr>
<fmt:setLocale value="en_US"/>
<c:forEach items="${list}" var="weather">
     <tr>
                <td>
                    <c:out value="${weather.getCountry()}"></c:out></td>
                <td>
                    <c:out value="${weather.getName()}"></c:out></td>
                <td>
                    <c:out value="${weather.getMain()}"></c:out></td>
                <td>
                    <c:set var="temp" value="${weather.getTemp()}" />
                    <fmt:formatNumber value="${temp}" maxFractionDigits="3"/></td>
                <td>
                    <c:set var="wind" value="${weather.getWindSpeed()}"/>
                    <fmt:formatNumber value="${wind}" maxFractionDigits="3"/></td>
                <td>
                    <c:out value="${weather.getSource()}"></c:out></td>
                <td>
                    <c:set var="lat" value="${weather.getLatitude()}"/>
                    <fmt:formatNumber value="${lat}" maxFractionDigits="3"/></td>
                <td>
                    <c:set var="lon" value="${weather.getLongitude()}"/>
                    <fmt:formatNumber value="${lon}" maxFractionDigits="3"/></td>


             </tr>
</c:forEach>

</table>
</body>
</html>