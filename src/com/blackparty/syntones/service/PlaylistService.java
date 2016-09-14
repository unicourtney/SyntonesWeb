package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlaylistDAO;
import com.blackparty.syntones.model.Playlist;
import com.blackparty.syntones.model.Song;
import com.blackparty.syntones.model.User;

@Service
public class PlaylistService {
	@Autowired PlaylistDAO playlistDao;
	public void savePlaylist(Playlist playlist)throws Exception{
		playlistDao.addPlaylist(playlist);
	}
	public List<Playlist> getPlaylist(User user)throws Exception{
		return playlistDao.getPlaylist(user);
	}
	
	public List<Song> getSongsFromPlaylist(long id)throws Exception{
		return playlistDao.getSongsFromPlaylist(id);
	}
}
