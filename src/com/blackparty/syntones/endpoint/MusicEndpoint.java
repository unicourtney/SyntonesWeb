package com.blackparty.syntones.endpoint;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.blackparty.syntones.core.PlayedSongTimeRanking;
import com.blackparty.syntones.core.PlaylistGenerator;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Genre;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.PlayedSongsByTime;
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
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.response.GeneratePlaylistResponse;
import com.blackparty.syntones.response.GenreResponse;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.ListenResponse;
import com.blackparty.syntones.response.LogoutResponse;
import com.blackparty.syntones.response.PlayedSongsByTimeResponse;
import com.blackparty.syntones.response.PlayedSongsResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.RemovePlaylistResponse;
import com.blackparty.syntones.response.RemoveToPlaylistResponse;
import com.blackparty.syntones.response.SongLyricsResponse;
import com.blackparty.syntones.response.TagsResponse;
import com.blackparty.syntones.response.ThreeItemSetResponse;
import com.blackparty.syntones.response.TwoItemSetResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.GenreService;
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
	@Autowired
	private GenreService genreService;

	@RequestMapping(value = "/generatePlaylist", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse generatedPlaylist(@RequestBody User user) {
		GeneratePlaylistResponse gpr = new GeneratePlaylistResponse();
		Message message = new Message();
		try {
			System.out.println("received request to generate playlist coming from :" + user.getUsername());

			// getcurrent time
			PlaylistGenerator pg = new PlaylistGenerator();
			Date currdate = new Date();
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			DateFormat timeFormat = new SimpleDateFormat("HH");
			Timestamp timeStamp = new Timestamp(currdate.getTime());
			String partOfDay = pg.part_of_day(Integer.parseInt(timeFormat.format(currdate)));

			// get number of sessions of the user
			int numberOfSession = playedSongsService.getNumberOfSessions(user.getUserId());
			System.out.println("Number of sessions created by the user: " + numberOfSession);
			// get "played songs" of the user
			List<PlayedSongs> playedSongList = playedSongsService.getUserPlayedSongs(user.getUserId(), partOfDay);
			for (PlayedSongs s : playedSongList) {
				System.out.println(s.toString());
			}
			// get list of songs. place on map
			Map<Long, Song> songMap = new HashMap<>();
			List<Song> songList = songService.getAllSongs();
			for (Song s : songList) {
				System.out.println(">> " + s.toStringFromDB());
				songMap.put(s.getSongId(), s);
			}

			List<Long> songIds = pg.generate(numberOfSession, songMap, playedSongList);
			List<Song> playList = songService.getAllSongs(songIds);

			message.setFlag(true);
			message.setMessage("Generating playlist is successful.");
			gpr.setMessage(message);
			gpr.setSongs(playList);
			for (Song s : playList) {
				System.out.println(s.getSongTitle() + " || " + s.getArtist().getArtistName());

			}
		} catch (Exception e) {
			message.setFlag(false);
			message.setMessage("There is a problem while generating a playlist. please refer to the webservice.");
			gpr.setMessage(message);
			gpr.setSongs(null);
			e.printStackTrace();
		}
		return gpr;
	}

	@RequestMapping(value = "/getGenre", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GenreResponse getGenre(@RequestBody String Something) throws Exception {
		GenreResponse genreResponse = new GenreResponse();
		Message message = new Message();
		System.out.println("Genre ====================================");
		List<Genre> genre = genreService.getGenreString();
		genreResponse.setGenre(genre);
		message.setMessage("Fetching song genre");
		genreResponse.setMessage(message);
		return genreResponse;

	}

	@RequestMapping(value = "/saveGeneratedPlaylist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GeneratePlaylistResponse saveGeneratedPlaylist(@RequestBody Playlist playlist) {
		System.out
				.println("Received request to save the generated playlist from : " + playlist.getUser().getUsername());
		GeneratePlaylistResponse generatedPlaylistResponse = new GeneratePlaylistResponse();
		Message message = new Message();
		List<Long> songIds = new ArrayList<>();

		for (Song s : playlist.getSongs()) {
			songIds.add(s.getSongId());
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
	public ListenResponse listen(@RequestBody TemporaryDB temporaryDB) {
		System.out.println("Received request to listen.");

		ListenResponse listenResponse = new ListenResponse();
		Message message = new Message();

		if (temporaryDB != null) {
			message.setMessage("ABLE TO ACCESS LISTEN");
			System.out.println("INSERTING TO TEMP DB");
			try {
				playedSongsService.saveTemporaryDB(temporaryDB);
			} catch (DataIntegrityViolationException e) {

			}
		} else {
			message.setMessage("NO SONG ID AND USER ID<|3");
			System.out.println("NOT INSERTING TO TEMP DB");
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
	public TwoItemSetResponse getTwoItemSet(@RequestBody String songId) {
		System.out.println("Received request to get Two Item Sets");
		TwoItemSetResponse twoItemSetResponse = new TwoItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING TWO ITEM SETS");
		List<Song> songList = new ArrayList<>();
		System.out.println(
				"SONG ID " + songId.replace("\"", "").trim());
//		playedSongsService.getRecommendation(songId.replace("\"", "").trim());
		List<Long> two_item_set_list = playedSongsService.getTwoItemSet(songId.replace("\"", "").trim());
		if (!two_item_set_list.isEmpty()) {
			songList = playedSongsService.getSongs(two_item_set_list);
		
			System.out.println("TWO ITEM SIZE " + songList.size());

			twoItemSetResponse.setSongList(songList);
		}
		twoItemSetResponse.setMessage(message);

		return twoItemSetResponse;
	}

	@RequestMapping(value = "/getThreeItemSet", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ThreeItemSetResponse getThreeItemSet(@RequestBody String songId) {
		System.out.println("Received request to get Three Item Sets");
		ThreeItemSetResponse threeItemSetResponse = new ThreeItemSetResponse();
		Message message = new Message();
		message.setMessage("FETCHING THREE ITEM SETS");
		System.out.println(
				"SONG ID " + songId.replace("\"", "").trim());
		List<Long> three_item_set_list = playedSongsService.getThreeItemSet(songId.replace("\"", "").trim());
//		playedSongsService.getRecommendation2(songId.replace("\"", "").trim());
		if (!three_item_set_list.isEmpty()) {

			List<Song> songList = playedSongsService.getSongs(three_item_set_list);
			System.out.println("THREE ITEM SIZE " + songList.size());
			threeItemSetResponse.setSongList(songList);
		}
		threeItemSetResponse.setMessage(message);

		return threeItemSetResponse;
	}

	@RequestMapping(value = "/logoutProcess", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public LogoutResponse logout() throws SQLException {
		System.out.println("Received request logout");
		LogoutResponse logoutResponse = new LogoutResponse();
		Message message = new Message();
		message.setMessage("You have logged out");
		List<TemporaryDB> temporaryDB = playedSongsService.getTemporaryDB();
		if (!playedSongsService.getTemporaryDB().isEmpty()) {
			System.out.println("========================= I'M NOT EMPTY");

			for (TemporaryDB a : temporaryDB) {
				System.out.println(a.getSession_id());
				PlayedSongs playedSongs = new PlayedSongs();
				playedSongs.setDate(a.getDate());
				playedSongs.setTrack_id(String.valueOf(a.getSong_id()));
				playedSongs.setSession_id(a.getSession_id());
				playedSongs.setUser_id(a.getUser_id());
				playedSongs.setPart_of_day(a.getPart_of_day());
				playedSongsService.savePlayedSongs(playedSongs);
			}

			playedSongsService.truncateTemporaryDB();

			List<PlayedSongs> played_songs_list = playedSongsService.getPlayedSongs();

			System.out.println("AR - ADMIN CONTROLLER");
			AssociationRule ar = new AssociationRule();

			ArrayList<String> track_id_list = ar.getUniqueOneItemTracks(played_songs_list);
			ArrayList<String> session_id_list = ar.getUniqueSessions(played_songs_list);

			playedSongsService.truncatePlayedSongsByTime();

			List<PlayedSongs> played_songs_list_asc = playedSongsService.getPlayedSongsAsc();

			PlayedSongTimeRanking playedSongTimeRanking = new PlayedSongTimeRanking();
			ArrayList<PlayedSongsByTime> played_songs_by_time_list = playedSongTimeRanking
					.getSongTimeRanking(played_songs_list_asc);

			playedSongsService.savePlayedSongsByTime(played_songs_by_time_list);

			if (session_id_list.size() > 1) {

				playedSongsService.truncateOneItemSetCount();
				playedSongsService.truncateTwoItemSet();
				playedSongsService.truncateThreeItemSet();

				int[][] oneItemBasket = ar.getOneItemBasket(played_songs_list, session_id_list, track_id_list);

				ArrayList<OneItemSetCount> one_item_set_count_list = ar.getOneItemCount(session_id_list, oneItemBasket,
						played_songs_list, track_id_list);

				playedSongsService.insertOneItemSetCount(one_item_set_count_list);
				ArrayList<String> passed_one_item_support_list = ar.getPassedOneItemSupport(one_item_set_count_list);
				ArrayList<TwoItemSetCombo> two_item_set_combo_list = ar.getTwoItemCombo(passed_one_item_support_list);

				int[][] twoItemBasket = ar.getTwoItemBasket(two_item_set_combo_list, oneItemBasket, track_id_list,
						session_id_list);

				ArrayList<TwoItemSet> two_item_set_list = ar.getTwoItemSet(one_item_set_count_list, track_id_list,
						twoItemBasket, two_item_set_combo_list, session_id_list);

				playedSongsService.insertTwoItemSet(two_item_set_list);

				ArrayList<String> passed_two_item_support_list = ar.getPassedTwoItemSupport(two_item_set_list);
				ArrayList<ThreeItemSetCombo> three_item_set_combo_list = ar
						.getThreeItemCombo(passed_two_item_support_list, two_item_set_list);

				int[][] threeItemBasket = ar.getThreeItemBasket(oneItemBasket, track_id_list, three_item_set_combo_list,
						twoItemBasket, two_item_set_combo_list, session_id_list);

				ArrayList<ThreeItemSet> three_item_set_list = ar.getThreeItemSet(three_item_set_combo_list,
						two_item_set_list, threeItemBasket, session_id_list, one_item_set_count_list);

				playedSongsService.insertThreeItemSet(three_item_set_list);

				System.out.println("AR ANALYSIS, DONE .");

			}

		}

		logoutResponse.setMessage(message);
		return logoutResponse;
	}

	@RequestMapping(value = "/getPlayedSongs", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public PlayedSongsResponse getPlayedSongs() throws SQLException {

		System.out.println("Received request to get played songs");
		PlayedSongsResponse playedSongsResponse = new PlayedSongsResponse();
		Message message = new Message();
		message.setMessage("FETCHING PLAYED SONGS");

		List<PlayedSongs> playedSongsList = playedSongsService.getPlayedSongs();
		if (!playedSongsList.isEmpty()) {

			message = new Message("Fetching played songs is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		playedSongsResponse.setMessage(message);
		playedSongsResponse.setPlayedSongsList(playedSongsList);

		return playedSongsResponse;
	}

	@RequestMapping(value = "/getPlayedSongsByTime", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public PlayedSongsByTimeResponse getPlayedSongsByTime() throws SQLException {

		System.out.println("Received request to get played songs by time");
		PlayedSongsByTimeResponse playedSongsByTimeResponse = new PlayedSongsByTimeResponse();
		Message message = new Message();
		message.setMessage("FETCHING PLAYED SONGS BY TIME");

		List<PlayedSongsByTime> playedSongsByTimeList = playedSongsService.getPlayedSongsByTime();
		if (!playedSongsByTimeList.isEmpty()) {

			message = new Message("Fetching played songs is successful.", true);
		} else {
			message = new Message("Query returns zero results.", true);
		}

		playedSongsByTimeResponse.setMessage(message);
		playedSongsByTimeResponse.setPlayedSongsByTimeList(playedSongsByTimeList);
		return playedSongsByTimeResponse;
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

	@RequestMapping(value = "/addSongsToPlaylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse addSongsToPlaylist(@RequestBody PlaylistSong playlistSong) {
		LibraryResponse libraryResponse = new LibraryResponse();

		return libraryResponse;
	}

	@RequestMapping(value = "/checkIfSongExists", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse checkIfSongExists(@RequestBody List<PlaylistSong> playlistSong) {
		LibraryResponse libraryResponse = new LibraryResponse();

		List<Long> notExistingSongs = playlistSongService.checkIfSongExists(playlistSong);

		System.out.println("==================================================================================");

		if (notExistingSongs != null) {
			for (Long a : notExistingSongs) {
				System.out.println(a);
			}

			libraryResponse.setNotExistingSongs(notExistingSongs);
		}
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

	@RequestMapping(value = "/getLyrics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public SongLyricsResponse getLyrics(@RequestBody long songId) throws SQLException {

		System.out.println("Received request to get song lyrics");
		SongLyricsResponse songLyricsResponse = new SongLyricsResponse();
		Message message = new Message();
		message.setMessage("FETCHING SONG LYRICS");

		String lyrics = songService.getLyrics(songId);

		songLyricsResponse.setMessage(message);
		songLyricsResponse.setLyrics(lyrics);

		return songLyricsResponse;
	}
}
