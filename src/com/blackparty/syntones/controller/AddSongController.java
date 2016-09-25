package com.blackparty.syntones.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.blackparty.syntones.core.ID3Extractor;
import com.blackparty.syntones.core.LyricsExtractor;
import com.blackparty.syntones.core.TrackSearcher;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.ArtistService;
import com.blackparty.syntones.service.PlayedSongsService;
import com.blackparty.syntones.service.SongService;

@Controller
@RequestMapping(value="/admin")
public class AddSongController {
	@Autowired
	ArtistService as;

	@Autowired
	SongService ss;

	@Autowired
	PlayedSongsService playedSongsService;
	
	@RequestMapping(value="/fetchLyrics")
	public ModelAndView fetchLyrics(@RequestParam("songTitle") String songTitle,
			@RequestParam("artistName")String artistName,HttpServletRequest request){
		ModelAndView mav = new ModelAndView("showLyrics");
		LyricsExtractor le = new LyricsExtractor();
		List<String> lyrics = null;
		try{
			lyrics = le.getSongLyrics(artistName,songTitle);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("system_message", "Exception occured while fetching lyrics");
		}
		HttpSession session = request.getSession();
		mav.addObject("artistName",artistName);
		mav.addObject("songTitle",songTitle);
		mav.addObject("system_message","fetcing lyrics successful.");
		session.setAttribute("lyrics", lyrics);
		return mav;
	}
	
	@RequestMapping(value="/checkDetails",method=RequestMethod.POST)
	public ModelAndView askDetails(@RequestParam("artistName")String artistName,
			@RequestParam("songTitle")String songTitle,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		//validates given artist and song title
		// validate the information via net
		System.out.println("Validating song details provided by the admin.");
		Song song = new Song();
		song.setArtistName(artistName);
		song.setSongTitle(songTitle);
		Song songResult = new Song();
		try{
			TrackSearcher ts = new TrackSearcher();
			songResult = ts.search(song);
			System.out.println("SONG RESULT! = "+songResult);
		}catch(Exception e){
			e.printStackTrace();
			mav.addObject("system_message", "Exception occured.");
			mav.setViewName("askDetails");
			return mav;
		}
		mav.addObject("system_message","details has been validated.");
		mav.addObject("artistName", songResult.getArtistName());
		mav.addObject("songTitle", songResult.getSongTitle());
		mav.setViewName("mp3Details");
		HttpSession session = request.getSession();
		session.setAttribute("song", songResult);
		return mav;
	}
	@RequestMapping(value="/saveSong")
	public ModelAndView saveSong(@RequestParam("artistName")String artistName,
			@RequestParam("songTitle")String songTitle,
			HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		System.out.print("Saving artist to the server...");
		// save the artist to the database
		Artist artist = new Artist();
		artist.setArtistName(artistName);
		System.out.println("saveSong().artistName "+artistName);
		as.addArtist(artist);
		// add the file, lyrics and artist to song object
		Song song = new Song();
		song.setSongTitle(songTitle);
		song.setArtistName(artistName);
		song.setLyrics((List) request.getSession().getAttribute("lyrics"));
		song.setFile((File) request.getSession().getAttribute("file"));
		System.out.print("Saving song to the server...");
		// save song to the database
		try{
//			ss.addSong(song);
		}catch(Exception e){
			e.printStackTrace();
		}
		HttpSession session = request.getSession();
		session.invalidate();
		MainController mc = new MainController();
		mav = mc.indexPage();
		mav.addObject("system_message", "Song saved successfully.");
		return mav;
	}
	@RequestMapping(value="/readMp3")
	public ModelAndView readMp3(@RequestParam(value="file")MultipartFile multiPartFile,HttpServletRequest request){
		ModelAndView mav = new ModelAndView();
		try{
			System.out.println(multiPartFile.getOriginalFilename());
			File file = new File("E:/deletables/" + multiPartFile.getOriginalFilename());
			multiPartFile.transferTo(file);
			System.out.println("file name: " + file.getName());
			// FileCopy fc = new FileCopy();
			Song song = null;
			// boolean flag = fc.copyFileUsingFileStreams(file);
			ID3Extractor id3 = new ID3Extractor();
			song = id3.readTags(file.getAbsolutePath());
			HttpSession session = request.getSession();
			if (song.getArtistName() == null) {
				mav.addObject("system_message",
						"Cant read any tags on the given file. Please provide title and artist instead.");
				mav.setViewName("askDetails");
				System.out.println("cant read any tags on the given file");
				session.setAttribute("file", file);
			} else {
				request.getSession().setAttribute("file", file);
				mav.addObject("system_message","reading on the mp3 tags is successful.");
				mav.addObject("artistName", song.getArtistName());
				mav.addObject("songTitle", song.getSongTitle());
				mav.setViewName("mp3Details");
				System.out.println("saving song and file to session.");
				System.out.println(song.displayTitleAndArtist());
				session.setAttribute("artistName", song.getArtistName());
				session.setAttribute("songTitle", song.getSongTitle());
				session.setAttribute("file", file);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mav;
	}
	@RequestMapping(value="/addSongPage")
	public ModelAndView showAddSongPage(){
		ModelAndView mav = new ModelAndView("addSong");
		return mav;
	}
	
	
	
}
