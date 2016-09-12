package com.blackparty.syntones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.PlaylistDAO;
import com.blackparty.syntones.model.Playlist;

@Service
public class PlaylistService {
	@Autowired PlaylistDAO playlistDao;
	public void savePlaylist(Playlist playlist)throws Exception{
		playlistDao.addPlaylist(playlist);
	}
}
