package com.blackparty.syntones.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.SearchResult;

@Repository
@Transactional
public class SearchResultDAO {
	@Autowired
	private SessionFactory sf;

	public List<Long> getSearchResult(long searchId) {
		Session session = sf.openSession();
		try {
			Query query = session
					.createQuery("select songId from SearchResult where searchId =:searchId order by degree asc")
					.setLong("searchId", searchId);
			List<Long> songIds = (List<Long>)query.list();
			session.flush();
			session.close();
			return songIds;
		} catch (Exception e) {
			System.out.println("at SearchResultDAO >>  getSearchResult() >>" + e);
			return null;
		}
	}

	public void addResult(ArrayList<SearchResult> results) {
		Session session = sf.openSession();
		try {
			for (SearchResult result : results) {
				session.save(result);
				session.flush();
			}
			session.close();
		} catch (Exception e) {
			System.out.println("at SearchResultDAO >>  addResult() >>" + e);
		}
	}

	public List<SearchResult> getSearchResult1(long searchId) {
		Session session = sf.openSession();
		try {
			Query query = session
					.createQuery("from SearchResult where searchId =:searchId order by degree asc")
					.setLong("searchId", searchId);
			List<SearchResult> songIds = (List<SearchResult>)query.list();
			session.flush();
			session.close();
			return songIds;
		} catch (Exception e) {
			System.out.println("at SearchResultDAO >>  getSearchResult() >>" + e);
			return null;
		}
	}
}
