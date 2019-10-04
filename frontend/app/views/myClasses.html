<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link href="<c:url value="/resources/css/stylesheet.css" />" rel="stylesheet">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>

    <title>Tu Teoria | <spring:message code="classRequests.title" /> </title>
</head>

<body class="body">
<c:url value="/searchResults" var="postPath"/>

<%@ include file="navbar.jsp" %>

<div class="content my-reservation">
    <div class="search-results w-100">
        <h3 class="search-data"><spring:message code="yourclassRequests" htmlEscape="true"/></h3>
        <c:if test="${reservations.size() == 0}">
            <h1><spring:message code="no.classRequests"/></h1>
        </c:if>
        <c:forEach var="reservation" items="${reservations}">
            <div class="reservation-course-result">
                <div class="search-result-img">
                    <c:choose>
                        <c:when test="${reservation.status == 2}">
                            <div class="button-2 relative" type="submit">
                                <a class="class-button" href="<c:url value="/approveClassRequest?classReservation=${reservation.classRequestId}" />"></a>
                                <spring:message code="Approve"/>
                            </div>
                            <div class="button-2 relative" type="submit">
                                <a class="class-button" href="<c:url value="/denyClassRequest?classReservation=${reservation.classRequestId}" />"></a>
                                <spring:message code="Deny"/>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div class="button-2 relative" type="submit">
                                <a class="class-button" href="<c:url value="/courseFiles?professor=${reservation.course.professor.id}&subject=${reservation.course.subject.id}" />"></a>
                                <spring:message code="files"/>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                    <%--TODO: add buttons--%>
                <a class="search-result-title">
                    <c:out value="${reservation.course.subject.area.name} - ${reservation.course.subject.name}" escapeXml="true" /></a>
                <a class="search-result-professor" >
                    <spring:message code="reservation.student" arguments="${reservation.student.name}" htmlEscape="true" /></a>
                <a class="search-result-specs"><spring:message code="course.specs" arguments="${reservation.course.price}" htmlEscape="true" /></a>
                <a class="search-result-description"><spring:message code="reservation.day" arguments="${reservation.startDay},${reservation.startMonth},${reservation.startYear}" htmlEscape="true" /><br/><spring:message code="reservation.from" arguments="${reservation.startHour},${reservation.startMinutes}" htmlEscape="true" /><spring:message code="reservation.to"  arguments="${reservation.endHour},${reservation.endMinutes}" htmlEscape="true" /></a>
                <a class="search-result-status">
                    <c:choose>
                        <c:when test="${reservation.status == 0}">
                            <spring:message code="reservation.accepted" htmlEscape="true" />
                        </c:when>
                        <c:when test="${reservation.status == 1}">
                            <spring:message code="reservation.canceled" htmlEscape="true" />
                        </c:when>
                        <c:otherwise>
                            <spring:message code="reservation.pending" htmlEscape="true" />
                        </c:otherwise>
                    </c:choose>
                </a>
            </div>
        </c:forEach>

        <div class="paged-result-buttons">
            <c:url value="/reservations?page=${page - 1}" var="previous"/>
            <c:url value="/reservations?page=${page + 1}" var="next"/>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>
    </div>
</div>
</body>

</html>