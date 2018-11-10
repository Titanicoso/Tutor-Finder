<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="course.create"/></title>

    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/courseForm.js" />"></script>

</head>

<body class="register">

<%@ include file="navbar.jsp" %>

<div class="content">
    <div class="button-container">
        <h2 class="label"><spring:message code="course.create" /></h2>
    </div>
    <c:url value="/createCourse" var="postPath"/>
    <form:form cssClass="form" modelAttribute="CourseForm" action="${postPath}" method="post">
        <div>
            <form:label cssClass="label" path="subjectId"><spring:message code="course.subject"/></form:label>
            <form:select cssClass="input-request course-select" path="subjectId">
                <form:option selected="selected" value=""><spring:message code="select.subject"/></form:option>
                <c:forEach var="subject" items="${subjects}">
                    <form:option value="${subject.id}">${subject.name}</form:option>
                </c:forEach>
            </form:select>
            <form:errors cssClass="error-text" path="subjectId" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="description"><spring:message code="description"/></form:label>
            <form:textarea cssClass="input-request" type="text" path="description" rows="5" cols="5"/>
            <form:errors cssClass="error-text" path="description" element="p"/>
        </div>
        <div>
            <form:label cssClass="label" path="price"><spring:message code="course.price"/></form:label>
            <form:input cssClass="input-request" type="number" step="0.01" path="price"/>
            <form:errors cssClass="error-text" path="price" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="create"/>"/>
        </div>
    </form:form>
</div>
<div class="footer">
</div>
</body>

</html>