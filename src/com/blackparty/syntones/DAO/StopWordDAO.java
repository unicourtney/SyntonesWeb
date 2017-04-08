package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.StopWord;

@Repository
@Transactional
public class StopWordDAO {
	@Autowired private SessionFactory sessionFactory;
	
	@SuppressWarnings("unchecked")
	public List<String> getAllStopWords(){
		try{
		Session session = sessionFactory.openSession();
		Query query = session.createQuery("select word from StopWord");
		List<String> stopWords = query.list();
		session.flush();
		session.close();
		return stopWords;
		}catch(Exception e){
			System.out.println("Exception @ getStopWords >> e"+e);
			return null;
		}
	}
}
