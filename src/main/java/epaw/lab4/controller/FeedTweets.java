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

@WebServlet("/FeedTweets")
public class FeedTweets extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		List<Tweet> tweets = null;
		HttpSession session = request.getSession(false);

		if (session != null) {
			User user = (User) session.getAttribute("user");
			if (user != null) {
				TweetService tweetService = TweetService.getInstance();
				tweets = tweetService.getFeedTweets(user.getId(), 0, 20);
			}
		}

		request.setAttribute("tweets", tweets);
		request.getRequestDispatcher("FeedTweets.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
