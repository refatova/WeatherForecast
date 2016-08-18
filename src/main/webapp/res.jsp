<%@ page language="java" %>
<%@ page import="com.weather.servlet.Weather" %>
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
<%-- List<Weather> resultList=session.getAttribute("resultList") --%>
<%
List<Weather> resultList=(List<Weather>)request.getAttribute("resultList");
//out.println("rs==="+resultList);
%>
<%

%>

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

    <%
    //out.println(request.getAttribute("select-country"));
 //   out.println(request.getAttribute("resultList"));

    for(Weather weather : resultList)
    {
    %>
        <tr>
            <td><%=weather.getCountry()%></td>
            <td><%=weather.getName()%></td>
            <td><%=weather.getMain()%></td>
            <td><%=weather.getTemp()%></td>
            <td><%=weather.getWindSpeed()%></td>
            <td><%=weather.getSource()%></td>
            <td><%=weather.getLatitude()%></td>
            <td><%=weather.getLongitude()%></td>


         </tr>

    <%
    }
    %>


<%--
    <c:forEach items="${resultList}" var="item">
        <tr>
           ${item}


        </tr>
    </c:forEach>
    --%>
</table>
</body>
</html>