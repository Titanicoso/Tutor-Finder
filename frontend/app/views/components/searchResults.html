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
    <link rel="stylesheet" href="<c:url value="/resources/css/course.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/searchForm.js" />"></script>
    <script src="<c:url value="/resources/js/fromToRestriction.js" />"></script>

    <title>Tu Teoria | <spring:message code="search.results" /> </title>
</head>

<body class="body">
<c:url value="/searchResults" var="postPath"/>
<form:form cssClass="staticSearchResults" modelAttribute="searchForm" action="${postPath}" method="post">
<div class="navbar">
    <a href="<c:url value="/" />" class="logo-box">
        <img alt="Tu Teoria" class="logo" src="<c:url value="/resources/images/logo_invert.jpg" />" />
    </a>

    <div class="search-bar">
        <spring:message code="search" var="searchPlaceholder"/>
        <form:input class="search-input" type="search" path="search" name="search" placeholder="${searchPlaceholder}"/>
        <div class="search-dropdown">
            <form:select class="select-search-type" path="type" name="type">
                <form:option value="" selected="true" disabled="true"><spring:message code="search.category" /> </form:option>
                <form:option value="professor"><spring:message code="professor" /> </form:option>
                <form:option value="course"><spring:message code="course.title" /> </form:option>
                <form:option value="area"><spring:message code="area" /></form:option>
            </form:select>
        </div>
        <button type="submit" class="search-button">
            <img class="search-img" src="<c:url value="/resources/images/search.png" />" />
        </button>
    </div>

    <div class="navbar-buttons">
        <c:choose>
            <c:when test="${currentUser != null}">
                <div class="dropdown navbar-button" id="dropdown">
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
    <div class="filter-panel">
        <h2 class="text-center-responsive"><spring:message code="filter" /> </h2>
        <div class="responsiveColumn">
            <div class="responsiveRow">
                <h3><spring:message code="search.dayHeader"/></h3>
                <div>
                    <form:select cssClass="js-example-basic-multiple select-subject no-border b-r-5 no-margin filter-input m-w-200" path="days" multiple="multiple">
                        <form:option value="1"><spring:message code ="day.monday"/></form:option>
                        <form:option value="2"><spring:message code ="day.tuesday"/></form:option>
                        <form:option value="3"><spring:message code ="day.wednesday"/></form:option>
                        <form:option value="4"><spring:message code ="day.thursday"/></form:option>
                        <form:option value="5"><spring:message code ="day.friday"/></form:option>
                        <form:option value="6"><spring:message code ="day.saturday"/></form:option>
                        <form:option value="7"><spring:message code ="day.sunday"/></form:option>
                    </form:select>
                    <form:errors cssClass="error-text" path="days" element="p"/>
                </div>
            </div>
            <div class="responsiveRow">
                <h3><spring:message code="search.hourHeader"/></h3>
                <div class="row">
                    <div class="m-10-b rm-10-b">
                        <form:select cssClass="select-subject no-border b-r-5 no-margin m-r-5 filter-input" path="startHour" >
                            <form:option selected="selected" disabled="true" value=""><spring:message code ="from"/></form:option>
                            <c:forEach var="hour" begin="1" end="23" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="startHour" element="p"/>
                    </div>
                    -
                    <div class="m-l-5">
                        <form:select cssClass="select-subject no-border b-r-5 no-margin filter-input" path="endHour">
                            <form:option selected="selected" disabled="true" value=""><spring:message code ="until"/></form:option>
                            <c:forEach var="hour" begin="2" end="24" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="endHour" element="p"/>
                    </div>
                </div>
            </div>
        </div>
        <div class="responsiveRow">
            <h3 class="r-filter-title"><spring:message code="search.priceHeader"/></h3>
            <div class="row">
                <div class="m-10-b rm-10-b m-w-80">
                    <form:input cssClass="input-request-filter first-input no-border b-r-5 m-r-5 filter-input" type="number"  min="1" step="0.01" path="minPrice" placeholder="Minimo"/>
                    <form:errors cssClass="error-text" path="minPrice" element="p"/>
                </div>
                -
                <div class="m-w-80 m-l-5">
                    <form:input cssClass="input-request-filter no-border b-r-5 filter-input" type="number" min="1" step="0.01" path="maxPrice" placeholder="Máximo"/>
                    <form:errors cssClass="error-text" path="maxPrice" element="p"/>
                </div>
            </div>
        </div>
        <button class="button-2 center" type="submit" class="search-button">
            <spring:message code="button.filter"/>
        </button>
    </div>
    <div class="search-results">
        <c:if test="${search.length() >= 1 }"><h3 class="search-data"><spring:message code="search.message" arguments="${search}" htmlEscape="true"/></h3></c:if>
        <c:choose>
            <c:when test="${pagedResults.results.size() == 0}">
                <h1><spring:message code="no.results"/></h1>
            </c:when>
            <c:when test="${type == 'professor'}">
                <%@ include file="professorSearch.jsp" %>
            </c:when>
            <c:when test="${type == 'course'}">
                <%@ include file="courseSearch.jsp" %>
            </c:when>
            <c:when test="${type == 'area'}">
                <%@ include file="areaSearch.jsp" %>
            </c:when>
        </c:choose>

        <div class="paged-result-buttons">
            <c:url value ="/searchResults" var="previous">
                <c:forEach items="${searchForm.days}" var="day">
                    <c:param name="days"><c:out value="${day}"/></c:param>
                </c:forEach>
                <c:param name="page">${page - 1}</c:param>
                <c:param name="search">${param.search}</c:param>
                <c:param name="type">${type}</c:param>
                <c:param name="minPrice">${param.minPrice}</c:param>
                <c:param name="maxPrice">${param.maxPrice}</c:param>
                <c:param name="startHour">${param.startHour}</c:param>
                <c:param name="endHour">${param.endHour}</c:param>
            </c:url>

            <c:url value ="/searchResults" var="next">
                <c:forEach items="${searchForm.days}" var="day">
                    <c:param name="days"><c:out value="${day}"/></c:param>
                </c:forEach>
                <c:param name="page">${page + 1}</c:param>
                <c:param name="search">${param.search}</c:param>
                <c:param name="type">${type}</c:param>
                <c:param name="minPrice">${param.minPrice}</c:param>
                <c:param name="maxPrice">${param.maxPrice}</c:param>
                <c:param name="startHour">${param.startHour}</c:param>
                <c:param name="endHour">${param.endHour}</c:param>
            </c:url>

            <c:if test="${page > 1}">
                <a href="${previous}" class="previous round">&#8249;</a>
            </c:if>
            <c:if test="${pagedResults.hasNext}">
                <a href="${next}" class="next round">&#8250;</a>
            </c:if>
        </div>
    </div>
</div>
</form:form>
</body>

</html>