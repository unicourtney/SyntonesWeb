package com.blackparty.syntones.endpoint;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongService;

 
@RestController
@Component
public class MusicEndpoint {
	@Autowired private SongService songService; 
	@Autowired private PlaylistService playlistService;
	@Autowired private PlaylistSongService playlistSongService;
	

	@RequestMapping(value="/savePlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public PlaylistResponse savePlayList(@RequestBody Playlist playlist) {
		PlaylistResponse playlistResponse = new PlaylistResponse();
		System.out.println("received request to save a playlist from: " + playlist.getUser().getUsername());
		try {
			playlistService.savePlaylist(playlist);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Message message = new Message("", true);
		playlistResponse.setMessage(message);
		return playlistResponse;
	}
	
	@RequestMapping(value="/removeToPlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public RemoveToPlaylistResponse removeToPlaylist(@RequestBody PlaylistSong playlistSong){
		RemoveToPlaylistResponse rtpResponse = new RemoveToPlaylistResponse();
		Message message = new Message();
		try{
			playlistSongService.removeToPlaylist(playlistSong);
		}catch(Exception e){
			e.printStackTrace();
			message.setMessage("Exception occured in the web service");
			message.setFlag(false);
		}
		message.setFlag(true);
		message.setMessage("Remove success");
		rtpResponse.setMessage(message);
		return rtpResponse;
	}
	
	@RequestMapping(value="/addToPlaylist",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse addToPlaylist(@RequestBody PlaylistSong playlistSong){
		LibraryResponse libraryResponse = new LibraryResponse();
		Message message = new Message();
		try{
			playlistSongService.addToplaylist(playlistSong);
			List<Playlist> playlists = playlistService.getPlaylist(playlistSong.getUser());
			libraryResponse.setRecentlyPlayedPlaylists(playlists);
			message.setMessage("saving successful.");
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
			libraryResponse.setMessage(message);
			return libraryResponse;
		}
		message.setFlag(true);
		libraryResponse.setMessage(message);
		return libraryResponse;
	}
	
	@RequestMapping(value="/playPlaylist")
	public PlaylistSongsResponse playPlaylist(@RequestBody long id){
		PlaylistSongsResponse ppResponse = new PlaylistSongsResponse();
		Playlist playlist = new Playlist();
		Message message = new Message();
		try{
			playlist = playlistService.getSongsFromPlaylist(id);
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
		}
		message.setFlag(true);
		ppResponse.setPlaylist(playlist);
		return ppResponse;
	}
}
