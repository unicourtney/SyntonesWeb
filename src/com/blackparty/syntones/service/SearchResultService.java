package com.blackparty.syntones.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.SearchResultDAO;
import com.blackparty.syntones.model.SearchResult;

@Service
public class SearchResultService {
	@Autowired
	private SearchResultDAO srDao;
	
	public List<Long> getSearchResult(long searchId){
		return srDao.getSearchResult(searchId);
	}
	public void addResult(ArrayList<SearchResult> result){
		srDao.addResult(result);
	}
	public List<SearchResult> getSearchResult1(long searchId) {
		return srDao.getSearchResult1(searchId);
	}
}
