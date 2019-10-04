<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/conversations.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <title>Tu Teoria | <spring:message code="conversations.title"/></title>
</head>

<body class="conversations">

<%@ include file="navbar.jsp" %>

<div class="content">
    <div class="inbox-message">
        <hr>
        <p class="inbox-title"><spring:message code="conversations.inbox"/></p>
        <hr>
    </div>
    <c:if test="${conversations.results.size() == 0}" >
        <h1><spring:message code="no.conversations"/></h1>
        <p class="help-message"><spring:message code="conversations.help"/></p>
    </c:if>
    <c:forEach var="conversation" items="${conversations.results}">
        <div class="chat">
            <a class="conversation-link" href = "<c:url value="/Conversation?id=${conversation.id}" />"></a>
            <a class="conversation-subject-name"><c:out value="${conversation.subject.name}" escapeXml="true"/></a>
            <h2 class="conversation-participants">
                <c:out value="${conversation.professor.name} - ${conversation.user.name}" escapeXml="true"/>
            </h2>
            <h6 class="conversation-last-time">
                <spring:message code="time" arguments="${conversation.day},${conversation.month},${conversation.year},${conversation.hours},${conversation.minutes}"/>
            </h6>
        </div>
    </c:forEach>
    <div class="paged-result-buttons">
        <c:url value="/Conversations?page=${page - 1}" var="previous"/>
        <c:url value="/Conversations?page=${page + 1}" var="next"/>

        <c:if test="${page > 1}">
            <a href="${previous}" class="previous round">&#8249;</a>
        </c:if>
        <c:if test="${conversations.hasNext}">
            <a href="${next}" class="next round">&#8250;</a>
        </c:if>
    </div>
</div>
</body>

</html>