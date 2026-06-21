package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import epaw.lab4.model.Tweet;
import epaw.lab4.model.User;
import epaw.lab4.service.TweetService;
import java.io.IOException;

@WebServlet("/EditTweet")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class EditTweet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                try {
                    String idStr = request.getParameter("id");
                    String content = request.getParameter("content");
                    if (idStr != null && content != null) {
                        int tweetId = Integer.parseInt(idStr);
                        TweetService tweetService = TweetService.getInstance();
                        Tweet tweet = tweetService.getTweetById(tweetId);
                        
                        if (tweet != null) {
                            boolean isAdmin = user.getRole() != null && user.getRole().equalsIgnoreCase("ADMINISTRATOR");
                            if (isAdmin || tweet.getUid() == user.getId()) {
                                Part filePart = null;
                                try { filePart = request.getPart("tweetImage"); } catch (Exception ignored) {}
                                boolean removeImage = "true".equals(request.getParameter("removeImage"));
                                String imgDir = getServletContext().getRealPath("/img/tweets/");
                                tweetService.update(tweetId, content, filePart, imgDir, removeImage);
                            } else {
                                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                                return;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
