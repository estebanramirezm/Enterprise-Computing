<html>
<head>
    <title>Client Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css">
    <script>
	    // Clears the SQL command textarea
	    function resetForm() {
	        document.getElementById("sqlCommand").value = "";
	    }
	
	    // Clears the results section
	    function clearResults() {
	        const resultsSection = document.querySelector(".results div");
	        if (resultsSection) resultsSection.innerHTML = "";
	        const messageSection = document.querySelector(".results .message");
	        if (messageSection) messageSection.innerHTML = "";
	    }
	</script>
</head>
<body>
    <h1>Servlet/JSP-based Multi-tiered Enterprise Application Using A Tomcat Container</h1>
    <h3 style="text-align: center;"> Welcome, <span style="color: darkseagreen;">client</span>! </h3>
	<form method="post" action="${pageContext.request.contextPath}/clientServlet" id="clientForm">
	    <textarea name="sqlCommand" placeholder="Enter SQL command" id="sqlCommand"></textarea>
	    <button type="submit">Execute</button>
	    <button type="button" onclick="resetForm()">Reset Form</button>
	    <button type="button" onclick="clearResults()">Clear Results</button>
	</form>
	<a href="${pageContext.request.contextPath}/logout">Logout</a>

    <!-- Results Section -->
    <div class="results">
        <h2>Results</h2>
        <c:if test="${not empty message}">
            <p class="message">${message}</p>
        </c:if>
        <c:if test="${not empty resultHtml}">
            <div>${resultHtml}</div>
        </c:if>
    </div>
</body>
</html>
