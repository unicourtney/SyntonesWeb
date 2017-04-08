package com.blackparty.syntones.controller;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.SocketTimeoutException;
//import java.rmi.UnknownHostException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.ArtistIDFProcess;
import com.blackparty.syntones.core.GenreSetter;
import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.NaiveBayes;
import com.blackparty.syntones.core.SongIDFProcess;
import com.blackparty.syntones.core.Stemmer;
import com.blackparty.syntones.core.Summarize;
import com.blackparty.syntones.core.Tagger;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.ArtistWordBank;
import com.blackparty.syntones.model.DataSetMood;
import com.blackparty.syntones.model.DataSetWord;
import com.blackparty.syntones.model.Genre;
import com.blackparty.syntones.model.IDFReturn;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.SongLine;
import com.blackparty.syntones.model.SongWordBank;
import com.blackparty.syntones.model.Tag;
import com.blackparty.syntones.model.TagSong;
import com.blackparty.syntones.model.TagSynonym;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.ArtistWordBankService;
import com.blackparty.syntones.service.DataSetConditionalProbabilityService;
import com.blackparty.syntones.service.DataSetMoodService;
import com.blackparty.syntones.service.DataSetWordBankService;
import com.blackparty.syntones.service.GenreService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SongLineService;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.SongWordBankService;
import com.blackparty.syntones.service.StopWordService;
import com.blackparty.syntones.service.TagService;
import com.blackparty.syntones.service.TagSongService;
import com.blackparty.syntones.service.TagSynonymService;
import com.blackparty.syntones.service.WordBankService;

@Controller
@Component
@Scope("session")
@RequestMapping(value = "/admin")
public class AddSongController {
	@Autowired
	private ArtistService as;
	@Autowired
	private SongService ss;
	@Autowired
	private PlayedSongsService playedSongsService;
	@Autowired
	private SongLineService songLineService;
	@Autowired
	private TagSynonymService tagSynonymService;
	@Autowired
	private TagSongService tagSongService;
	@Autowired
	private SongService songService;
	@Autowired
	private TagService tagService;
	@Autowired
	private SongWordBankService swService;
	@Autowired
	private ArtistWordBankService awService;
	@Autowired
	private WordBankService wordBankService;

	@Autowired
	private DataSetWordBankService dsWordBankService;

	@Autowired
	private DataSetConditionalProbabilityService dsConditionalService;

	@Autowired
	private DataSetMoodService dsm;

	@Autowired
	private StopWordService stopWordService;

	@Autowired
	private GenreService genreService;


	@RequestMapping(value = "/fetchLyrics")
	public ModelAndView fetchLyrics(@RequestParam("songTitle") String songTitle,
			@RequestParam("artistName") String artistName, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView("showLyrics");
		LyricsExtractor le = new LyricsExtractor();
		List<String> lyrics = null;
		try {
			lyrics = le.getSongLyrics(artistName, songTitle);
		} catch (SocketTimeoutException timeout) {
			mav.addObject("system_message", "timeoue exception, please click \"read\" again.");
			mav.setViewName("mp3Details");
			mav.addObject("artistName", artistName);
			mav.addObject("songTitle", songTitle);
			return mav;
		} catch (Exception e) {
			e.printStackTrace();
			mav.addObject("system_message",
					"Cant find lyrics due to unknown artist / title. Please provide correct song details");
			mav.setViewName("askDetails");
			return mav;
		}

		mav.addObject("artistName", artistName);
		mav.addObject("songTitle", songTitle);
		mav.addObject("system_message", "fetcing lyrics successful.");
//		request.setAttribute("lyrics", lyrics);
		return mav;
	}

