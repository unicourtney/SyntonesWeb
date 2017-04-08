package com.blackparty.syntones.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.Genre;

@Repository
@Transactional
public class GenreDAO {
	
	@Autowired
	private SessionFactory sf;
	
	public void saveGenre(Genre genre) throws Exception{
		Session session = sf.openSession();
		String query = "from Genre where id =:id";
		Query q = session.createQuery(query);
		q.setLong("id", genre.getId());
		if(q.uniqueResult() == null){
			session.save(genre);
			session.flush();
			session.clear();
			session.close();
		}
	}
	
	public Genre getGenre(long id)throws Exception{
		Session session = sf.openSession();
		String query = "from Genre where id =:id";
		Query q = session.createQuery(query);
		q.setLong("id", id);
		Genre genre = (Genre)q.uniqueResult();
		session.flush();
		session.clear();
		session.close();
		return genre;
		
	}
	
	public List<Genre> getGenreString(){
		Session session = sf.openSession();
		Query query = session.createQuery("from Genre");
		List<Genre> genre = query.list();
		session.flush();
		session.clear();
		session.close();
		return genre;
	}
	

}
