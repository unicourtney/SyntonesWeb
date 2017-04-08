package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.io.File;
import java.math.BigInteger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SongDAO;

import com.blackparty.syntones.model.SearchModel;

import com.blackparty.syntones.model.Artist;

import com.blackparty.syntones.model.Song;

@Service
public class SongService {
	@Autowired
	private SongDAO songDao;

	public long addSong(Song song) throws Exception {
		return songDao.addSong(song);
	}

	public List<Song> getAllSongs(List<Long> songIds) throws Exception {
		return songDao.getSongs(songIds);
	}

	public List<Song> getAllSongsFromDb() {
		return songDao.getAllSongsFromDb();
	}

	public List<Long> getAllSongsByArtist(Artist artist) throws Exception {
		return songDao.getAllSongByArtist(artist);

	}

	public List<Song> getAllSongs() throws Exception {
		List<Song> songList = songDao.getAllSongs();
		return songList;
	}

	public Song getSong(long songId) throws Exception {
		return songDao.getSong(songId);
	}

	public void updateBatchAllSongs(List<Song> songs) throws Exception {
		songDao.updateBatchAllSongs(songs);
	}

	public ArrayList<Song> getSongs(ArrayList<SearchModel> model) {
		return songDao.getSongs(model);
	}

	public List<Song> displaySong(int firstResult) {
		return songDao.displaySong(firstResult);
	}

	public long songCount() {
		return songDao.songCount();
	}

	public String getLyrics(long songId) {
		return songDao.getLyrics(songId);
	}

	public List<BigInteger> getIdToUpdate() {
		return songDao.getIdToUpdate();
	}

	public Song getSongSearched(Long songId) {
		return songDao.getSongSearched(songId);
	}
	
	public List<Song> getSongQ(String searchedWord){
		return songDao.getSongQ(searchedWord);
	}
	
	public Song getSongSearchedbyIds(Long songIds){
		return songDao.getSongSearchedbyIds(songIds);
	}
	public List<Long> checkSong(String searchedWord){
		return songDao.checkSong(searchedWord);
	}
}
