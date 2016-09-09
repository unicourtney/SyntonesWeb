package com.blackparty.syntones.endpoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.model.UserTransaction;
import com.blackparty.syntones.response.SongListResponse;
import com.blackparty.syntones.service.UserService;

@RestController
@Component
public class UserEndpoint {
	@Autowired
	UserService userService;

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public User login(@RequestBody User user, HttpSession session, HttpServletRequest request) {
		System.out.println("Login request is received coming from " + user.getUsername());
		User fetchedUser = null;
		try {
			
			fetchedUser = userService.authenticateUser(user);
			System.out.println("fetchedUser: " + fetchedUser.toString());

			// creates user's transaction
			UserTransaction userTransaction = new UserTransaction();
			userTransaction.setUser(fetchedUser);

			// creates session
			session.invalidate();
			HttpSession newSession = request.getSession();
			newSession.setAttribute("username", user.getUsername());
			newSession.setAttribute("counter", 0);
			
			
			
			System.out.println("Session username: "+newSession.getAttribute("username")+" counter: "+newSession.getAttribute("counter"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fetchedUser;
	}

	@RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public Message register(@RequestBody User user) {
		Message message = new Message();
		System.out.println("Register request is received coming from " + user.getUsername());

		try {
			message = userService.addUser(user);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return message;
	}

	@RequestMapping(value = "/songList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SongListResponse getSongs(HttpServletRequest request) {
		SongListResponse slr = new SongListResponse();
		System.out.println("Song list request is received");
		HttpSession session = request.getSession();
		// fetching transaction details of the user
		int counter = (int) session.getAttribute("counter") + 1;
		String username = (String)session.getAttribute("username");
		System.out.println("Checking " + username + "'s session counter: " + counter);

		return slr;
	}

}
