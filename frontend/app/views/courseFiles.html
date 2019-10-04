<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="<c:url value = "/resources/css/fonts.css" />" rel='stylesheet'>

    <link rel="stylesheet" href="<c:url value="/resources/css/course.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/navbar.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/search.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/responsive.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/conversations.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/schedule.css" />">
    <link rel="stylesheet" href="<c:url value="/resources/css/stylesheet.css" />">
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/courseFile.js" />"></script>
    <title>Tu Teoria | <spring:message code="course.files.title"/></title>
</head>

<body class="body">
<%@ include file="navbar.jsp" %>

<div class="content my-reservation">
    <div class="search-results w-98 rm-10-b m-t-20">
        <h3 class="search-data"><spring:message code="contents" htmlEscape="true"/></h3>
        <c:if test="${courseFiles.size() == 0}">
            <h1><spring:message code="no.contents"/></h1>
        </c:if>
        <c:forEach var="file" items="${courseFiles}">
            <div class="reservation-course-result">
                <div class="search-result-img">
                    <div class="button-2 relative" type="submit">
                        <a class="class-button" href="<c:url value="/downloadFile?courseFile=${file.id}" />"></a>
                        <spring:message code="download"/>
                    </div>
                    <c:if test="${param.professor == currentUser.id}">
                        <div class="button-2 relative" type="submit">
                            <a class="class-button" href="<c:url value="/deleteFile?courseFile=${file.id}&professor=${param.professor}&subject=${param.subject}" />"></a>
                            <spring:message code="delete"/>
                        </div>
                    </c:if>
                </div>
                <a class="search-result-title">
                    <c:out value="${file.name}" escapeXml="true" /></a>
                <a class="search-result-professor" ></a>
                <a class="search-result-specs"></a>
                <a class="search-result-description"><spring:message code="description"/><c:out value="${file.description}" escapeXml="true" /></a>
                <a class="search-result-status"></a>

            </div>
        </c:forEach>
    </div>
    <c:if test="${param.professor == currentUser.id}">
        <div class="comment w-100">
            <div class="button-container">
                <h2 class="label"><spring:message code="course.fileUploadTitle"/></h2>
            </div>
            <c:url value="/uploadFile?professor=${param.professor}&subject=${param.subject}" var="postPath"/>
            <form:form method="POST" modelAttribute="uploadClassFileForm" enctype="multipart/form-data" class="form-horizontal" action="${postPath}">
                <div>
                    <form:label cssClass="label" path="file"><spring:message code="course.fileUpload"/></form:label>
                    <form:input type="file" path="file" id="file" class="input-request"/>
                    <form:errors cssClass="error-text" path="file" element="p"/>
                    <p id="fileError" class="error-text"><spring:message code="FileSize.uploadClassFileForm.file" /></p>
                </div>
                <div>
                    <form:label cssClass="label" path="description"><spring:message code="course.description"/></form:label>
                    <form:textarea type="text" path="description" id="description" class="input-request chat-box" rows="5" cols="5"/>
                    <form:errors cssClass="error-text" path="description" element="p"/>
                </div>
                <div class="button-container">
                    <input class="button-2" type="submit" id="uploadSubmit" value="<spring:message code="upload"/>"/>
                </div>
            </form:form>
        </div>
    </c:if>

</div>
</div>
</body>
</html>
