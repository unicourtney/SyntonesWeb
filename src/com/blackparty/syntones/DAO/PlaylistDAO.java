package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
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
		// fetch details for user
		User fetchedUser = userService.getUser(playlist.getUser());
		playlist.setUser(fetchedUser);

		// fetch details for the song
		List<Song> songList = songService.getAllSongs(playlist.getSongIdList());
		playlist.setSongs(songList);
		Session session = sessionFactory.openSession();
		session.save(playlist);
		session.flush();
		session.close();
	}

	public Playlist getSongsFromPlaylist(long id) throws Exception {
		Session session = sessionFactory.openSession();
		Query query = session.createSQLQuery("select song_id from playlist_song b where b.playlist_id =:id");
		query.setLong("id", id);
		List<String> songId = new ArrayList<String>();
		List<Song> songList = new ArrayList<Song>();
		for (int i = 0; i < query.list().size(); i++) {
			System.out.println(query.list().get(i));
			songList.add(songService.getSong(String.valueOf(query.list().get(i))));
		}
		
		query = session.createSQLQuery("select playlist_name from playlist_tbl b where b.playlist_id = :id");
		query.setLong("id", id);
		Playlist fetchedPlaylist = new Playlist();
		fetchedPlaylist.setSongs(songList);
		fetchedPlaylist.setPlaylistName((String)query.uniqueResult());
		session.flush();
		session.close();
		
		return fetchedPlaylist;
	}

	public ArrayList<Playlist> getPlaylist(User user) throws Exception {
		ArrayList<Playlist> playlists = new ArrayList<>();
		ArrayList<Song> songlist = new ArrayList<>();
		// fetching the playlistId
		User fetchedUser = (User) userService.getUser(user);
		// System.out.println("getting playlist for user:
		// "+fetchedUser.getUserId());
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("from Playlist b join b.songs s where b.user.userId=:id");
		query.setLong("id", fetchedUser.getUserId());
		List<Object[]> test = (List<Object[]>) query.list();

		if (test.isEmpty()) {
			return null;
		}
		/*
		 * for(Object[] e:test){ System.out.println(e[0].toString());
		 * System.out.println(e[1].toString()); }
		 */

		Playlist newPlaylist = null;
		for (Object[] e : test) {
			Playlist p = (Playlist) e[0];
			// System.out.println(">>>>>>>>>>>>>>>>>>"+p.getPlaylistId());
			long temp = p.getPlaylistId();
			if (songlist.isEmpty()) {
				// System.out.println("hit");
				newPlaylist = p;
				songlist.add((Song) e[1]);
			} else {
				System.out.println("hithit");
				if (temp == newPlaylist.getPlaylistId()) {
					// System.out.println("same");
					songlist.add((Song) e[1]);
				} else {
					// System.out.println("new");
					newPlaylist.setSongs(songlist);
					playlists.add(newPlaylist);
					newPlaylist = p;
					songlist = new ArrayList<>();
					songlist.add((Song) e[1]);
				}
			}
		}
		newPlaylist.setSongs(songlist);
		playlists.add(newPlaylist);
		System.out.println("Fetching Playlists for: " + user.getUsername());
		for (Playlist pl : playlists) {
			// System.out.println("harharharharharh");
			List<Song> songs = pl.getSongs();
			System.out.println("Playlist Name: " + pl.getPlaylistName());
			for (Song s : songs) {
				System.out.println("\ttitle: " + s.getSongTitle());
			}
		}
		
		session.flush();
		session.close();

		return playlists;
	}

}
