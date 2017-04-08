package com.blackparty.syntones.endpoint;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.blackparty.syntones.core.MediaResource;
import com.blackparty.syntones.core.SearchProcess;
import com.blackparty.syntones.core.Stemmer;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.Message;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.SearchResult;
import com.blackparty.syntones.model.SearchResultModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.response.ArtistResponse;
import com.blackparty.syntones.response.LibraryResponse;
import com.blackparty.syntones.response.PlaylistResponse;
import com.blackparty.syntones.response.PlaylistSongsResponse;
import com.blackparty.syntones.response.SearchResponse;
import com.blackparty.syntones.response.SongListResponse;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.PlaylistService;
import com.blackparty.syntones.service.PlaylistSongService;
import com.blackparty.syntones.service.SearchResultService;
import com.blackparty.syntones.service.SearchWordService;
import com.blackparty.syntones.service.SearchedWordBankService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;
import com.blackparty.syntones.service.StopWordService;
import com.blackparty.syntones.service.WordBankService;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

@RestController
@Component
public class NavigationEndpoint {
	@Autowired
	private SongService songService;
	@Autowired
	private PlaylistService playlistService;
	@Autowired
	private PlaylistSongService playlistSongService;
	@Autowired
	private ArtistService artistService;

	@Autowired
	SongWordBankService swService;

	@Autowired
	SearchedWordBankService searchService;

	@Autowired
	WordBankService wbService;

	@Autowired
	SearchWordService searchWordService;

	@Autowired
	SearchResultService searchResultService;

	@Autowired
	StopWordService stopWordService;

	@RequestMapping(value = "/getAllArtists", produces = MediaType.APPLICATION_JSON_VALUE)
	public ArtistResponse getAllArtist() {
		System.out.println("Received request to get all artists");
		ArtistResponse artistResponse = new ArtistResponse();
		Message message = new Message();
		try {
			List<Artist> artists = artistService.getAllArtists();
			artistResponse.setArtists(artists);
			message.setFlag(true);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			artistResponse.setMessage(message);
			return artistResponse;
		}
		artistResponse.setMessage(message);
		return artistResponse;
	}

	@RequestMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public SearchResponse search(@RequestBody String searchString) throws Exception {
		// wala pa ni siya gamit
		System.out.println("recived search request");
		SearchResponse sr = new SearchResponse();
		Message message = new Message();
		message.setMessage("search request \"" + searchString + "\"has been received.");
		sr.setMessage(message);
		System.out.println("======================= " + searchString + " =======================" + " -ENDPOINT");
		System.out.println("");
		float step5Query = 0f;
		searchString = searchString.substring(1, searchString.length() - 1).replace("\\", "");
		System.out.println("searchString >> " + searchString);
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<Song> songList = new ArrayList<Song>();

		long searchId = searchWordService.checkSearchedString(searchString);
		if (searchId != 0) {
			if (searchResultService.getSearchResult(searchId) == null || searchResultService.getSearchResult(searchId).isEmpty()) {
				songList = processSearchString(searchString, searchId);
			} else {
				List<Long> songIds = searchResultService.getSearchResult(searchId);
				ArrayList<SearchResult> rs = (ArrayList<SearchResult>) searchResultService.getSearchResult1(searchId);
				for (Long songId : songIds) {
					songList.add(songService.getSongSearchedbyIds(songId));
				}
			}
		} else {
			searchId = searchWordService.addSearchWord(searchString);
			songList = processSearchString(searchString, searchId);
		}

		if (!resultT.isEmpty()) {
			message.setMessage("Found");
		} else {
			message.setMessage("Not Found");
		}

		// List<Song> songs = songService.getSongs(searchResult.getSongs());
		// List<Artist> artists =
		// artistService.getArtists(searchResult.getArtists());
		sr.setSongs(songList);
		// sr.setArtists(artists);
		sr.setMessage(message);
		return sr;
	}
	@RequestMapping(value = "/test")
	public String toIndex() {
		System.out.println("user dir:" + System.getProperty("user.dir"));
		return "index";
	}

