<%@ taglib prefix="c" uri ="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous">
    <title>Tu Teoria | <spring:message code="profile.title" /> </title>
    <script src="<c:url value="/resources/js/jquery-3.3.1.min.js" />"></script>
    <script src="<c:url value="/resources/js/dropdownClick.js" />"></script>
    <script src="<c:url value="/resources/js/fromToRestriction.js" />"></script>
    <script src="<c:url value="/resources/js/scheduleModal.js" />"></script>
</head>

<body class="staticProfile">

<%@ include file="navbar.jsp" %>

<div class="content">
    <%--<img class="profile-picture" alt="Profile picture" src="<c:url value="/resources/images/logo_invert.jpg" />" />--%>
    <div class="profile-profesor">
        <img class="profile-picture" alt="Profile picture" src="<c:url value="data:image/jpeg;base64,${professor.picture}" />"/>
        <div class="profile-name row">
            <h1 class="m-0-10 fit-content"><c:out value="${professor.name} ${professor.lastname}" escapeXml="true"/></h1>
            <a class="fas fa-pen center profile-course-button" title="<spring:message code="course.modify"/>" href="<c:url value="/editProfessorProfile" />"></a>
        </div>
        <h3 class="profile-description m-0-10"><c:out value="${professor.description}" escapeXml="true"/></h3>
    </div>
    <div class="class-content">
        <div class="row r-center">
            <div class="add-class button-2 relative">
                <a class="class-button" href="<c:url value="/createCourse" />"></a>
                <spring:message code="addClass"/>
            </div>
            <div id="delete-class-modal-button" class="add-class button-2 relative">
                <spring:message code="deleteClass"/>
            </div>
        </div>
        <div id="delete-class-modal" class="add-time-modal">
            <div>
                <span class="modal-close">&times;</span>
                <h1 class="center-text"><spring:message code="deleteClass"/></h1>
                <c:url value="/deleteCourse" var="postPath"/>
                <form:form cssClass="form" modelAttribute="deleteCourseForm" action="${postPath}" method="post">
                    <div>
                        <form:label cssClass="label" path="subject"><spring:message code="course.subject"/></form:label>
                        <form:select cssClass="select-subject" path="subject">
                            <form:option selected="disabled" value=""><spring:message code="select.subject"/></form:option>
                            <c:forEach var="course" items="${subjects}">
                                <form:option value="${course.subject.id}"><c:out value="${course.subject.name}" escapeXml="true"/></form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="subject" element="p"/>
                    </div>
                    <div class="button-container">
                        <input class="button-2" type="submit" value="<spring:message code="deleteClass"/>"/>
                    </div>
                </form:form>
            </div>
        </div>
        <div class="classes">
            <div>
                <h2><spring:message code="clasesTitleFP"/></h2>
                <p><spring:message code="clasesDescriptionFP" /></p>
            </div>
            <c:forEach var="course" items="${courses.results}">
                <div class="class">
                    <a class="class-button" href="<c:url value="/Course/?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                    <h3 class="class-title"><c:out value="${course.subject.name}" escapeXml="true"/></h3>
                    <div class="class-description"><c:out value="${course.description}" escapeXml="true"/></div>
                    <div class="edit-class">
                        <a title="<spring:message code="course.modify"/>" class="fas fa-pen center profile-course-button" href="<c:url value="/modifyCourse/?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                        <a title="<spring:message code="files"/>" class="fas fa-file center profile-course-button" href="<c:url value="/courseFiles?professor=${course.professor.id}&subject=${course.subject.id}" />"></a>
                    </div>
                </div>
            </c:forEach>

            <div class="paged-result-buttons">
                <c:url value="/Profile?page=${page - 1}" var="previous"/>
                <c:url value="/Profile?page=${page + 1}" var="next"/>

                <c:if test="${page > 1}">
                    <a href="${previous}" class="previous round">&#8249;</a>
                </c:if>
                <c:if test="${courses.hasNext}">
                    <a href="${next}" class="next round">&#8250;</a>
                </c:if>
            </div>
        </div>
    </div>

    <div class="time-content">
        <div class="row r-center">
            <div id="add-time-modal-button" class="add-time-modal-button button-2">
                <spring:message code="addTime"/>
            </div>
            <div id="delete-time-modal-button" class="add-time-modal-button button-2">
                <spring:message code="deleteTime"/>
            </div>
        </div>
        <div id="add-time-modal" class="add-time-modal">
            <div>
                <span class="modal-close">&times;</span>
                <h1 class="center-text"><spring:message code="addTimeslot"/></h1>
                <c:url value="/CreateTimeSlot" var="postPath"/>
                <form:form cssClass="form" modelAttribute="addScheduleForm" action="${postPath}" method="post">
                    <div>
                        <form:label cssClass="label" path="day"><spring:message code="schedule.form.day"/></form:label>
                        <form:select cssClass="select-subject" path="day">
                            <form:option selected="selected" value=""><spring:message code="select.day"/></form:option>
                            <form:option value="1"><spring:message code ="day.monday"/></form:option>
                            <form:option value="2"><spring:message code ="day.tuesday"/></form:option>
                            <form:option value="3"><spring:message code ="day.wednesday"/></form:option>
                            <form:option value="4"><spring:message code ="day.thursday"/></form:option>
                            <form:option value="5"><spring:message code ="day.friday"/></form:option>
                            <form:option value="6"><spring:message code ="day.saturday"/></form:option>
                            <form:option value="7"><spring:message code ="day.sunday"/></form:option>
                        </form:select>
                        <form:errors cssClass="error-text" path="day" element="p"/>
                    </div>
                    <div>
                        <form:label cssClass="label" path="startHour"><spring:message code="schedule.form.startHour"/></form:label>
                        <form:select cssClass="select-subject" path="startHour">
                            <form:option selected="selected" value=""><spring:message code="select.startHour"/></form:option>
                            <c:forEach var="hour" begin="1" end="23" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="startHour" element="p"/>
                    </div>
                    <div>
                        <form:label cssClass="label" path="endHour"><spring:message code="schedule.form.endHour"/></form:label>
                        <form:select cssClass="select-subject" path="endHour">
                            <form:option selected="selected" value=""><spring:message code="select.endHour"/></form:option>
                            <c:forEach var="hour" begin="2" end="24" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="endHour" element="p"/>
                    </div>
                    <div class="button-container">
                        <input class="button-2" type="submit" value="<spring:message code="addTimeslot"/>"/>
                    </div>
                </form:form>
            </div>
        </div>

        <div id="delete-time-modal" class="add-time-modal">
            <div>
                <span class="modal-close">&times;</span>
                <h1 class="center-text"><spring:message code="deleteTimeSlot"/></h1>
                <c:url value="/RemoveTimeSlot" var="postPath"/>
                <form:form cssClass="form" modelAttribute="deleteScheduleForm" action="${postPath}" method="post">
                    <div>
                        <form:label cssClass="label" path="day"><spring:message code="schedule.form.day"/></form:label>
                        <form:select cssClass="select-subject" path="day">
                            <form:option selected="selected" value=""><spring:message code="select.day"/></form:option>
                            <form:option value="1"><spring:message code ="day.monday"/></form:option>
                            <form:option value="2"><spring:message code ="day.tuesday"/></form:option>
                            <form:option value="3"><spring:message code ="day.wednesday"/></form:option>
                            <form:option value="4"><spring:message code ="day.thursday"/></form:option>
                            <form:option value="5"><spring:message code ="day.friday"/></form:option>
                            <form:option value="6"><spring:message code ="day.saturday"/></form:option>
                            <form:option value="7"><spring:message code ="day.sunday"/></form:option>
                        </form:select>
                        <form:errors cssClass="error-text" path="day" element="p"/>
                    </div>
                    <div>
                        <form:label cssClass="label" path="startHour"><spring:message code="schedule.form.startHour"/></form:label>
                        <form:select cssClass="select-subject" path="startHour">
                            <form:option selected="selected" value=""><spring:message code="select.startHour"/></form:option>
                            <c:forEach var="hour" begin="1" end="23" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="startHour" element="p"/>
                    </div>
                    <div>
                        <form:label cssClass="label" path="endHour"><spring:message code="schedule.form.endHour"/></form:label>
                        <form:select cssClass="select-subject" path="endHour">
                            <form:option selected="selected" value=""><spring:message code="select.endHour"/></form:option>
                            <c:forEach var="hour" begin="2" end="24" >
                                <form:option value="${hour}">${hour}:00</form:option>
                            </c:forEach>
                        </form:select>
                        <form:errors cssClass="error-text" path="endHour" element="p"/>
                    </div>
                    <div class="button-container">
                        <input class="button-2" type="submit" value="<spring:message code="deleteTimeSlot"/>"/>
                    </div>
                </form:form>
            </div>
        </div>
        <div class="schedule">
            <h2><spring:message code="schedule.titleFP"/></h2>
            <p><spring:message code="schedule.descriptionFP" /></p>
            <%@ include file="schedule.jsp"%>
        </div>
    </div>


</div>
<div class="footer">
</div>

</body>

</html>