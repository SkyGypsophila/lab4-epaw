package epaw.lab4.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import epaw.lab4.model.User;

import java.io.IOException;

@WebServlet("/Menu")
public class Menu extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		String view = "MenuNotLogged.html";

		if (session != null && session.getAttribute("user") != null) {
			User loggedUser = (User) session.getAttribute("user");

			if (loggedUser.isBanned()) {
				view = "MenuBanned.html";
			} else if (loggedUser.getRole() != null && loggedUser.getRole().equalsIgnoreCase("ADMINISTRATOR")) {
				view = "MenuAdmin.html";
			} else {
				view = "MenuLogged.html";
			}
		}

		request.getRequestDispatcher(view).forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
