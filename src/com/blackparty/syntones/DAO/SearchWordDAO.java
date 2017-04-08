package com.blackparty.syntones.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SearchedWord;

@Repository
@Transactional
public class SearchWordDAO {
	@Autowired
	private SessionFactory sf;
	
	public long addSearchWord(String searchString){
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("from SearchedWord");
			SearchedWord sw = new SearchedWord();
			sw.setSearchWord(searchString);
			long id = (Long)session.save(sw);
			session.flush();
			session.close();
			return id;
		} catch (Exception e) {
			System.out.println("at SearchedWordDAO >>  addSearchWord() >>" + e);
			return 0;
		}
	}
	
	public long checkSearchedString(String searchString){
		Session session = sf.openSession();
		try {
			Query query = session.createQuery("select searchId from SearchedWord where searchWord =:searchString").setString("searchString", searchString);
			long searchId = (Long) query.setMaxResults(1).uniqueResult();
			session.flush();
			session.close();
			if(searchId != 0){
				return searchId;
			}else{
				return searchId;
			}
		} catch (Exception e) {
			System.out.println("at SearchedWordDAO >>  checkSearchedString() >>" + e);
			return 0;
		}
	}
}
