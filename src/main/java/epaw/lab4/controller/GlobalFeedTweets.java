package epaw.lab4.controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import epaw.lab4.model.*;
import epaw.lab4.service.TweetService;

@WebServlet("/GlobalFeedTweets")
public class GlobalFeedTweets extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            // Pedimos los tweets globales
            List<Tweet> tweets = TweetService.getInstance().getGlobalTweets(user.getId(), 0, 50);
            request.setAttribute("tweets", tweets);
        }
        // Reutilizamos la misma vista que usa el feed normal
        request.getRequestDispatcher("FeedTweets.jsp").forward(request, response);
    }
}