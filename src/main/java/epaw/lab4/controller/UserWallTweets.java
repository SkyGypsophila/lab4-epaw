package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;
import java.io.IOException;
import java.util.List;

@WebServlet("/UserWallTweets")
public class UserWallTweets extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            try {
                int targetUserId = Integer.parseInt(idParam);
                HttpSession session = request.getSession(false);
                Integer viewerId = -1;
                if (session != null && session.getAttribute("user") != null) {
                    User user = (User) session.getAttribute("user");
                    viewerId = user.getId();
                }
                
                TweetService tweetService = TweetService.getInstance();
                List<Tweet> tweets = tweetService.getTweetsByUser(targetUserId, viewerId, 0, 50);
                request.setAttribute("tweets", tweets);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("FeedTweets.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
