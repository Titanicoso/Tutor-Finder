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
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>
    <title>Tu Teoria | <spring:message code="index.title"/></title>
</head>

<body class="staticHome">
<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <div class="search-bar"></div>

    <div class="navbar-buttons">
        <c:choose>
            <c:when test="${currentUser != null}">
                <div class="navbar-button dropdown" id="dropdown">
                    <a class="dropdown-button" id="dropdown-button"><c:out value="${currentUser.name} " escapeXml="true"/></a>
                    <div class="dropdown-content" id="dropdown-content">
                        <c:choose>
                            <c:when test="${currentUserIsProfessor == true}">
                                <a href="<c:url value="/Profile" />" class="navbar-button"><spring:message code="profile.title"/></a>
                                <a href="<c:url value="/classRequests?page=1" />" class="navbar-button"><spring:message code="classes.title"/></a>
                                <!--<a>Modificar</a>-->
                            </c:when>
                            <c:otherwise>
                                <a href="<c:url value="/registerAsProfessor" />" class="navbar-button"><spring:message code="register.professor"/></a>
                            </c:otherwise>
                        </c:choose>
                        <a href="<c:url value="/reservations?page=1" />" class="navbar-button"><spring:message code="reservations.title"/></a>
                        <a href="<c:url value="/Conversations" />" class="navbar-button"><spring:message code="conversations.title"/></a>
                        <a href="<c:url value="/logout" />" class="navbar-button"><spring:message code="user.logout"/></a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <a href="<c:url value="/register" />" class="navbar-button"><spring:message code="register"/></a>
                <a href="<c:url value="/login" />" class="navbar-button"><spring:message code="login"/></a>
            </c:otherwise>
        </c:choose>
    </div>
</div>



<div class="content">
    <c:url value="/searchResults" var="postPath"/>
    <form:form modelAttribute="SearchForm" action="${postPath}" method="post">
    <div class="search-box">
        <img alt="Tu Teoria" class="search-logo" src="<c:url value="/resources/images/logo.png" />" />
        <div class="search-bar">
            <spring:message code="search" var="searchPlaceholder"/>
            <form:input class="search-input" type="search" path="search" placeholder="${searchPlaceholder}"/>
            <div class="search-dropdown">
                <form:select cssClass="select-searchbar-type" path="type" name="type">
                    <form:option value="" selected="true" disabled="true"><spring:message code="search.category" /> </form:option>
                    <form:option value="professor"><spring:message code="professor" /> </form:option>
                    <form:option value="course"><spring:message code="course.title" /> </form:option>
                    <form:option value="area"><spring:message code="area" /></form:option>
                </form:select>
            </div>
            <button type="submit" class="search-button">
                <img class="search-img" src="<c:url value="/resources/images/search.png"/>" />
            </button>
        </div>
    </div>
    </form:form>
</div>
<div class="footer">    
</div>
</body>

</html>