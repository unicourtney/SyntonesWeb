package com.blackparty.syntones.DAO;

import java.util.List;
import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.core.AssociationRule;
import com.blackparty.syntones.model.OneItemSet;
import com.blackparty.syntones.model.OneItemSetCount;
import com.blackparty.syntones.model.PlayedSongs;
import com.blackparty.syntones.model.PlayedSongsByTime;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.TemporaryDB;
import com.blackparty.syntones.model.ThreeItemSet;
import com.blackparty.syntones.model.TwoItemSet;
import com.blackparty.syntones.service.PlayedSongsService;

@Repository
@Transactional
public class PlayedSongsDAO {
	@Autowired
	private SessionFactory sf;
	@Autowired
	private PlayedSongsService playedSongsService;

	// INSERTS

	public int getNumberOfSessions(long userId) throws Exception {
		String sql = "select distinct session_id from played_songs_tbl where user_id =:name";
		Session session = sf.openSession();
		Query query = session.createSQLQuery(sql);
		query.setLong("name", userId);
		int numberOfSessions = query.list().size();
		session.clear();
		session.flush();
		session.close();
		return numberOfSessions;
	}

	public List<PlayedSongs> getUserPlayedSongs(long userId, String partOfDay) throws Exception {
		String sql = "from PlayedSongs where user_id =:id and part_of_day =:day";
		Session session = sf.openSession();
		Query query = session.createQuery(sql);
		query.setLong("id", userId);
		query.setString("day", partOfDay);
		List<PlayedSongs> songList = query.list();
		session.clear();
		session.flush();
		session.close();

		return songList;

	}

	public void savePlayedSongByTime(ArrayList<PlayedSongsByTime> playedSongsByTime) {
		Session session = sf.openSession();

		for (PlayedSongsByTime a : playedSongsByTime) {
			a.getTrack_id();
			a.getMorning();
			a.getNoon();
			a.getAfternoon();
			a.getEvening();
			a.getMidnight();
			session.save(a);
			session.flush();
			session.clear();
		}

		session.close();

	}

	public void saveTemporaryDB(TemporaryDB temporaryDB) {
		Session session = sf.openSession();

		temporaryDB.getSong_id();
		temporaryDB.getSession_id();
		temporaryDB.getDate();
		temporaryDB.getUser_id();
		temporaryDB.getPart_of_day();
		session.save(temporaryDB);
		session.flush();
		session.clear();
		session.close();

	}

	public void savePlayedSongs(PlayedSongs playedSongs) {
		Session session = sf.openSession();

		playedSongs.getTrack_id();
		playedSongs.getSession_id();
		playedSongs.getDate();
		playedSongs.getUser_id();
		playedSongs.getPart_of_day();
		session.save(playedSongs);
		session.flush();
		session.clear();
		session.close();
	}

	public void insertOneItemSetCount(ArrayList<OneItemSetCount> one_item_set_count_list) {

		Session session = sf.openSession();

		for (OneItemSetCount a : one_item_set_count_list) {

			a.getTrack_id();
			a.getCount();
			a.getSupport();
			session.save(a);

			session.flush();
			session.clear();
		}

		session.close();

	}

	public void insertTwoItemSet(ArrayList<TwoItemSet> two_item_set_list) {

		Session session = sf.openSession();

		for (TwoItemSet a : two_item_set_list) {

			a.getTrack_id();
			a.getCount();
			a.getSupport();
			a.getConfidence();
			a.getRecom_song();

			session.save(a);

			session.flush();
			session.clear();
		}
		session.close();

	}

	public void insertThreeItemSet(ArrayList<ThreeItemSet> three_item_set_list) {

		Session session = sf.openSession();

		for (ThreeItemSet a : three_item_set_list) {

			a.getTrack_id();
			a.getCount();
			a.getSupport();
			a.getConfidence();
			a.getRecom_song();

			session.save(a);

			session.flush();
			session.clear();
		}
		session.close();

	}

	// FETCHES

	public List<PlayedSongsByTime> getPlayedSongsByTime() {
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongsByTime");
		List<PlayedSongsByTime> played_songs_by_time = query.list();
		session.flush();
		session.clear();
		session.close();
		return played_songs_by_time;

	}

	public List<PlayedSongs> getPlayedSongsAsc() {
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs ORDER BY track_id ASC");
		List<PlayedSongs> played_songs_list = query.list();
		session.flush();
		session.clear();
		session.close();
		return played_songs_list;

	}

	public List<TemporaryDB> getTemporaryDB() {
		Session session = sf.openSession();
		Query query = session.createQuery("from TemporaryDB");
		List<TemporaryDB> temporary_db_list = query.list();
		session.flush();
		session.clear();
		session.close();
		return temporary_db_list;
	}

