package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.User;
import com.blackparty.syntones.service.SongService;
import com.blackparty.syntones.service.UserService;

@Repository
@Transactional
public class PlaylistDAO {
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	UserService userService;
	
	@Autowired
	SongService songService;

	public void addPlaylist(Playlist playlist) throws Exception {
		// complete details needed for playlist
		//fetch details for user
		User fetchedUser = userService.getUser(playlist.getUser());
		playlist.setUser(fetchedUser);
		
		//fetch details for the song
		List<Song> songList = songService.getAllSongs(playlist.getSongIdList());
		
		Session session = sessionFactory.openSession();
		session.save(playlist);
		session.flush();
		session.close();
	}
}
