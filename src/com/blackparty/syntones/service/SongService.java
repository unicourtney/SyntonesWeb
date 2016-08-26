package com.blackparty.syntones.service;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.blackparty.syntones.DAO.SongDAO;
import com.blackparty.syntones.model.Artist;
import com.blackparty.syntones.model.Song;

@Service
public class SongService {
	@Autowired
	private SongDAO songDao;
	
	public void addSong(Song song) throws Exception{
		songDao.addSong(song);
	}
}
