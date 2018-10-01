<c:forEach var="result" items="${pagedResults.results}">
    <div class="search-area-result">
        <a class="conversation-link" href = "<c:url value="/Area/${result.id}"/>"/>
        <a class="search-result-img"><img class="search-result-picture" src="<c:url value="data:image/jpeg;base64,${result.image}"/>"/></a>
        <a class="search-result-title" >
            <c:out value="${result.name}" escapeXml="true" /></a>
        <a class="search-result-description">
            <c:out value="${result.description}" escapeXml="true" /></a>
    </div>
</c:forEach>
