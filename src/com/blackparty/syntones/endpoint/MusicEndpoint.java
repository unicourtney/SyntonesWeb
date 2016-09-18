package com.blackparty.syntones.endpoint;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.SongService;

@RestController
@Component
public class MusicEndpoint {
	@Autowired private SongService songService; 
	@Autowired private PlaylistService playlistService;
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
