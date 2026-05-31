package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.UserService;

import java.io.IOException;

@WebServlet("/BanUser")
public class BanUser extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User admin = (User) session.getAttribute("user");
            if (admin != null && admin.getRole() != null && admin.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
                try {
                    int bannedUserId = Integer.parseInt(request.getParameter("id"));
                    String reason = request.getParameter("reason");
                    if (reason == null || reason.trim().isEmpty()) {
                        reason = "Banned by administrator";
                    }

                    UserService userService = UserService.getInstance();
                    userService.banUser(bannedUserId, admin.getId(), reason);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
