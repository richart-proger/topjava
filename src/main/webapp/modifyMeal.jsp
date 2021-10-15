<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Modify Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action == 'create' ? 'Add meal' : 'Edit meal'}</h2>

<table>
    <jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
    <form action="meals" method="post">
        <%--<tr>--%>
            <%--<label>--%>
                <%--<input type="number" name="id" value="${id}" style="display:none;" readonly/>--%>
            <%--</label>--%>
        <%--</tr>--%>
            <input type="hidden" name="id" value="${meal.id}">
        <tr>
            <th>DateTime:</th>
            <td>
                <input type="datetime-local" name="dateTime" value="${meal.dateTime}" size="45" required/>
            </td>
        </tr>
        <tr>
            <th>Description:</th>
            <td>
                <input type="text" name="description" value="${meal.description}" size="45" required/>
            </td>
        </tr>
        <tr>
            <th>Calories:</th>
            <td>
                <input type="number" name="calories" value="${meal.calories}" size="5" required/>
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
