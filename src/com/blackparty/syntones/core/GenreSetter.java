package com.blackparty.syntones.core;

import java.util.Map;

import com.blackparty.syntones.model.Genre;
import com.blackparty.syntones.model.Song;

import radams.gracenote.webapi.GracenoteMetadata;
import radams.gracenote.webapi.GracenoteWebAPI;

public class GenreSetter {
	private static String clientID = "837982737"; // Put your clientID here.
    private static String clientTag = "8796C33F5BFFEB72CC6A759EF430A16A"; // Put your clientTag here.
	private GracenoteWebAPI api;
	public GenreSetter(){

		try{
			 api = new GracenoteWebAPI(clientID, clientTag);
		}catch(Exception e){
			e.printStackTrace();
			
		}
		
		
	}
	
	public Song getGenre(Song song){
		String artistName = song.getArtistName();
		GracenoteMetadata results = api.searchArtist(song.getArtistName());
		Map<String,String> genreMap = results.getGenre();
		for(String s:genreMap.keySet()){
			long genreId = Long.parseLong(s);
			Genre genre = new Genre(genreId,genreMap.get(s));
			song.setGenre(genre);
			song.setGenreId(genreId);
			break;
		}
		
		return song;
	}
}
