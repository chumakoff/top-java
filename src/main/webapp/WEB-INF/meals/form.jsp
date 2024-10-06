<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .form-group {
        margin-bottom: 1rem
    }
</style>

<form action="${meal.id == null ? 'create' : 'update'}" method="post">
    <c:if test="${meal.id != null}">
    <input type="hidden" name="id" value="${meal.id}"/></c:if>

    <div class="form-group">
        <label>Time:</label>
        <input type="datetime-local" name="dateTime" value="${meal.dateTime.toString()}"/>
    </div>

    <div class="form-group">
        <label>Description:</label>
        <input type="text" name="description" value="${meal.description}"/>
    </div>

    <div class="form-group">
        <label>Calories:</label>
        <input type="number" name="calories" value="${meal.calories}"/>
    </div>

    <div class="form-group">
        <input type="submit" value="${meal.id == null ? 'Add Meal' : 'Update Meal'}"/>
    </div>
</form>

