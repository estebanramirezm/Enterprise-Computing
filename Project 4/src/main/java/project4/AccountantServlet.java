package project4;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AccountantServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String report = request.getParameter("report"); // Selected report from dropdown

        if (report == null || report.isEmpty()) {
            request.setAttribute("message", "Please select a report to generate.");
            request.setAttribute("resultHtml", "");
            request.getRequestDispatcher("/accountantHome.jsp").forward(request, response);
            return;
        }

        StringBuilder resultHtml = new StringBuilder(); // To store query results
        String message = "";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "theaccountant", "theaccountant");
             CallableStatement cstmt = conn.prepareCall("{call " + report + "}")) {

            boolean hasResultSet = cstmt.execute();

            if (hasResultSet) {
                ResultSet rs = cstmt.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Build result table
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
                rs.close();
            } else {
                int updateCount = cstmt.getUpdateCount();
                message = "Procedure executed successfully. Rows affected: " + updateCount;
            }
        } catch (SQLException e) {
            message = "Error executing report: " + e.getMessage();
        }

        // Forward results to JSP
        request.setAttribute("resultHtml", resultHtml.toString());
        request.setAttribute("message", message);
        request.getRequestDispatcher("/accountantHome.jsp").forward(request, response);
    }
}
