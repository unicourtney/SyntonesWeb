package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlayedSongsDAO;
import com.blackparty.syntones.DAO.UserDAO;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.PlayedSongsByTime;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TemporaryDB;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.TwoItemSet;

@Service
public class PlayedSongsService {
	@Autowired
	private PlayedSongsDAO playedSongsDAO;

	// INSERTS

	
	public List<PlayedSongs> getUserPlayedSongs(long userId,String partOfDay) throws Exception{
		return playedSongsDAO.getUserPlayedSongs(userId,partOfDay);
	}
	
	public  int getNumberOfSessions(long userId)throws Exception {
		return playedSongsDAO.getNumberOfSessions(userId);
	}
	
	public void savePlayedSongsByTime(ArrayList<PlayedSongsByTime> playedSongsByTime) {
		playedSongsDAO.savePlayedSongByTime(playedSongsByTime);
	}

	public void saveTemporaryDB(TemporaryDB temporaryDB) {
		playedSongsDAO.saveTemporaryDB(temporaryDB);
	}

	public void savePlayedSongs(PlayedSongs playedSongs) {

		playedSongsDAO.savePlayedSongs(playedSongs);

	}

	public void insertOneItemSetCount(ArrayList<OneItemSetCount> one_item_set_count_list) {
		playedSongsDAO.insertOneItemSetCount(one_item_set_count_list);
	}

	public void insertTwoItemSet(ArrayList<TwoItemSet> two_item_set_list) {
		playedSongsDAO.insertTwoItemSet(two_item_set_list);
	}

	public void insertThreeItemSet(ArrayList<ThreeItemSet> three_item_set_list) {
		playedSongsDAO.insertThreeItemSet(three_item_set_list);
	}

	// FETCHES

	public List<PlayedSongsByTime> getPlayedSongsByTime() {
		return playedSongsDAO.getPlayedSongsByTime();
	}

	public List<PlayedSongs> getPlayedSongsAsc() {
		return playedSongsDAO.getPlayedSongsAsc();
	}

	public List<TemporaryDB> getTemporaryDB() {
		return playedSongsDAO.getTemporaryDB();
	}

	public List<PlayedSongs> getPlayedSongs() {

		return playedSongsDAO.getPlayedSongs();
	}

	public List<Long> getTwoItemSet(String songId) {
		return playedSongsDAO.getTwoItemSet(songId);
	}

	public List<Long> getThreeItemSet(String songId) {
		return playedSongsDAO.getThreeItemSet(songId);
	}

	public List<OneItemSetCount> getOneItemSetCount() {
		return playedSongsDAO.getOneItemSetCount();
	}

	public boolean checkIfPlayedSongExists(long session_id, String song_id) {
		return playedSongsDAO.checkIfPlayedSongExists(session_id, song_id);
	}
	
	public List<Song> getSongs(List<Long> songIds){
		return playedSongsDAO.getSongs(songIds);
	}
	
	public void getRecommendation(String songId){
		playedSongsDAO.getRecommendation(songId);
	}

	public void getRecommendation2(String songId){
		playedSongsDAO.getRecommendation2(songId);
	}
	// DELETES

	public void truncatePlayedSongsByTime() {
		playedSongsDAO.truncatePlayedSongsByTime();
	}

	public void truncateTemporaryDB() {
		playedSongsDAO.truncateTemporaryDB();
	}

	public void truncateOneItemSetCount() {
		playedSongsDAO.truncateOneSetItemCount();
	}

	public void truncateTwoItemSet() {
		playedSongsDAO.truncateTwoItemSet();
	}

	public void truncateThreeItemSet() {
		playedSongsDAO.truncateThreeItemSet();
	}

}
