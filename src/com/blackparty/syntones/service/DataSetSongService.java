package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.DataSetSongDAO;
import com.blackparty.syntones.model.DataSetSong;

@Service
public class DataSetSongService {
	@Autowired
	private DataSetSongDAO dsDao;
	
	public List<DataSetSong> getAllSongs()throws Exception{
		return dsDao.getAllSongs();
	}
	
}
