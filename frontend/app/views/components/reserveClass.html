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
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/schedule.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/fromToRestriction.js" />"></script>
    <title>Tu Teoria | <spring:message code="class.reserve"/></title>

    <link href="<c:url value="/resources/css/select2.min.css" />" rel="stylesheet" />
    <script src="<c:url value="/resources/js/select2.min.js" />"></script>
    <script src="<c:url value="/resources/js/courseForm.js" />"></script>

</head>

<body class="register">

<%@ include file="navbar.jsp" %>

<div class="content reserve-class">
    <div class="button-container">
        <h2 class="label"><spring:message code="class.reserve" /></h2>
    </div>
    <c:url value="/reserveClass?professor=${param.professor}&subject=${param.subject}" var="postPath"/>
    <form:form cssClass="form round-background" modelAttribute="classReservationForm" action="${postPath}" method="post">
        <div>
            <form:label cssClass="label" path="day"><spring:message code="classReservation.dayHeader"/></form:label>
            <form:input path="day" cssClass="input-request" type="date" min="2000-1-1"/>
            <form:errors cssClass="error-text" path="day" element="p"/>
        </div>
        <div class="responsiveRow">
            <h3><spring:message code="classReservation.hourHeader"/></h3>
            <div class="row">
                <div class="m-10-b rm-10-b">
                    <form:select cssClass="select-subject no-border b-r-5 no-margin m-r-5 filter-input" path="startHour" >
                        <form:option selected="selected" disabled="true" value=""><spring:message code="from"/></form:option>
                        <c:forEach var="hour" begin="1" end="23" >
                            <form:option value="${hour}">${hour}:00</form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors cssClass="error-text" path="startHour" element="p"/>
                </div>
                -
                <div class="m-l-5">
                    <form:select cssClass="select-subject no-border b-r-5 no-margin filter-input" path="endHour">
                        <form:option selected="selected" disabled="true" value=""><spring:message code="until"/></form:option>
                        <c:forEach var="hour" begin="2" end="24" >
                            <form:option value="${hour}">${hour}:00</form:option>
                        </c:forEach>
                    </form:select>
                    <form:errors cssClass="error-text" path="endHour" element="p"/>
                </div>
            </div>
        </div>
        <div class="button-container">
            <input class="button-2" type="submit" value="<spring:message code="reserve"/>"/>
        </div>
    </form:form>
    <div class="schedule round-background">
        <h2><spring:message code="schedule.title"/></h2>
        <p><spring:message code="schedule.description" /></p>
        <%@ include file="schedule.jsp"%>
    </div>
</div>
<div class="footer">
</div>
</body>

</html>