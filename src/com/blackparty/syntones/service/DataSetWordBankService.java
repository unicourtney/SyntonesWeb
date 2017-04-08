package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.DataSetWordBankDAO;
import com.blackparty.syntones.model.DataSetWord;

@Service
public class DataSetWordBankService {
	@Autowired
	private DataSetWordBankDAO dsDao;
	
	public boolean save(List<String> wordList)throws Exception{
		return dsDao.save(wordList);
	}
	
	public List<DataSetWord> getAll()throws Exception{
		return dsDao.getAll();
	}
}
