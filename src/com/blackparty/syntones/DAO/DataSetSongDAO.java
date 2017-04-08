package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.DataSetSong;

@Repository
@Transactional
public class DataSetSongDAO {
	@Autowired
	private SessionFactory sf;
	
	public List<DataSetSong> getAllSongs() throws Exception{
		System.out.println("Fetching all songs on the song dataset");
		Session session = sf.openSession();
		Query query = session.createQuery("from DataSetSong");
		//query.setMaxResults(2);
		List<DataSetSong> songList = query.list();
		session.flush();
		session.close();
		return songList;
	}
}
