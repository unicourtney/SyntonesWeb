package com.blackparty.syntones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.TracksMetadataDAO;
import com.blackparty.syntones.model.TracksMetadata;

@Service
public class TracksMetadataService {
	@Autowired
	private TracksMetadataDAO tmDao;
	
	public TracksMetadata getSong(int number)throws Exception{
		return tmDao.getSong(number);
	}
}
