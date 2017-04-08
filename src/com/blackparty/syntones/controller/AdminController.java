package com.blackparty.syntones.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.AssociationRule;
import com.blackparty.syntones.core.FileCopy;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.NaiveBayes;
import com.blackparty.syntones.core.PlayedSongTimeRanking;
import com.blackparty.syntones.core.SearchProcess;
import com.blackparty.syntones.core.Stemmer;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.DataSetMood;
import com.blackparty.syntones.model.DataSetSong;
import com.blackparty.syntones.model.DataSetWord;
import com.blackparty.syntones.model.IdfModel;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.PlayedSongsByTime;
import com.blackparty.syntones.model.SearchResult;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.ThreeItemSetCombo;
import com.blackparty.syntones.model.ThreeItemSetRecomSong;
import com.blackparty.syntones.model.TracksMetadata;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.model.TwoItemSetCombo;
import com.blackparty.syntones.model.TwoItemSetRecomSong;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.DataSetConditionalProbabilityService;
import com.blackparty.syntones.service.DataSetMoodService;
import com.blackparty.syntones.service.DataSetSongService;
import com.blackparty.syntones.service.DataSetWordBankService;
import com.blackparty.syntones.service.GenreService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SearchResultService;
import com.blackparty.syntones.service.SearchWordService;
import com.blackparty.syntones.service.SearchedWordBankService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;
import com.blackparty.syntones.service.StopWordService;
import com.blackparty.syntones.service.TracksMetadataService;
import com.blackparty.syntones.service.WordBankService;

@Controller

@RequestMapping("/admin")

public class AdminController {
	@Autowired
	ArtistService as;

	@Autowired
	SongService ss;

	@Autowired
	SongWordBankService swService;

	@Autowired
	PlayedSongsService playedSongsService;

	@Autowired
	SearchedWordBankService searchService;

	@Autowired
	WordBankService wbService;

	@Autowired
	SearchWordService searchWordService;

	@Autowired
	SearchResultService searchResultService;

	@Autowired
	private DataSetSongService dss;
	@Autowired
	private DataSetMoodService dsm;
	@Autowired
	private DataSetWordBankService dsWordBankService;
	@Autowired
	private TracksMetadataService tmService;

	@Autowired
	private DataSetConditionalProbabilityService dsConditionalService;

	@Autowired
	private StopWordService stopWordService;