	@RequestMapping(value = "/library", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public LibraryResponse library(@RequestBody User user) {
		LibraryResponse lResponse = new LibraryResponse();
		Message message = new Message();
		List<Playlist> playlists = null;
		try {
			playlists = playlistService.getPlaylist(user);
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("Exception occured on the we service.");
			message.setFlag(false);
			lResponse.setMessage(message);
			return lResponse;
		}
		message.setFlag(true);
		lResponse.setRecentlyPlayedPlaylists(playlists);
		lResponse.setMessage(message);
		return lResponse;
	}

	@RequestMapping(value = "/songlist", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public SongListResponse getSongs() {
		SongListResponse slr = new SongListResponse();
		System.out.println("Song list request is received");
		Message message;
		try {
			List<Song> songList = songService.getAllSongs();
			if (!songList.isEmpty()) {
				message = new Message("Fetching all song is successful.", true);
			} else {
				message = new Message("Query returns zero results.", true);
			}
			slr.setMessage(message);
			slr.setSongList(songList);
		} catch (Exception e) {
			e.printStackTrace();
			message = new Message("Exception occured on the web service.", false);
			slr.setMessage(message);
			slr.setSongList(null);
		}
		return slr;
	}

	@RequestMapping(value = "/playlist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public PlaylistResponse playList(@RequestBody User user) {
		System.out.println("Received request to get playlists from: " + user.getUsername());
		PlaylistResponse playlistResponse = new PlaylistResponse();
		Message message = null;
		try {
			List<Playlist> playlists = playlistService.getPlaylist(user);
			if (playlists == null) {
				return null;
			}
			if (!playlists.isEmpty()) {
				playlistResponse.setPlaylists(playlists);
				System.out.println("Web service is responding from the playlist request with the following playlists:");
				for (Playlist e : playlists) {
					Playlist pl = e;
					System.out.println("Playlist Name: " + e.getPlaylistName());
				}
				message = new Message("", true);
			} else {
				message = new Message("", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		playlistResponse.setMessage(message);
		return playlistResponse;
	}

	@RequestMapping(value = "/playlistSong", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PlaylistSongsResponse playlistSong(@RequestBody Map<String, String> data) {
		PlaylistSongsResponse playlistSongsResponse = new PlaylistSongsResponse();
		System.out.println("Received playlist-song request from: " + data.get("username"));

		long id = Long.parseLong(data.get("id"));
		Message message = new Message();
		try {
			Playlist playlist = new Playlist();
			playlist.setPlaylistId(id);
			playlist.setSongs(playlistSongService.getSongs(id));
			for (Song s : playlist.getSongs()) {
				System.out.println(s.toString());
			}
			playlistSongsResponse.setPlaylist(playlist);
			message.setFlag(true);
		} catch (Exception e) {
			e.printStackTrace();
			message.setFlag(false);
			message.setMessage("Error occured on the webservice");
		}
		playlistSongsResponse.setMessage(message);
		return playlistSongsResponse;
	}
	
	private ArrayList<Song> processSearchString(String searchWord, long searchId) {
		SearchProcess sp = new SearchProcess();
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<SearchResult> tempResultT = new ArrayList<SearchResult>();
		ArrayList<Song> songList = new ArrayList<Song>();
		ArrayList<String> searchList = new ArrayList<String>();
		ArrayList<String> operator = new ArrayList<String>();

		if (searchWord.startsWith("\"")) {
			songList = (ArrayList<Song>) songService.getSongQ(searchWord);
		} else if (searchWord.startsWith("[")) {
			String tempSvar = "";
			boolean qflag = false, orFlag = false;
			StringTokenizer str = new StringTokenizer(searchWord, "[()] ");
			while (str.hasMoreTokens()) {
				String token = str.nextToken().toLowerCase().trim();
				if (token.contains("\"") && !qflag) {
					tempSvar = token.replace("\"", "") + " ";
					qflag = true;
				} else if (token.contains("\"") && qflag && !orFlag) {
					qflag = false;
					tempSvar += token.replace("\"", "");
					searchList.add(tempSvar);
				} else if (qflag && !token.contains("\"")) {
					tempSvar += token + " ";
				} else if (token.equalsIgnoreCase("or") && !qflag) {
					orFlag = true;
				} else if ((token.equalsIgnoreCase("and") || token.equalsIgnoreCase("not")) && !qflag) {
					orFlag = false;
					operator.add(token);
				} else if (!qflag && !token.contains("\"") && !orFlag) {
					searchList.add(token);
				} else if (token.contains("\"") && qflag && orFlag) {
					qflag = false;
					tempSvar += " " + token.replace("\"", "") + " " + searchList.get(searchList.size() - 1);
					searchList.remove(searchList.size() - 1);
					searchList.add(tempSvar);
					orFlag = false;
				} else if (!qflag && !token.contains("\"") && orFlag) {
					tempSvar = token + " " + searchList.get(searchList.size() - 1);
					searchList.remove(searchList.size() - 1);
					searchList.add(tempSvar);
					orFlag = false;
				}
			}
			for (String sWord : searchList) {
				System.out.println("searched string >> " + sWord);
			}
			int p = 0;
			for (String sWord : searchList) {
				if (tempResultT.isEmpty()) {
					resultT = processTFIDFBoolean(sWord, null, searchId);
					tempResultT.addAll(resultT);
				} else {
					if (!operator.isEmpty() && operator.size() == 1) {
						if (operator.get(0).equalsIgnoreCase("AND")) {
							List<Long> songIds = swService.getDistinctForAnd(tempResultT);
							resultT = processTFIDFBoolean(sWord, songIds, searchId);
							tempResultT.removeAll(tempResultT);
							tempResultT.addAll(resultT);
						} else if (operator.get(0).equalsIgnoreCase("NOT")) {
							List<Long> songIds = swService.getDistinctForAnd(tempResultT);
							resultT = processTFIDFBoolean(sWord, songIds, searchId);
							tempResultT.removeAll(resultT);
						}
					} else if (operator.size() > 1) {
						if (operator.get(p).equalsIgnoreCase("AND")) {
							List<Long> songIds = swService.getDistinctForAnd(tempResultT);
							// tempResultT.removeAll(tempResultT);
							tempResultT.addAll(processTFIDF(sWord, songIds, searchId));
							p++;
						} else if (operator.get(p).equalsIgnoreCase("NOT")) {
							List<Long> songIds = swService.getDistinctForAnd(tempResultT);
							resultT = processTFIDFBoolean(sWord, songIds, searchId);
							tempResultT.removeAll(resultT);
							p++;
						}
					}
				}
			}
			resultT = tempResultT;
			searchResultService.addResult(resultT);

			List<Long> songIds = searchResultService.getSearchResult(searchId);
			for (Long songId : songIds) {
				songList.add(songService.getSongSearchedbyIds(songId));
			}

		} else {
			resultT = processTFIDF(searchWord, null, searchId);
			searchResultService.addResult(resultT);

			List<Long> songIds = searchResultService.getSearchResult(searchId);
			for (Long songId : songIds) {
				songList.add(songService.getSongSearchedbyIds(songId));
			}
		}

		return songList;
	}

	private ArrayList<SearchResult> processTFIDF(String searchWord, List<Long> songIds, long searchId) {
		SearchProcess sp = new SearchProcess();
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<String> wb = new ArrayList<String>();
		List<SongWordBank> sWordList = new ArrayList<SongWordBank>();
		List<String> stopwords = stopWordService.getStopWords();
		StringTokenizer str = new StringTokenizer(searchWord, " ");
		System.out.println("RESULT WILDCARD");
		boolean flag = false;
		int flagW = 0;
		while (str.hasMoreTokens()) {
			String token = str.nextToken().toLowerCase().trim();
			if (!token.equalsIgnoreCase("")) {
				if (!sp.isStopWord(token, stopwords)) {
					if (!token.equalsIgnoreCase(null)) {
						if (token.matches("^([A-z]*)(\\*|\\?)([A-z]*)$")) {
							token = token.replace("*", "_").replace("?", "_");
							List<String> words = wbService.getWordsFromWildcard(token);
							if (!words.isEmpty()) {
								for (String word : words) {
									System.out.println(word);
									flag = searchService.updateWord(word, flag);
								}
								wb.addAll(words);
							}
						} else {
							flag = searchService.updateWord(token, flag);
							wb.add(token);
						}
					}
				}
			}
		}
		if (flag) {
			searchService.setStep4();
		}
		wb = (ArrayList<String>) searchService.getWords(wb);
		for (String w : wb) {
			System.out.println("wb>> " + w);
		}

		float step5Query = (float) Math.sqrt(searchService.step5(wb));
		System.out.println("step5Q >> " + step5Query);
		if (songIds == null) {
			songIds = swService.getDistinctIdBySearchedStrings(wb);
		}
		for (Long songId : songIds) {
			SearchResult model = new SearchResult();
			if (wb.size() > 1) {
				sWordList = swService.checkWord(wb, songId);
				flagW = sp.parseWord(sWordList);
			}
			if (wb.size() == flagW) {
				model.setSongId(songId);
				model.setSearchId(searchId);
				model.setDegree((float) 8.123);
			} else {
				float step5 = (float) Math.sqrt(swService.getStep5(songId));
				float step6 = (float) searchService.step6(songId) / (float) (step5 * step5Query);
				float radians = (float) Math.acos(step6);
				float degree = (float) Math.toDegrees(radians);
				model.setSongId(songId);
				model.setSearchId(searchId);
				model.setDegree(degree);
			}

			if (model.getDegree() < 90f) {
				resultT.add(model);
			}
		}
		return resultT;
	}

	private ArrayList<SearchResult> processTFIDFBoolean(String searchWord, List<Long> songIds, long searchId) {
		SearchProcess sp = new SearchProcess();
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<String> wb = new ArrayList<String>();
		List<SongWordBank> sWordList = new ArrayList<SongWordBank>();
		List<String> stopwords = stopWordService.getStopWords();
		StringTokenizer str = new StringTokenizer(searchWord, " ");
		System.out.println("RESULT WILDCARD");
		boolean flag = false;
		int flagW = 0;
		while (str.hasMoreTokens()) {
			String token = str.nextToken().toLowerCase().trim();
			if (!token.equalsIgnoreCase("")) {
				if (!sp.isStopWord(token, stopwords)) {
					if (!token.equalsIgnoreCase(null)) {
						if (token.matches("^([A-z]*)(\\*|\\?)([A-z]*)$")) {
							token = token.replace("*", "_").replace("?", "_");
							List<String> words = wbService.getWordsFromWildcard(token);
							if (!words.isEmpty()) {
								for (String word : words) {
									System.out.println(word);
									flag = searchService.updateWord(word, flag);
								}
								wb.addAll(words);
							}
						} else {
							flag = searchService.updateWord(token, flag);
							wb.add(token);
						}
					}
				}
			}
		}
		if (flag) {
			searchService.setStep4();
		}
		for (String w : wb) {
			System.out.println("wb>> " + w);
		}
		float step5Query = (float) Math.sqrt(searchService.step5(wb));
		System.out.println("step5Q >> " + step5Query);
		if (songIds == null) {
			songIds = swService.getDistinctId(wb);
		}
		for (Long songId : songIds) {
			SearchResult model = new SearchResult();
			if (wb.size() > 1) {
				sWordList = swService.checkWord(wb, songId);
				flagW = sp.parseWord(sWordList);
			}
			if (wb.size() == flagW) {
				model.setSongId(songId);
				model.setSearchId(searchId);
				model.setDegree((float) 8.123);
			} else {
				float step5 = (float) Math.sqrt(swService.getStep5(songId));
				float step6 = (float) searchService.step6(songId) / (float) (step5 * step5Query);
				float radians = (float) Math.acos(step6);
				float degree = (float) Math.toDegrees(radians);
				model.setSongId(songId);
				model.setSearchId(searchId);
				model.setDegree(degree);
			}

			if (model.getDegree() < 90f) {
				resultT.add(model);
			}
		}
		return resultT;
	}
}
