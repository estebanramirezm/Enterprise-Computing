<html>
<head>
    <title>Accountant Home</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/styles.css">
    <style>
	    select, textarea, input[type="text"], input[type="password"]
	    {
	        margin-bottom: 5%;
	    }
    </style>
    <script>
	    // Resets the dropdown to the default option
	    function resetForm() {
	        document.getElementById("report").selectedIndex = 0;
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
    <h3 style="text-align: center;"> Welcome, <span style="color: darkseagreen;">theaccountant</span>! </h3>
	<form method="post" action="${pageContext.request.contextPath}/accountantServlet" id="accountantForm">
	    <label for="report">Select a report to run:</label>
	    <select name="report" id="report">
	        <option value="" disabled selected>Select a report</option>
	        <option value="GetMaxStatus">Get The Maximum Status Value Of All Suppliers</option>
	        <option value="GetTotalWeight">Get The Total Weight Of All Parts</option>
	        <option value="GetTotalShipments">Get The Total Number of Shipments</option>
	        <option value="GetJobWithMostWorkers">Get The Name And Number Of Workers Of The Job With The Most Workers</option>
	        <option value="ListSuppliersWithStatus">List The Name And Status Of Every Supplier</option>
	    </select>
	    <button type="submit">Generate Report</button>
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
