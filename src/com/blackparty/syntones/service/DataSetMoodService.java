package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.DataSetMoodDAO;
import com.blackparty.syntones.model.DataSetMood;

@Service
public class DataSetMoodService {
	@Autowired private DataSetMoodDAO dsmDao;
	
	public List<DataSetMood> getAllMood() throws Exception {
		return dsmDao.getAllMood();
	}
	public boolean updateBatchAllMood(List<DataSetMood> moodList) throws Exception{
		return dsmDao.updateBatchAllMood(moodList);
	}
}
