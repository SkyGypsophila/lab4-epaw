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
import java.util.List;

@WebServlet("/GestioUsuaris")
public class GestioUsuaris extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            User admin = (User) session.getAttribute("user");
            if (admin != null && admin.getRole() != null && admin.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
                UserService userService = UserService.getInstance();
                List<User> users = userService.getAllUsersWithBanStatus(admin.getId());
                request.setAttribute("users", users);
                request.getRequestDispatcher("GestioUsuaris.jsp").forward(request, response);
                return;
            }
        }

        // Not admin — redirect to content
        request.getRequestDispatcher("Content").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
