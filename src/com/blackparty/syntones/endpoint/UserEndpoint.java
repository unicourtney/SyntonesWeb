package com.blackparty.syntones.endpoint;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tomcat.jni.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.model.Message;

import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.model.UserTransaction;
import com.blackparty.syntones.response.LoginResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.ProfileResponse;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.UserService;

@RestController
@Component
public class UserEndpoint {
	@Autowired
	UserService userService;

	@Autowired
	SongService songService;
	
	@Autowired PlaylistService playlistService;

	@RequestMapping(value = "/profile", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ProfileResponse showProfile(@RequestBody User user) {
		System.out.println("profile request coming from: " + user.getUsername());
		User fetchedUser = null;
		Message message = new Message();
		try {
			fetchedUser = userService.getUser(user);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("runtime error happened on the web service.");
			fetchedUser = null;
		}
		message.setFlag(true);
		ProfileResponse profileResponse = new ProfileResponse(fetchedUser, message);
		return profileResponse;
	}

	@RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public LoginResponse login(@RequestBody User user, HttpSession session, HttpServletRequest request) {
		LoginResponse loginResponse = new LoginResponse();
		System.out.println("Login request is received coming from " + user.getUsername());
		Message message = new Message();
		try {
			message = userService.authenticateUser(user);
			if(message.getFlag()){
				//get recently played playlists..
				List<Playlist> playlists = playlistService.getPlaylist(user);
				if(playlists != null){
					loginResponse.setRecentlyPlayedPlaylists(playlists);
				}else{
					loginResponse.setRecentlyPlayedPlaylists(null);
				}
			}
			loginResponse.setMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginResponse;
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

	@RequestMapping(value = "/savePlaylist",produces = MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public PlaylistResponse savePlayList(@RequestBody Playlist playlist) {
		PlaylistResponse playlistResponse = new PlaylistResponse();
		System.out.println("received request to save a playlist from: " + playlist.getUser().getUsername());
		String[] songIdList = playlist.getSongIdList();
		System.out.println("songs to be saved: ");
//		for (String e : songIdList) {
//			System.out.println(e);
//		}
		try {
			playlistService.savePlaylist(playlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message("", true);
		playlistResponse.setMessage(message);
		return playlistResponse;
	}

}