	@RequestMapping(value = "/testClassify")
	public ModelAndView classifySongForTest(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		ModelAndView mav = new ModelAndView("index");

		int songId = 2747;
		try {
			TracksMetadata tracksMetadata = tmService.getSong(songId);
			NaiveBayes nb = new NaiveBayes();
			System.out.println(tracksMetadata.getArtistName());
			System.out.println(tracksMetadata.getTitle());

			List<DataSetWord> dataSetWordList = dsWordBankService.getAll();
			List<DataSetMood> dataSetMoodList = dsm.getAllMood();
			dataSetMoodList = dsConditionalService.setMood(dataSetMoodList);
			nb.classify(tracksMetadata, dataSetWordList, dataSetMoodList);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/dbClassify")
	public ModelAndView classifySongsOnDB(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		ModelAndView mav = new ModelAndView("index");

		try {
			NaiveBayes nb = new NaiveBayes();
			List<Song> songList = ss.getAllSongs();
			List<DataSetWord> dataSetWordList = dsWordBankService.getAll();
			List<DataSetMood> dataSetMoodList = dsm.getAllMood();
			dataSetMoodList = dsConditionalService.setMood(dataSetMoodList);
			List<String> stopWordList = stopWordService.getStopWords();
			for (Song s : songList) {
				Map.Entry<String, Double> result = nb.classify(s, dataSetWordList, dataSetMoodList, stopWordList);
				s.setMood(result.getKey());
			}
			ss.updateBatchAllSongs(songList);
		} catch (Exception e) {
			e.printStackTrace();

		}

		return mav;
	}

	@RequestMapping(value = "/train")
	public ModelAndView train() {
		ModelAndView mav = new ModelAndView("index");

		System.out.println("Attempts to train...");

		NaiveBayes nb = new NaiveBayes();
		try {

			// get all mood from the db
			List<DataSetMood> moodList = dsm.getAllMood();

			// get all songs on the dataset
			List<DataSetSong> songList = dss.getAllSongs();

			// creating a list of words coming from the lyrics of all songs
			List<String> wordList = nb.listAllWords(songList);

			// get priors and place them on the respective mood
			List<DataSetMood> moodListUpdated = nb.getPriors(songList, moodList);

			// get conditional probability
			moodListUpdated = nb.getConditionalProbability(moodListUpdated, songList);

			// save to database
			dsWordBankService.save(wordList);
			dsm.updateBatchAllMood(moodListUpdated);

		} catch (Exception e) {

			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/upload")
	public ModelAndView readSongFile(@RequestParam(value = "file") MultipartFile multiPartFile,
			@RequestParam(value = "action") String action, @RequestParam(value = "artistName") String artistName,
			@RequestParam(value = "songTitle") String songTitle, HttpServletRequest request) {
		String tempDirectory = "D:/data/temp/";
		ModelAndView mav = new ModelAndView("index");
		// please validate format of the file to be uploaded

		try {
			if (action.equals("read")) {
				// read the mp3 file first..
				// converting multipartfile into file
				System.out.println(multiPartFile.getOriginalFilename());
				File file = new File(multiPartFile.getOriginalFilename());
				multiPartFile.transferTo(file);
				System.out.println("file name: " + file.getName());
				// FileCopy fc = new FileCopy();
				Song song = null;
				// boolean flag = fc.copyFileUsingFileStreams(file);
				ID3Extractor id3 = new ID3Extractor();
				song = id3.readTags(file.getAbsolutePath());
				if (song == null) {
					mav.addObject("message",
							"Cant read any tags on the given file. Please provide title and artist instead.");
					System.out.println("cant read any tags on the given file");
				} else {

					// validate the information via net
					// Song songResult = new Song();
					// TrackSearcher ts = new TrackSearcher();
					// songResult = ts.search(song);

					// extracting lyrics to the database
					LyricsExtractor le = new LyricsExtractor();
					List<String> lyrics = null;
					lyrics = le.getSongLyrics(song.getArtistName(), song.getSongTitle());
					if (lyrics == null) {
						// validate the information via net
						System.out.println("cant get lyrics on the given artist and title.");
						Song songResult = new Song();
						TrackSearcher ts = new TrackSearcher();
						songResult = ts.search(song);
						lyrics = le.getSongLyrics(song.getArtistName(), song.getSongTitle());
					}

					System.out.println(lyrics);

					mav.addObject("artistName", song.getArtistName());
					mav.addObject("songTitle", song.getSongTitle());
					request.getSession().setAttribute("song", song);
					request.getSession().setAttribute("lyrics", lyrics);
					request.getSession().setAttribute("file", file);
				}
			} else {
				System.out.println("Saving song to the server...");
				// save the artist to the database
				Artist artist = new Artist();
				artist.setArtistName(artistName);
				as.addArtist(artist);

				// add the file, lyrics and artist to song object
				Song song = (Song) request.getSession().getAttribute("song");
				song.setArtist(artist);
				song.setLyrics((List) request.getSession().getAttribute("lyrics"));
				song.setFile((File) request.getSession().getAttribute("file"));
				// save song to the database
				ss.addSong(song);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public ModelAndView searchSong(@RequestParam(value = "input") String searchWord, HttpServletRequest request,
			HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("index");
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<Song> songList = new ArrayList<Song>();

		long searchId = searchWordService.checkSearchedString(searchWord);
		if (searchId != 0) {
			if (searchResultService.getSearchResult(searchId) == null || searchResultService.getSearchResult(searchId).isEmpty()) {
				songList = processSearchString(searchWord, searchId);
			} else {
				List<Long> songIds = searchResultService.getSearchResult(searchId);
				ArrayList<SearchResult> rs = (ArrayList<SearchResult>) searchResultService.getSearchResult1(searchId);
				for (Long songId : songIds) {
					songList.add(ss.getSongSearchedbyIds(songId));
				}
			}
		} else {
			searchId = searchWordService.addSearchWord(searchWord);
			songList = processSearchString(searchWord, searchId);
		}
		String offset = (String) request.getParameter("offSet");
		int size;
		if (offset != null) {
			int offsetreal = Integer.parseInt(offset);
			offsetreal = offsetreal * 10;
			// songList = ss.displaySong(offsetreal);
		} else {
			size = (int) resultT.size();
			session.setAttribute("size", size / 10);
		}
		mav.addObject("songList", songList);

		return mav;
	}

	@RequestMapping(value = "/songList", method = RequestMethod.GET)
	public ModelAndView showSongList() {
		ModelAndView mav = new ModelAndView("songList");
		// return lists of songs to the database;
		try {

			List<Song> songList = ss.getAllSongs();
			mav.addObject("songList", songList);

		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("system_message", "there is an error when fetching to the database.");
		}
		return mav;
	}

	// Association Rule
	@RequestMapping(value = "/arRecom")
	public String viewAllSongs(HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {
		AssociationRule ar = new AssociationRule();

		List<PlayedSongs> played_songs_list = playedSongsService.getPlayedSongs();

		System.out.println("AR - ADMIN CONTROLLER");

		playedSongsService.truncateOneItemSetCount();
		playedSongsService.truncateTwoItemSet();
		// playedSongsService.truncateThreeItemSet();

		ArrayList<String> track_id_list = ar.getUniqueOneItemTracks(played_songs_list);
		ArrayList<String> session_id_list = ar.getUniqueSessions(played_songs_list);
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
		ArrayList<ThreeItemSetCombo> three_item_set_combo_list = ar.getThreeItemCombo(passed_two_item_support_list,
				two_item_set_list);

		int[][] threeItemBasket = ar.getThreeItemBasket(oneItemBasket, track_id_list, three_item_set_combo_list,
				twoItemBasket, two_item_set_combo_list, session_id_list);

		ArrayList<ThreeItemSet> three_item_set_list = ar.getThreeItemSet(three_item_set_combo_list, two_item_set_list,
				threeItemBasket, session_id_list, one_item_set_count_list);

		playedSongsService.insertThreeItemSet(three_item_set_list);

		playedSongsService.truncatePlayedSongsByTime();

		List<PlayedSongs> played_song_list = playedSongsService.getPlayedSongsAsc();

		PlayedSongTimeRanking playedSongTimeRanking = new PlayedSongTimeRanking();
		ArrayList<PlayedSongsByTime> played_songs_by_time_list = playedSongTimeRanking
				.getSongTimeRanking(played_song_list);

		playedSongsService.savePlayedSongsByTime(played_songs_by_time_list);

		return "index";

	}

	@RequestMapping(value = "/getLyrics")
	public String getMostPlayedSongsByTime(HttpServletRequest request, HttpServletResponse response, ModelMap map)
			throws Exception {

		/*
		 * long songId = 65034; String genre =
		 * genreService.getGenreString(songId); System.out.println("=====GENRE:"
		 * + genre);
		 */
		return "index";
	}
	private ArrayList<Song> processSearchString(String searchWord, long searchId) {
		SearchProcess sp = new SearchProcess();
		ArrayList<SearchResult> resultT = new ArrayList<SearchResult>();
		ArrayList<SearchResult> tempResultT = new ArrayList<SearchResult>();
		ArrayList<Song> songList = new ArrayList<Song>();
		ArrayList<String> searchList = new ArrayList<String>();
		ArrayList<String> operator = new ArrayList<String>();

		if (searchWord.startsWith("\"")) {
			songList = (ArrayList<Song>) ss.getSongQ(searchWord);
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
				songList.add(ss.getSongSearchedbyIds(songId));
			}

		} else {
			resultT = processTFIDF(searchWord, null, searchId);
			searchResultService.addResult(resultT);

			List<Long> songIds = searchResultService.getSearchResult(searchId);
			for (Long songId : songIds) {
				songList.add(ss.getSongSearchedbyIds(songId));
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
