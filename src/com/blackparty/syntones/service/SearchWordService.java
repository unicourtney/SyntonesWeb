package com.blackparty.syntones.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SearchWordDAO;

@Service
public class SearchWordService {
	@Autowired
	private SearchWordDAO swDao;
	
	public long addSearchWord(String searchString){
		return swDao.addSearchWord(searchString);
	}
	public long checkSearchedString(String searchString){
		return swDao.checkSearchedString(searchString);
	}
}
