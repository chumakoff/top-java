<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Meals</title>
    <style>
        table {
            width: 100%;
            border-collapse: collapse;
            margin: 25px 0;
            font-size: 0.9em;
            font-family: sans-serif;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.15);
        }
        thead tr {
            background-color: #009879;
            color: #ffffff;
            text-align: left;
        }
        th, td {
            padding: 12px 15px;
        }
        tbody tr {
            border-bottom: 1px solid #dddddd;
        }
        tbody tr:nth-of-type(even) {
            background-color: #f3f3f3;
        }
        tbody tr:last-of-type {
            border-bottom: 2px solid #009879;
        }

        tr.excess td {
            color: darkred;
        }
    </style>
</head>
<body>

<%@ include file="../header.jsp" %>

<h1>Meals</h1>

<p><a href="meals/new">New Meal</a></p>

<table>
    <thead>
        <tr>
            <th>Time</th>
            <th>Description</th>
            <th>Calories</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach items="${meals}" var="meal">
            <tr class="${meal.excess ? 'excess' : ''}">
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern('yyyy-MMM-dd HH:mm'))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>
                    <a href="meals/edit?id=${meal.id}">Edit2</a><br>

                    <form action="meals/delete" method="post">
                        <input type="hidden" name="id" value="${meal.id}"/>
                        <input type="submit" value="Delete"/>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

</body>
</html>