	public List<PlayedSongs> getPlayedSongs() {
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs");
		List<PlayedSongs> played_songs_lists = query.list();
		session.flush();
		session.clear();
		session.close();
		return played_songs_lists;

	}

	public List<Long> getTwoItemSet(String songId) {
		AssociationRule associationRule = new AssociationRule();
		int threshold = associationRule.getThreshold();
		Session session = sf.openSession();
		Query query = session.createQuery(
				"Select distinct(recom_song) from TwoItemSet where track_id LIKE:songId AND confidence >=:threshold");
		query.setString("songId", songId + "%");
		query.setInteger("threshold", threshold);

		List<Long> two_item_set_list = query.list();

		session.flush();
		session.clear();
		session.close();

		return two_item_set_list;
	}

	public void getRecommendation(String songId) {
		AssociationRule associationRule = new AssociationRule();
		int threshold = associationRule.getThreshold();
		Session session = sf.openSession();
		Query query = session
				.createQuery("select distinct(recom_song) from TwoItemSet where track_id LIKE:songId AND confidence >=:threshold");
		query.setString("songId", songId + "%");
		query.setInteger("threshold", threshold);

		List<TwoItemSet> two_item_set_list = query.list();

		for (TwoItemSet a : two_item_set_list) {
			System.out.println("Song Id: " + a.getRecom_song() + " - Confidence: " + a.getConfidence());
		}
		session.flush();
		session.clear();
		session.close();
	}
	
	public void getRecommendation2(String songId) {
		AssociationRule associationRule = new AssociationRule();
		int threshold = associationRule.getThreshold();
		Session session = sf.openSession();
		Query query = session
				.createQuery("select distinct(recom_song) from ThreeItemSet where track_id LIKE:songId AND confidence >=:threshold");
		query.setString("songId", songId + "%");
		query.setInteger("threshold", threshold);

		List<ThreeItemSet> three_item_set_list = query.list();

		for (ThreeItemSet a : three_item_set_list) {
			System.out.println("Song Id: " + a.getRecom_song() + " - Confidence: " + a.getConfidence());
		}
		session.flush();
		session.clear();
		session.close();
	}

	public List<Long> getThreeItemSet(String songId) {
		AssociationRule associationRule = new AssociationRule();
		int threshold = associationRule.getThreshold();
		Session session = sf.openSession();

		Query query = session.createQuery(
				"select distinct(recom_song) from ThreeItemSet where track_id LIKE:songId AND confidence >=:threshold");
		query.setString("songId", songId + "%");
		query.setInteger("threshold", threshold);
		List<Long> three_item_set_list = query.list();

		session.flush();
		session.clear();
		session.close();
		return three_item_set_list;

	}

	public List<OneItemSetCount> getOneItemSetCount() {

		Session session = sf.openSession();
		Query query = session.createQuery("from OneItemSetCount");
		List<OneItemSetCount> one_item_set_count_list = query.list();
		session.flush();
		session.clear();
		session.close();
		return one_item_set_count_list;
	}

	public boolean checkIfPlayedSongExists(long session_id, String song_id) {
		boolean songExists;
		Session session = sf.openSession();
		Query query = session.createQuery("from PlayedSongs where session_id = :session_id AND track_id = :song_id");
		query.setLong("session_id", session_id);
		query.setString("song_id", song_id);

		PlayedSongs playedSongs = (PlayedSongs) query.uniqueResult();
		if (playedSongs != null) {
			songExists = true;
		} else {
			songExists = false;
		}

		session.flush();
		session.clear();
		session.close();

		return songExists;
	}

	public List<Song> getSongs(List<Long> songIds) {
		String sql = "from Song where songId =:id";
		List<Song> songList = new ArrayList<>();
		for (Long s : songIds) {
			Session session = sf.openSession();
			Query query = session.createQuery(sql);
			query.setLong("id", s);
			Song song = (Song) query.uniqueResult();
			songList.add(song);
			session.clear();
			session.flush();
			session.close();
		}
		return songList;
	}

	// DELETES

	public void truncatePlayedSongsByTime() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from PlayedSongsByTime");
		query.executeUpdate();
		session.flush();
		session.clear();
		session.close();
	}

	public void truncateTemporaryDB() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from TemporaryDB");
		query.executeUpdate();
		session.flush();
		session.clear();
		session.close();
	}

	public void truncateOneSetItemCount() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from OneItemSetCount");
		query.executeUpdate();
		session.flush();
		session.clear();
		session.close();
	}

	public void truncateTwoItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from TwoItemSet");
		query.executeUpdate();
		session.flush();
		session.clear();
		session.close();
	}

	public void truncateThreeItemSet() {

		Session session = sf.openSession();
		Query query = session.createQuery("delete from ThreeItemSet");
		query.executeUpdate();
		session.flush();
		session.clear();
		session.close();
	}

}
