package com.blackparty.syntones.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blackparty.syntones.DAO.GenreDAO;
import com.blackparty.syntones.model.Genre;

@Service
public class GenreService {
	@Autowired
	private GenreDAO genreDao;
	
	public void saveGenre(Genre genre)throws Exception{
		genreDao.saveGenre(genre);
		
	}
	public Genre getGenre(long id)throws Exception{
		return genreDao.getGenre(id);
	}
	
	public List<Genre> getGenreString(){
		return genreDao.getGenreString();
	}
}
