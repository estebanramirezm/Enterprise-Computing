package project4;

import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class RootServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sqlCommand = request.getParameter("sqlCommand"); // SQL command from the form
        Connection conn = null;
        Statement stmt = null;
        StringBuilder resultHtml = new StringBuilder(); // To store the result in HTML format
        String message = ""; // To store error or success messages

        try {
            // Load JDBC driver and establish connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/project4", "root", "rootMAC1$");

            stmt = conn.createStatement();

            // Execute SQL command and handle results
            if (sqlCommand.toLowerCase().startsWith("select")) {
                ResultSet rs = stmt.executeQuery(sqlCommand);
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Build table for results
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
                int rowsAffected = stmt.executeUpdate(sqlCommand);
                message = "Command executed successfully. Rows affected: " + rowsAffected;
                // Trigger business logic if modifying shipments
                if (sqlCommand.toLowerCase().contains("insert into shipments") ||
                    sqlCommand.toLowerCase().contains("update shipments")) 
                {
                    int rowsUpdated = applyBusinessLogic(conn);
                    if (rowsUpdated > 0) {
                        message += ". Business logic triggered: Status updated for " + rowsUpdated + " supplier(s).";
                    } else {
                        message += ". Business logic triggered. No updates.";
                    }
                }
                else {
                    message += ". Business logic not triggered.";
                }
            }
        } catch (SQLException e) {
            message = "Error executing command: " + e.getMessage();
        } catch (ClassNotFoundException e) {
            message = "Database driver not found.";
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                message += " Error closing resources: " + e.getMessage();
            }
        }

        // Add results and message to request attributes
        request.setAttribute("sqlCommand", sqlCommand);
        request.setAttribute("resultHtml", resultHtml.toString());
        request.setAttribute("message", message);

        // Forward the request back to the JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("rootHome.jsp");
        dispatcher.forward(request, response);
    }

    private int applyBusinessLogic(Connection conn) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement(
                "UPDATE suppliers " +
                "SET status = status + 5 " +
                "WHERE snum IN (" +
                "   SELECT snum " +
                "   FROM shipments " +
                "   WHERE quantity >= 100" +
                ")")) {
            return pstmt.executeUpdate(); // Returns the number of suppliers updated
        }
    }
}
