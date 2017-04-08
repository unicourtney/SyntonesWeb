  package com.blackparty.syntones.DAO;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.type.LongType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.core.Mp3Uploader;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.SearchModel;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.service.ArtistService;

@Repository
@Transactional
public class SongDAO {
	@Autowired
	private SessionFactory sf;
	@Autowired
	private ArtistService as;

	public List<Long> getAllSongByArtist(Artist artist) throws Exception {
		Session session = sf.openSession();
		Query query = session.createSQLQuery("select song_id from song_tbl where artist_artist_id = :id")
				.addScalar("song_id", LongType.INSTANCE);
		query.setLong("id", artist.getArtistId());
		System.out.println("=======Get All Songs By Artist");
		List<Long> list = query.list();
		session.flush();
		session.clear();
		session.close();
		return list;
	}

	public long addSong(Song song) throws Exception {
		Session session = sf.openSession();
		Artist fetchedArtist = as.getArtist(song.getArtistName());

		song.setArtist(fetchedArtist);

		long songId = (Long) session.save(song);
		session.flush();
		session.close();
		System.out.println("======================= ADD SONG");
		// call mp3uploader to save a copy of the mp3 on the server side
		long artistId = song.getArtist().getArtistId();
		Mp3Uploader uploader = new Mp3Uploader();
		System.out.println("=====================" + song.getFilePath() + "\nSONG ID: " + songId + "\nARTIST ID: " + artistId);
		String file = uploader.upload(song.getFile(), songId, artistId);
		song.getFile().delete();
		updateSong(songId, file);

		return songId;
	}

	public List<Song> getAllSongsFromDb() {
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
		System.out.println("=======Get All Songs from DB");
		List<Song> song_list = query.list();
		session.flush();
		session.clear();
		session.close();
		return song_list;

	}

	public Song getSong(long songId) throws Exception {
		Session session = sf.openSession();
		System.out.println("Querying song for :" + songId);
		Query q = session.createQuery("from Song where song_id =:id");
		q.setLong("id", songId);
		System.out.println("=======Get Song");
		Song song = (Song) q.uniqueResult();
		session.flush();
		session.clear();
		session.close();
		return song;
	}

	public List<Song> getAllSongs() throws Exception {
		Session session = sf.openSession();
		Query q = session.createQuery("from Song");
		//q.setMaxResults(1);
		List<Song> songList = q.list();
		session.flush();
		session.close();
		for(Song s:songList){
			s.setArtist(as.getArtist(s.getArtist().getArtistId()));
		}
		int i=0;
//		for(Song s:songList){
//			System.out.println(++i+" "+s.toString());
//		}
		return songList;
	}

	public ArrayList<Song> fetchAllSong() throws Exception {
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
		System.out.println("=======Fetch All Song");
		@SuppressWarnings("unchecked")
		ArrayList<Song> songs = (ArrayList<Song>) query.list();

		session.flush();
		session.clear();
		session.close();
 
		return songs;

	}

	public void updateBatchAllSongs(List<Song> songs) throws Exception {
		StatelessSession session = sf.openStatelessSession();
		Transaction trans = session.beginTransaction();
		System.out.println("=======Update Batch All Songs");
		for (Song s : songs) {
			session.update(s);
		}
		trans.commit();
		session.close();
	}

	
	public List<Song> getSongs(List<Long> songIds)throws Exception{
		String sql = "from Song where songId =:id";
		List<Song> songList = new ArrayList<>();
		for(Long s:songIds){
			Session session = sf.openSession();
			Query query = session.createQuery(sql);
			query.setLong("id", s);
			Song song = (Song)query.uniqueResult();
			songList.add(song);
			session.clear();
			session.flush();
			session.close();
		}
		return songList;
	}
	
	public ArrayList<Song> getSongs(ArrayList<SearchModel> sm) {
		Session session = sf.openSession();
		System.out.println("=======Get Songs ARRAYLIST");
		ArrayList<Song> songs = new ArrayList();
		for (SearchModel model : sm) {
			Query query = session.createQuery("from Song where songId=:id");
			query.setLong("id", model.getSongId());
			Song song = (Song) query.uniqueResult();
			songs.add(song);
		}
		session.close();
		return songs;
	}

