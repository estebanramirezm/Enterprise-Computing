package project4;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class ClientServlet extends HttpServlet 
{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sqlCommand = request.getParameter("sqlCommand");
        StringBuilder resultHtml = new StringBuilder();
        String message = "";

        // Only allow SELECT commands for clients
        if (!sqlCommand.trim().toLowerCase().startsWith("select")) {
            message = "Clients can only execute SELECT queries.";
        	if(sqlCommand.trim().equals(""))
        	{
                message = "Error executing command: Can not issue empty query";
        	}
            request.setAttribute("message", message);
            request.setAttribute("resultHtml", resultHtml.toString());
            request.getRequestDispatcher("/clientHome.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "client", "client");
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery(sqlCommand);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Generate HTML table for results
            resultHtml.append("<table>");
            resultHtml.append("<tr>");
            for (int i = 1; i <= columnCount; i++) {
                resultHtml.append("<th>").append(metaData.getColumnName(i)).append("</th>");
            }
            resultHtml.append("</tr>");

            while (rs.next()) {
                resultHtml.append("<tr>");
                for (int i = 1; i <= columnCount; i++) {
                    resultHtml.append("<td>").append(rs.getString(i)).append("</td>");
                }
                resultHtml.append("</tr>");
            }
            resultHtml.append("</table>");

        } catch (SQLException e) {
            message = "Error executing command: " + e.getMessage();
        }

        // Forward results and messages back to the JSP
        request.setAttribute("message", message);
        request.setAttribute("resultHtml", resultHtml.toString());
        request.getRequestDispatcher("/clientHome.jsp").forward(request, response);
    }
}
