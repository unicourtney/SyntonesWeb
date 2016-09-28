package com.blackparty.syntones.endpoint;

import java.io.File;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.AssociationRule;
import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.PlaylistSong;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.model.TemporaryDB;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.ThreeItemSetCombo;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.model.TwoItemSetCombo;
import com.blackparty.syntones.response.GeneratePlaylistResponse;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.ListenResponse;
import com.blackparty.syntones.response.LogoutResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemovePlaylistResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;
import com.blackparty.syntones.response.TagsResponse;
import com.blackparty.syntones.response.ThreeItemSetResponse;
import com.blackparty.syntones.response.TwoItemSetResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SongLineService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.TagService;
import com.blackparty.syntones.service.TagSongService;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;

@RestController
@Component
public class MusicEndpoint {
	@Autowired
	private SongService songService;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private PlaylistSongService playlistSongService;
	@Autowired
	private PlayedSongsService playedSongsService;
	@Autowired
	private ArtistService artistService;
	@Autowired
	private SongLineService songLineService;
	@Autowired
	private TagSongService tagSongService;
	@Autowired
	private TagService tagService;

	@RequestMapping(value = "/generatePlaylistByArtist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylistByArtist(@RequestBody String artistName) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist for : "+artistName);
		Message message = new Message();
		try{
			//getting all songs of the artist
			artistName = artistName.replace("\"", "");
			Artist artist = artistService.getArtist(artistName);
			List<Long> artistSongIds = songService.getAllSongsByArtist(artist);	
			List<SongLine> songLines = songLineService.getAllLines();
			ArrayList<Long> songIds = new ArrayList<>();
			HashMap<Long, Long> map = new HashMap<>();
			for(long s: artistSongIds){
				System.out.println("Artist song id: "+s);
			}
			for(int i=0;i<songLines.size();i++){
				for(int j=0; j<artistSongIds.size();j++){
					if(songLines.get(i).getSongId() == artistSongIds.get(j)){
						System.out.println("hit!!");
						long id1 = 0;
						long id2 = 0;
						if(i == 0){
							id1 = songLines.get(i+1).getSongId();
						}else{
							id1 = songLines.get(i+1).getSongId();
							id2 = songLines.get(i-1).getSongId();
						}
						if(!map.containsKey(id1)){
							map.put(id1,id1);
							songIds.add(id1);
							System.out.println("Adding song :"+id1);
						} 
						if(!map.containsKey(id2)){
							map.put(id2,id2);
							songIds.add(id2);
							System.out.println("Adding song :"+id2);
						}
					}
				}
			}
			//adding artist's song
			List<Song> songList = new ArrayList<>();
			int counter = 0;
			if(!songIds.isEmpty()){
				for(Long id:songIds){
					if(counter < 10){
						Song song = songService.getSong(id);
						songList.add(song);
					}
					counter++;
				}
				System.out.println("Generating Songs successful.");
				for(Song s:songList){
					System.out.println(s.toString());
				}
				message.setMessage("Generating Songs successful.");
				message.setFlag(true);
				generatePlaylistResponse.setSongs(songList);
			}else{
				message.setFlag(false);
				System.out.println("List is empty. No lists of songs generated.");
				message.setMessage("List is empty. No lists of songs generated.");
			}
		}catch(Exception e){
			e.printStackTrace();
			message.setFlag(false);
			System.out.println("error happend on the web service");
			message.setMessage("error happend on the web service");
			generatePlaylistResponse.setMessage(message);
			return generatePlaylistResponse;
		}
		generatePlaylistResponse.setMessage(message);
		return generatePlaylistResponse;
	}
	
	@RequestMapping(value = "/saveGeneratedPlaylist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse saveGeneratedPlaylist(@RequestBody Playlist playlist) {
		System.out
				.println("Received request to save the generated playlist from : " + playlist.getUser().getUsername());
		GeneratePlaylistResponse generatedPlaylistResponse = new GeneratePlaylistResponse();
		Message message = new Message();
		long[] songIds = playlist.getSongIds();
		for (long s : songIds) {
			System.out.println(s);
		}
		try {
			long playlistId = playlistService.addGeneratedPlaylist(playlist);
			System.out.println("PLAYLIST ID: " + playlistId);
			ArrayList<PlaylistSong> pls = new ArrayList<>();
			for (long s : songIds) {
				PlaylistSong playlistSong = new PlaylistSong();
				playlistSong.setSongId(s);
				playlistSong.setPlaylistId(playlistId);
				pls.add(playlistSong);
			}
			for (PlaylistSong f : pls) {
				System.out.println(f.toString());
			}
			playlistSongService.savebatchPlaylistSong(pls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		generatedPlaylistResponse.setMessage(message);
		return generatedPlaylistResponse;
	}
	
	

	

	@RequestMapping(value = "/generatePlaylistByTags", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatePlaylist(@RequestBody String tag) {
		GeneratePlaylistResponse generatePlaylistResponse = new GeneratePlaylistResponse();
		System.out.println("Received request to generate playlist for : ");
		List<TagSong> tagsongs = new ArrayList<>();
		Message message = new Message();
		try {
		
				System.out.println(tag);
				tagsongs = tagSongService.getSongsByTags(tag);
			
			ArrayList<Song> songs = new ArrayList<>();
			for (TagSong ts : tagsongs) {
				System.out.println(ts.toString());
				Song song = songService.getSong(ts.getSongId());
				songs.add(song);
			}
			for (Song s : songs) {
				System.out.println(s.toStringFromDB());
			}
			if (songs.size() > 0) {
				generatePlaylistResponse.setSongs(songs);
				message.setFlag(true);
				generatePlaylistResponse.setMessage(message);
			} else {
				message.setFlag(false);
				message.setMessage("no songs can be found with the given tag(s)");
				generatePlaylistResponse.setMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return generatePlaylistResponse;
	}

	@RequestMapping(value = "/getAllTags", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public TagsResponse getAllTags() {
		TagsResponse tagResponse = new TagsResponse();
		List<Tag> tags = null;
		Message message = new Message();
		try {
			tags = tagService.getAllTags();
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Exception occured on the webservice");
			message.setFlag(false);
			tagResponse.setMessage(message);
			return tagResponse;
		}
		tagResponse.setTags(tags);
		message.setFlag(true);
		tagResponse.setMessage(message);
		return tagResponse;
	}
	
	@RequestMapping(value = "/removePlaylist")
	public RemovePlaylistResponse removePlaylist(@RequestBody Playlist playlist) {
		System.out.println("Received request to remove playlist from: " + playlist.getUser().getUsername());
		RemovePlaylistResponse removePlaylistResponse = new RemovePlaylistResponse();
		Message message = new Message();
		try {
			playlistService.removePlaylist(playlist);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the webservice");
		}
		message.setFlag(true);
		removePlaylistResponse.setMessage(message);
		return removePlaylistResponse;
	}

	@RequestMapping(value = "/listen", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ListenResponse listen(@RequestBody List<TemporaryDB> temporaryDB) {
		System.out.println("Received request to listen.");

		ListenResponse listenResponse = new ListenResponse();
		Message message = new Message();

		if (temporaryDB != null) {
			message.setMessage("ABLE TO ACCESS LISTEN");
			for (TemporaryDB a : temporaryDB) {

				System.out.println("Listen: \nSong ID: " + a.getSong_id() + "User ID: " + a.getUser_id());

			}
			try {
				playedSongsService.saveTemporaryDB(temporaryDB);
			} catch (DataIntegrityViolationException e) {

			}
		} else {
			message.setMessage("NO SONG ID AND USER ID<|3");
		}
		message.setFlag(true);
		listenResponse.setMessage(message);

		// insert service that will fetch recommended songs to be added on
		// listenresponse

		return listenResponse;
	}

	@RequestMapping(value = "/listenPlaylist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public ListenResponse listen(@RequestBody Playlist playlist) {
		System.out.println("Received request to listen for playlist: " + playlist.getPlaylistId() + " from: "
				+ playlist.getUser().getUsername());
		ListenResponse lResponse = new ListenResponse();
		// insert service that will fetch recommended songs to be added on
		// listenresponse
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		Date dateObject = new Date();
		System.out.println("Date and time: " + dateObject);

		return lResponse;
	}

	@RequestMapping(value = "/getTwoItemSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public TwoItemSetResponse getTwoItemSet() {
		System.out.println("Received request to get Two Item Sets");
		TwoItemSetResponse twoItemSetResponse = new TwoItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING TWO ITEM SETS");

		List<TwoItemSet> two_item_set_list = playedSongsService.getTwoItemSet();
		if (!two_item_set_list.isEmpty()) {
			message = new Message("Fetching two item set is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		twoItemSetResponse.setMessage(message);
		twoItemSetResponse.setTwo_item_set_list(two_item_set_list);
		return twoItemSetResponse;
	}

	@RequestMapping(value = "/getThreeItemSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ThreeItemSetResponse getThreeItemSet() {
		System.out.println("Received request to get Two Item Sets");
		ThreeItemSetResponse threeItemSetResponse = new ThreeItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING THREE ITEM SETS");

		List<ThreeItemSet> three_item_set_list = playedSongsService.getThreeItemSet();
		if (!three_item_set_list.isEmpty()) {
			message = new Message("Fetching three item set is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		threeItemSetResponse.setMessage(message);
		threeItemSetResponse.setThree_item_set_list(three_item_set_list);
		return threeItemSetResponse;
	}

	@RequestMapping(value = "/logoutProcess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public LogoutResponse logout() throws SQLException {
		System.out.println("Received request logout");
		LogoutResponse logoutResponse = new LogoutResponse();
		Message message = new Message();
		message.setMessage("You have logged out");
		List<TemporaryDB> temporaryDB = playedSongsService.getTemporaryDB();

		for (TemporaryDB a : temporaryDB) {
			System.out.println(a.getUser_id());
			PlayedSongs playedSongs = new PlayedSongs();
			playedSongs.setTrack_id(String.valueOf(a.getSong_id()));
			playedSongs.setUser_id(a.getUser_id());
			playedSongsService.savePlayedSongs(playedSongs);
		}

		playedSongsService.truncateTemporaryDB();

		List<PlayedSongs> played_songs_list = playedSongsService.getPlayedSongs();

		System.out.println("AR - ADMIN CONTROLLER");
		AssociationRule ar = new AssociationRule();

		ArrayList<String> track_id_list = ar.getUniqueOneItemTracks(played_songs_list);
		ArrayList<String> session_id_list = ar.getUniqueSessions(played_songs_list);

		if (session_id_list.size() > 1) {

			playedSongsService.truncateOneItemSetCount();
			playedSongsService.truncateTwoItemSet();
			playedSongsService.truncateThreeItemSet();

			int[][] oneItemBasket = ar.getOneItemBasket(played_songs_list, session_id_list, track_id_list);

			ArrayList<OneItemSetCount> one_item_set_count_list = ar.getOneItemCount(session_id_list, oneItemBasket,
					played_songs_list, track_id_list);
			playedSongsService.insertOneItemSetCount(one_item_set_count_list);
			ArrayList<TwoItemSetCombo> two_item_set_combo_list = ar.getTwoItemCombo(track_id_list);

			int[][] twoItemBasket = ar.getTwoItemBasket(two_item_set_combo_list, oneItemBasket, track_id_list,
					session_id_list);

			ArrayList<TwoItemSet> two_item_set_list = ar.getTwoItemSet(one_item_set_count_list, track_id_list,
					twoItemBasket, two_item_set_combo_list, session_id_list);

			playedSongsService.insertTwoItemSet(two_item_set_list);

			ArrayList<ThreeItemSetCombo> three_item_set_combo_list = ar.getThreeItemCombo(track_id_list);
			int[][] threeItemBasket = ar.getThreeItemBasket(oneItemBasket, track_id_list, three_item_set_combo_list,
					twoItemBasket, two_item_set_combo_list, session_id_list);

			ArrayList<ThreeItemSet> three_item_set_list = ar.getThreeItemSet(three_item_set_combo_list,
					two_item_set_list, threeItemBasket, session_id_list);

			playedSongsService.insertThreeItemSet(three_item_set_list);

		}

		logoutResponse.setMessage(message);
		return logoutResponse;
	}

	@RequestMapping(value = "/savePlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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

	@RequestMapping(value = "/removeToPlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public RemoveToPlaylistResponse removeToPlaylist(@RequestBody PlaylistSong playlistSong) {
		RemoveToPlaylistResponse rtpResponse = new RemoveToPlaylistResponse();
		Message message = new Message();
		try {
			playlistSongService.removeToPlaylist(playlistSong);
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Exception occured in the web service");
			message.setFlag(false);
		}
		message.setFlag(true);
		message.setMessage("Remove success");
		rtpResponse.setMessage(message);
		return rtpResponse;
	}

	@RequestMapping(value = "/addToPlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse addToPlaylist(@RequestBody PlaylistSong playlistSong) {
		LibraryResponse libraryResponse = new LibraryResponse();
		Message message = new Message();
		try {
			playlistSongService.addToplaylist(playlistSong);
			List<Playlist> playlists = playlistService.getPlaylist(playlistSong.getUser());
			libraryResponse.setRecentlyPlayedPlaylists(playlists);
			message.setMessage("saving successful.");
		} catch (Exception e) {
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

	@RequestMapping(value = "/playPlaylist")
	public PlaylistSongsResponse playPlaylist(@RequestBody long id) {
		PlaylistSongsResponse ppResponse = new PlaylistSongsResponse();
		Playlist playlist = new Playlist();
		Message message = new Message();
		try {
			playlist = playlistService.getSongsFromPlaylist(id);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Exception occured on the web service");
		}
		message.setFlag(true);
		ppResponse.setPlaylist(playlist);
		return ppResponse;
	}
}