	public void updateSong(long songId, String file) {
		Session session = sf.openSession();
		Query query = session.createQuery("from Song where songId=:id");
		query.setLong("id", songId);
		System.out.println("=======Update Song");
		Song song = (Song) query.uniqueResult();
		song.setFilePath("/songUploaded/" + file);
		session.update(song);
		session.flush();
		session.clear();
		session.close();

	}

	public List<Song> displaySong(int firstResult) {
		System.out.println("=======Display Song");
		Session session = sf.openSession();
		Query query = session.createQuery("from Song");
/*		query.setFirstResult(firstResult);
		query.setMaxResults(10);*/

		List<Song> songs = query.list();
		session.flush();
		session.clear();
		session.close();
		return songs;
	}

	public List<Song> getSongbyArtist(List<Artist> artists) {
		Session session = sf.openSession();
		System.out.println("=======Get Song By Artist");
		ArrayList<Song> songs = new ArrayList<Song>();
		for (Artist artist : artists) {
			Query query = session.createQuery("from Song where artist.artistId =:id");
			query.setLong("id", artist.getArtistId());
			songs.addAll(query.list());
		
		}
		session.flush();
		session.close();
		return songs;
	}

	public long songCount() {
		Session session = sf.openSession();
		System.out.println("=======Song Count");
		long count = ((Long) session.createQuery("select count(*) from Song").uniqueResult()).intValue();
		session.flush();
		session.clear();
		session.close();
		return count;

	}
	
	public String getLyrics(long songId){
		Session session = sf.openSession();
		System.out.println("=======Get Lyrics");
		Query query = session.createQuery("select lyrics from Song where song_id =:id");
		query.setLong("id",songId);
		String lyrics = (String) query.uniqueResult();
		
		session.flush();
		session.clear();
		session.close();
		
		return lyrics.replace("\\n", "");
		
	}

	public Song getSongSearched(Long songId){
		Session session = sf.openSession();
		try{
		Song song = (Song) session.createQuery("from Song where songId=:songId").setParameter("songId", songId).uniqueResult();
		session.flush();
		session.close();

		return song;
		}catch(Exception e){
			System.out.println("@ SongDao >> getSongSearched >> e "+e);
			return null;
		}

	}
	public List<Song> getSongQ(String searchedWord){
		Session session = sf.openSession();
		try{
			@SuppressWarnings("unchecked")
			List<Song> songs = (List<Song>) session.createQuery("from Song where songTitle =:sWord or lyrics like :sWord").setString("sWord", "%"+searchedWord.replaceAll("\\W", "_")+"%").list();
			session.flush();
			session.close();
			return songs;
		}catch(Exception e){
			System.out.println("@ SongDao >> getSongQ >> e "+e);
			return null;
		}
	}
	public List<BigInteger> getIdToUpdate(){
		Session session = sf.openSession();
		Query q = session.createSQLQuery("Select distinct song_id from song_tbl where song_id not in (select distinct song_id from song_word_bank)");
		List<BigInteger> songs = (List<BigInteger>)q.setMaxResults(20).list();
		session.flush();
		session.close();
		return songs;
	}

	public Song getSongSearchedbyIds(Long songIds){
		Session session = sf.openSession();
		try{
		@SuppressWarnings("unchecked")
		Song songs = (Song) session.createQuery("from Song where songId = :songIds").setParameter("songIds", songIds).uniqueResult();
		session.flush();
		session.close();

		return songs;
		}catch(Exception e){
			System.out.println("@ SongDao >> getSongSearchedIds >> e "+e);
			return null;
		}

	}
	public List<Long> checkSong(String searchedWord){
		Session session = sf.openSession();
		try{
			@SuppressWarnings("unchecked")
			List<Long> songIds = (List<Long>) session.createQuery("select songId from Song where songTitle =:sWord or lyrics like :sWord").setString("sWord", "%"+searchedWord.replaceAll("\\W", "_")+"%").list();
			session.flush();
			session.close();
			return songIds;
		}catch(Exception e){
			System.out.println("@ SongDao >> checkSong >> e "+e);
			return null;
		}
	}

}
