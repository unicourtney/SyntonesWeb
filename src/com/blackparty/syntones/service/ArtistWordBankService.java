package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.ArtistWordBankDAO;
import com.blackparty.syntones.model.ArtistWordBank;

@Service
public class ArtistWordBankService {
	@Autowired
	private ArtistWordBankDAO awbDao;
	
	public void updateWordBank(List<ArtistWordBank> words) throws Exception{
		awbDao.updateWordBank(words);
	}
	
	public List<ArtistWordBank> fetchAllWordBank() throws Exception{
		return awbDao.fetchAllWordBank();
	}
	public List<ArtistWordBank> fetchWordBank(long artistId) throws Exception{
		return awbDao.fetchWordBank(artistId);
		
	}
}
