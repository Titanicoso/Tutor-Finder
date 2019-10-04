<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/schedule.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="profile.title" /></title>
</head>

<body class="staticProfile">

<%@ include file="navbar.jsp" %>

<div class="content">
    <%--<img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />" />--%>
    <divc class="profile-profesor">
        <img class="profile-picture" alt="Profile picture" src="<c:url value="data:image/jpeg;base64,${professor.picture}" />"/>
        <h1 class="profile-name m-0-10"><c:out value="${professor.name} ${professor.lastname}" escapeXml="true"/></h1>
        <h3 class="profile-description m-0-10"><c:out value="${professor.description}" escapeXml="true"/></h3>
    </divc>
    <div class="class-content">
        <div class="classes">
            <div>
                <h2><spring:message code="clasesTitle"/></h2>
                <p><spring:message code="clasesDescription" /></p>
            </div>
            <c:if test="${courses.results.size() == 0}">
                <h4><spring:message code="no.courses"/></h4>
            </c:if>
            <c:forEach var="course" items="${courses.results}">
                <div class="class">
                    <a class="class-button" href="<c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                    <h3 class="class-title"><c:out value="${course.subject.name}" escapeXml="true"/></h3>
                    <div class="class-description"><c:out value="${course.description}" escapeXml="true"/></div>
                </div>
            </c:forEach>

        <div class="paged-result-buttons">
            <c:url value="/Professor/${professor.id}?page=${page - 1}" var="previous"/>
            <c:url value="/Professor/${professor.id}?page=${page + 1}" var="next"/>

                <c:if test="${page > 1}">
                    <a href="${previous}" class="previous round">&#8249;</a>
                </c:if>
                <c:if test="${courses.hasNext}">
                    <a href="${next}" class="next round">&#8250;</a>
                </c:if>
            </div>
        </div>
    </div>

    <div class="time-content time-content-other">
        <div class="schedule">
            <h2><spring:message code="schedule.title"/></h2>
            <p><spring:message code="schedule.description" /></p>
            <c:choose>
                <c:when test="${schedule.monday.isEmpty() && schedule.tuesday.isEmpty() && schedule.wednesday.isEmpty() && schedule.thursday.isEmpty() && schedule.friday.isEmpty() && schedule.saturday.isEmpty() && schedule.sunday.isEmpty()}">
                    <h4><spring:message code="schedule.empty"/></h4>
                </c:when>
                <c:otherwise>
                    <%@ include file="schedule.jsp"%>
                </c:otherwise>
            </c:choose>
        </div>
    </div>


</div>
<div class="footer">
</div>
</body>

</html>