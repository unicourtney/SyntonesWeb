package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.DataSetConditionalProbabilityDAO;
import com.blackparty.syntones.model.DataSetConditionalProbability;
import com.blackparty.syntones.model.DataSetMood;

@Service
public class DataSetConditionalProbabilityService {
	@Autowired
	private DataSetConditionalProbabilityDAO dsDao;
	public boolean save(List<DataSetMood> moodList)throws Exception{
		return dsDao.save(moodList);
	}
	
	public List<DataSetMood> setMood(List<DataSetMood> moodList) throws Exception{
		return 	dsDao.setMood(moodList);
	}
	
	public List<DataSetConditionalProbability> getList(int id)throws Exception{
		return dsDao.getList(id);
	}
}