package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;
import java.io.IOException;

@WebServlet("/RemoveTweetImage")
public class RemoveTweetImage extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) { response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); return; }
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMINISTRATOR".equalsIgnoreCase(user.getRole())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        String idStr = request.getParameter("id");
        if (idStr == null) { response.setStatus(HttpServletResponse.SC_BAD_REQUEST); return; }
        try {
            int tweetId = Integer.parseInt(idStr);
            String webappRoot = getServletContext().getRealPath("/");
            TweetService.getInstance().removeTweetImage(tweetId, webappRoot);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
