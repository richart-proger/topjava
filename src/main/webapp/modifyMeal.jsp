<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Modify Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Modify meal</h2>

<table>
    <form action="mealServlet" method="post">
        <tr>
            <label>
                <input type="number" name="id" value="${id}" style="display:none;" readonly/>
            </label>
        </tr>
        <tr>
            <th>DateTime:</th>
            <td>
                <input type="datetime-local" name="dateTime" size="45" required/>
            </td>
        </tr>
        <tr>
            <th>Description:</th>
            <td>
                <input type="text" name="description" size="45" required/>
            </td>
        </tr>
        <tr>
            <th>Calories:</th>
            <td>
                <input type="number" name="calories" size="5" required/>
            </td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="Save"/>
            </td>
            <td>
                <input class="button" type="button" onclick="window.history.back();" value="Cancel"/>
            </td>
        </tr>
    </form>
</table>
</body>
</html>
