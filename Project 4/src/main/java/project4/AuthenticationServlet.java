package project4;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/// @WebServlet("/authenticate")
public class AuthenticationServlet extends HttpServlet {
    private String dbUrl;
    private String dbUser;
    private String dbPassword;

    @Override
    public void init() throws ServletException {
        try {
            // Load system-level database credentials
            String propertiesPath = getServletContext().getRealPath("/WEB-INF/systemapp.properties");
            Properties props = new Properties();
            props.load(new FileInputStream(propertiesPath));

            dbUrl = props.getProperty("url");
            dbUser = props.getProperty("user");
            dbPassword = props.getProperty("password");

            // Load JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            throw new ServletException("Error loading database properties or driver", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement pstmt = conn.prepareStatement("SELECT login_username FROM usercredentials WHERE login_username = ? AND login_password = ?")) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // User authenticated, determine redirect based on username
                    switch (username) {
                        case "root":
                            response.sendRedirect("rootHome.jsp");
                            break;
                        case "client":
                            response.sendRedirect("clientHome.jsp");
                            break;
                        case "theaccountant":
                            response.sendRedirect("accountantHome.jsp");
                            break;
                    }
                } else {
                    // Authentication failed
                    request.setAttribute("errorMessage", "Invalid username or password.");
                    request.getRequestDispatcher("/static/error.html").forward(request, response);
                }
            }
        } catch (Exception e) {
            throw new ServletException("Authentication failed.", e);
        }
    }
}
