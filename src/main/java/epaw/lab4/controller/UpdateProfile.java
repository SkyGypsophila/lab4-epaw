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
import java.util.Map;

@WebServlet("/UpdateProfile")
public class UpdateProfile extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        User loggedUser = (User) session.getAttribute("user");
        String name = request.getParameter("name");
        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");
        String favoriteGame = request.getParameter("favoriteGame");

        // Temporary User for validation
        User tempUser = new User();
        tempUser.setId(loggedUser.getId());
        tempUser.setName(name);
        tempUser.setNickname(nickname);
        tempUser.setEmail(email);
        tempUser.setFavoriteGame(favoriteGame);

        Map<String, String> errors = userService.validateProfileUpdate(tempUser);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (errors.isEmpty()) {
            // Update loggedUser fields
            loggedUser.setName(name);
            loggedUser.setNickname(nickname);
            loggedUser.setEmail(email);
            loggedUser.setFavoriteGame(favoriteGame);
            
            // Persist to database
            userService.updateUser(loggedUser);
            
            // Sync with session
            session.setAttribute("user", loggedUser);
            
            response.getWriter().write("{\"success\":true}");
        } else {
            StringBuilder json = new StringBuilder();
            json.append("{\"success\":false,\"errors\":{");
            boolean first = true;
            for (Map.Entry<String, String> entry : errors.entrySet()) {
                if (!first) json.append(",");
                json.append("\"").append(entry.getKey()).append("\":\"").append(entry.getValue().replace("\"", "\\\"")).append("\"");
                first = false;
            }
            json.append("}}");
            response.getWriter().write(json.toString());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