	@RequestMapping(value = "/checkDetails", method = RequestMethod.POST)
	public ModelAndView askDetails(@RequestParam("artistName") String artistName,
			@RequestParam("songTitle") String songTitle,  @RequestParam("luriks") List<String> luriks, @RequestParam("leFile") File leFile, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		// validates given artist and song title
		// validate the information via net
		System.out.println("Validating song details provided by the admin.");
		Song song = new Song();
		song.setArtistName(artistName);
		song.setSongTitle(songTitle);
		song.setLyrics(luriks);
		Song songResult = new Song();
		try {
			TrackSearcher ts = new TrackSearcher();
			songResult = ts.search(song);
			if (songResult != null) {
				System.out.println("SONG RESULT! = " + songResult);
				mav.addObject("succ_message", "Details has been validated.");
				mav.addObject("artistName", songResult.getArtistName());
				mav.addObject("songTitle", songResult.getSongTitle());
				mav.setViewName("mp3Details");

				// fetch lyrics
				System.out.println("i'm here");
				request.getSession().setAttribute("file", request.getSession().getAttribute("file"));
				System.out.println("Reading tags successful.");
				mav.addObject("succ_message", "Reading on the mp3 tags is successful.");
				mav.addObject("artistName", song.getArtistName());
				mav.addObject("songTitle", song.getSongTitle());
				LyricsExtractor lyricsExtractor = new LyricsExtractor();
				List<String> lyrics = lyricsExtractor.getSongLyrics(song.getArtistName(), song.getSongTitle());
				System.out.println("saving song and file to session.");
				System.out.println(song.displayTitleAndArtist());
//				request.setAttribute("lyrics", lyrics);

			} else {
				mav.addObject("artistName", artistName);
				mav.addObject("songTitle", songTitle);
				mav.addObject("err_message", "An error occured, click \"save\" ");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
			mav.addObject("err_message", "Exception occured.\nPlease enter song lyrics manually.");
			mav.setViewName("showLyrics");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mav.addObject("err_message", "Exception occured.\nPlease enter song lyrics manually.");
			mav.setViewName("showLyrics");
		}
		return mav;
	}

	@RequestMapping(value = "/saveSong")
	public ModelAndView saveSong(@RequestParam("artistName") String artistName,
			@RequestParam("songTitle") String songTitle, @RequestParam("lereks") List<String> lereks, @RequestParam("leFile") File leFile,  HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		System.out.print("Saving artist to the server...");
//		session = request.getSession();
		System.out.println("LEREKS:" + lereks);
	    File mahFile = new File(request.getParameter("leFile"));
		// System.out.println("======================================FOILE: " +
		// mahFile.getAbsolutePath());
		// save the artist to the database
		Artist artist = new Artist();
		artist.setArtistName(artistName);
		System.out.println("saveSong().artistName " + artistName);
		as.addArtist(artist);
		// add the file, lyrics and artist to song object
		Song song = new Song();
		song.setSongTitle(songTitle);
		song.setArtistName(artistName);

		try {
			
			// getGenre of the song
			GenreSetter ms = new GenreSetter();
			song = ms.getGenre(song);

//			List<String> lyrics = (List) session.getAttribute("lyrics");
		
			if (request.getParameter("lereks") != null) {
				song.setLyrics(lereks);

				song.setFlag(true);
			} else {
				ArrayList<String> lyrics1 = new ArrayList();
				song.setFlag(false);
		
				song.setLyrics(lereks);
			}
			song.setFile((File) mahFile);
			System.out.print("Saving song to the server...");


			// get mood for the song
			NaiveBayes nb = new NaiveBayes();
			List<DataSetWord> dataSetWordList = dsWordBankService.getAll();
			List<DataSetMood> dataSetMoodList = dsm.getAllMood();
			dataSetMoodList = dsConditionalService.setMood(dataSetMoodList);
			List<String> stopWordList = stopWordService.getStopWords();

			Map.Entry<String, Double> result = nb.classify(song, dataSetWordList, dataSetMoodList, stopWordList);
			song.setMood(result.getKey());

			// save song to the database

			long songId = ss.addSong(song);
			song.setSongId(songId);

			// save genre if not yet exists on the db
			Genre genre = song.getGenre();
			genreService.saveGenre(genre);

			
			//Update Wordbank
			List<String> stopwords = stopWordService.getStopWords();
			song = ss.getSong(songId);
			SongIDFProcess sip = new SongIDFProcess();
			String songSL = song.getSongTitle() + "\n" + song.getLyrics() + "\n" + song.getArtist().getArtistName();
			StringTokenizer str = new StringTokenizer(songSL, "  \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?");
			while (str.hasMoreTokens()) {
				String token = str.nextToken().toLowerCase().trim();
				if (!token.equalsIgnoreCase("")) {
					//String baseform = stem.stem(token).trim();
					if (!sip.isStopWord(token, stopwords)) {
						if (!token.equalsIgnoreCase(null)) {
							swService.updateWord(token, song.getSongId(),
									sip.titleWeight(song.getSongTitle(), token), sip.artistWeight(song.getArtist().getArtistName(), token));
						}
					}
				}
			}
		
			wordBankService.processIDF(ss.songCount());
			swService.setStep3();
			
			//end update wordbank
			
			
//			request.getSession().invalidate();
			File deletables = new File("E:/deletables/");
			File listAllFiles[] = deletables.listFiles();
			if (listAllFiles != null && listAllFiles.length > 0) {
				for (File currentFile : listAllFiles) {
					if (currentFile.isDirectory()) {

					} else {
						if (currentFile.getName().endsWith(".mp3")) {

							File getFileDir = currentFile.getAbsoluteFile();
							getFileDir.delete();

						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		// HttpSession session = request.getSession();

		session = request.getSession(true);
		String offset = (String) request.getParameter("offSet");
		int size;
		List<Song> songList;
		if (offset != null) {
			int offsetreal = Integer.parseInt(offset);
			offsetreal = offsetreal * 10;
			songList = songService.displaySong(offsetreal);
		} else {
			songList = songService.displaySong(0);
			size = (int) songService.songCount();
			session.setAttribute("size", size / 10);
		}
		mav.addObject("songList", songList);
		System.out.println("song saved successfully.");
		mav.setViewName("index");
		mav.addObject("succ_message", "Song saved successfully.");
		return mav;
	}

	@RequestMapping(value = "/readMp3")
	public ModelAndView readMp3(@RequestParam(value = "file") MultipartFile multiPartFile, HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		try {
			System.out.println(multiPartFile.getOriginalFilename());
			File file = new File("E:/deletables/" + multiPartFile.getOriginalFilename());
			multiPartFile.transferTo(file);
			System.out.println("file name: " + file.getName());
			// FileCopy fc = new FileCopy();
			Song song = null;
			// boolean flag = fc.copyFileUsingFileStreams(file);
			ID3Extractor id3 = new ID3Extractor();
			song = id3.readTags(file.getAbsolutePath());
//			HttpSession session = request.getSession();
			if (song.getArtistName() == null) {
				mav.addObject("err_message",
						"Cant read any tags on the given file. Please provide title and artist instead.");
				mav.setViewName("askDetails");
				System.out.println("cant read any tags on the given file");
				request.getSession().setAttribute("file", file);

			} else {

				// fetch lyrics
				request.getSession().setAttribute("file", file);
				System.out.println("Reading tags successful.");
				mav.addObject("succ_message", "Reading on the mp3 tags is successful.");
				mav.addObject("artistName", song.getArtistName());
				mav.addObject("songTitle", song.getSongTitle());
				LyricsExtractor lyricsExtractor = new LyricsExtractor();
				List<String> lyrics = lyricsExtractor.getSongLyrics(song.getArtistName(), song.getSongTitle());
				System.out.println("saving song and file to session.");
				System.out.println(song.displayTitleAndArtist());
				request.getSession().setAttribute("lyrics", lyrics);

				
				mav.setViewName("mp3Details");
			}
		} catch (HttpStatusException e) {
			System.out.println("Attempting to get lyrics to the internet had failed.");
			mav.addObject("system_message", "Attempting to get lyrics to the internet had failed.");
			mav.setViewName("askDetails");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			mav.setViewName("askDetails");
		} catch (Exception e) {
			e.printStackTrace();
			mav.setViewName("askDetails");
		}
		return mav;
	}

	@RequestMapping(value = "/viewLyrics")
	public ModelAndView viewSong(@RequestParam("songId") long songId) {
		ModelAndView mav = new ModelAndView("viewLyrics");
		try {
			Song song = songService.getSong(songId);
			mav.addObject("song", song);
		} catch (Exception e) {
			System.out.println("something went wrong!");
			e.printStackTrace();
		}
		return mav;
	}

	@RequestMapping(value = "/addSongPage")
	public ModelAndView showAddSongPage() {
		ModelAndView mav = new ModelAndView("addSong");
		return mav;
	}

	@RequestMapping(value = "/edit")
	public ModelAndView updateSong(@RequestParam("songId") long songId) throws Exception {
		ModelAndView mav = new ModelAndView("updateSong");
		Song song = songService.getSong(songId);
		mav.addObject("song", song);
		return mav;
	}

	@RequestMapping(value = "/updateSong")
	public ModelAndView saveUpdatedSong(@RequestParam("songId") long songId, @RequestParam("lyrics") String lyrics,
			HttpServletRequest request, HttpSession session) throws Exception {
		ModelAndView mav = new ModelAndView("index");
		try {
			// word bank process
			/*
			 * List<Song> songs = ss.getAllSongs(); if (!songs.isEmpty()) {
			 * SongWordBankProcess swb = new SongWordBankProcess();
			 * TemporaryModel tm = swb.WBSongProcess((ArrayList<Song>) songs);
			 * songs = tm.getSongs(); List<SongWordBank> words = tm.getWords();
			 * 
			 * ss.updateBatchAllSongs(songs); sservice.updateWordBank(words); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		//
		// HttpSession session = request.getSession();
		// session.invalidate();
		String offset = (String) request.getParameter("offSet");
		int size;
		List<Song> songList;
		if (offset != null) {
			int offsetreal = Integer.parseInt(offset);
			offsetreal = offsetreal * 10;
			songList = songService.displaySong(offsetreal);
		} else {
			songList = songService.displaySong(0);
			size = (int) songService.songCount();
			session.setAttribute("size", size / 10);
		}
		mav.addObject("songList", songList);
		System.out.println("song saved successfully.");
		mav.setViewName("index");
		mav.addObject("succ_message", "Song saved successfully.");

		return mav;
	}
	@RequestMapping(value = "/updateWordBank")
	public ModelAndView updateWordBank(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView("index");
		// word bank process
		List<BigInteger> songIds = ss.getIdToUpdate();
		List<String> stopwords = stopWordService.getStopWords();
		try {
			for (BigInteger songId :songIds) {// song.getSongId()
													// swService.checkSongId(song.getSongId()
				Song song = ss.getSong(songId.longValue());
				SongIDFProcess sip = new SongIDFProcess();
				String songSL = song.getSongTitle() + "\n" + song.getLyrics() + "\n" + song.getArtist().getArtistName();
				StringTokenizer str = new StringTokenizer(songSL, "  \n`~!@#$ %^&*()-_=+[]\\{}|;:\",./<>?");
				while (str.hasMoreTokens()) {
					String token = str.nextToken().toLowerCase().trim();
					if (!token.equalsIgnoreCase("")) {
						//String baseform = stem.stem(token).trim();
						if (!sip.isStopWord(token, stopwords)) {
							if (!token.equalsIgnoreCase(null)) {
								swService.updateWord(token, song.getSongId(),
										sip.titleWeight(song.getSongTitle(), token), sip.artistWeight(song.getArtist().getArtistName(), token));
							}
						}
					}
				}
			}
			wordBankService.processIDF(ss.songCount());
			swService.setStep3();
			// sip.SongIdfProcess(songService.getLastUploaded(), songCount);

			System.out.println("Song Done... Artist done...");
			String offset = (String) request.getParameter("offSet");
			int size;
			List<Song> songList;
			if (offset != null) {
				int offsetreal = Integer.parseInt(offset);
				offsetreal = offsetreal * 10;
				songList = ss.displaySong(offsetreal);
			} else {
				songList = ss.displaySong(0);
				size = (int) ss.songCount();
				session.setAttribute("size", size / 10);
			}
			mav.addObject("songList", songList);
			mav.setViewName("index");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mav;
	}

}

