<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/course.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/conversations.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/schedule.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="course.title"/></title>
</head>

<body class="staticClass">

<%@ include file="navbar.jsp" %>

<div class="content course">
    <div class="class-profile">
        <h1 class="title center-text"><c:out value="${course.subject.area.name} - ${course.subject.name}" escapeXml="true"/></h1>
        <h4 class="description center-text"><c:out value="${course.description}" escapeXml="true"/></h4>
        <div class="profile round-background">
            <a class="profile-picture" href="<c:url value="/Professor/${course.professor.id}"/>">
                <img class="profile-picture" alt="Profile picture" src="<c:url value="data:image/jpeg;base64,${course.professor.picture}" />"/>
            </a>
            <div class="profile-name"><c:out value="${course.professor.name}" escapeXml="true"/> <c:out value = "${course.professor.lastname}" escapeXml="true"/></div>
            <div class="profile-description"><c:out value="${course.professor.description}" escapeXml="true"/></div>
        </div>
    </div>
    <div class="contact">
        <div class="button-container">
            <h2 class="label"><spring:message code="course.contact"/></h2>
        </div>
    <c:url value="/sendMessage" var="postPath"/>
    <form:form cssClass="form" modelAttribute="messageForm" action="${postPath}" method="post">
        <form:hidden path="professorId" />
        <form:hidden path="subjectId" />
        <div>
            <form:label cssClass="label" path="body"><spring:message code="contact.body"/></form:label>
            <form:textarea cssClass="input-request chat-box" type="text" path="body" rows="5" cols="5"/>
            <form:errors cssClass="error-text" path="body" element="p"/>
        </div>
        <div>
            <form:hidden path="extraMessage"/>
            <form:errors cssClass="success-text no-margin" path="extraMessage" element="p"/>
        </div>
        <div class="button-container">
            <input class="button-2 no-margin" type="submit" value="<spring:message code="send"/>"/>
        </div>
    </form:form>
    </div>

    <div class="comment">
        <div class="button-container">
            <h2 class="label"><spring:message code="course.comment"/></h2>
        </div>
        <c:url value="/postComment" var="postPath"/>
            <form:form cssClass="form" modelAttribute="commentForm" action="${postPath}" method="post">
            <form:hidden path="commentProfessorId" />
            <form:hidden path="commentSubjectId" />
            <div>
                <form:label cssClass="label" path="commentBody"><spring:message code="comment.body"/></form:label>
                <form:textarea cssClass="input-request chat-box" type="text" path="commentBody" rows="5" cols="5"/>
                <form:errors cssClass="error-text" path="commentBody" element="p"/>
            </div>
            <div>
                <form:label cssClass="label" path="rating"><spring:message code="comment.rating"/></form:label>
                <form:input cssClass="input-request" type="number" step="1" min="1" max="5" path="rating"/>
                <form:errors cssClass="error-text" path="rating" element="p"/>
            </div>

            <c:choose>
                <c:when test="${canComment == false}">
                    <c:set var="disabled" value="disabled" />
                </c:when>
                <c:otherwise>
                    <c:set var="disabled" value="" />
                </c:otherwise>
            </c:choose>

            <div class="button-container">
                <input class="button-2" type="submit" ${disabled} value="<spring:message code="send"/>"/>
            </div>
        </form:form>
    </div>

    <div class="comments">

        <div>
            <h2><spring:message code="commentTitle"/></h2>
        </div>
        <c:forEach var="comment" items="${comments.results}">
            <div class="message message-comment">
                <h5 class="message-text"><c:out value="${comment.user.username}: ${comment.comment}" escapeXml="true"/></h5>
                <h6 class="conversation-last-time message-comment-time">
                    <spring:message code="time.sent" arguments="${comment.day},${comment.month},${comment.year},${comment.hours},${comment.minutes}"/>
                </h6>
            </div>
        </c:forEach>

        <div class="paged-result-buttons">
            <c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}&page=${page - 1}" var="previous"/>
            <c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}&page=${page + 1}" var="next"/>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${comments.hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>
    </div>

    <div class="schedule w-92">
        <h2><spring:message code="schedule.title"/></h2>
        <c:if test="${currentUser == null || (currentUser != null && currentUser.id != course.professor.id)}">
            <input  class="button-2" type="button" value="<spring:message code="reservations.action"/>" onclick="location.href='<c:url value="/reserveClass?professor=${course.professor.id}&subject=${course.subject.id}" />'"/>
        </c:if>
        <p><spring:message code="schedule.description" /></p>
        <%@ include file="schedule.jsp"%>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>