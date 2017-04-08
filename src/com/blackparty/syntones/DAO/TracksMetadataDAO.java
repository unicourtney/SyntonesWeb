package com.blackparty.syntones.DAO;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.blackparty.syntones.model.TracksMetadata;

@Repository
@Transactional
public class TracksMetadataDAO {
	@Autowired
	private SessionFactory sf;
	
	public TracksMetadata getSong(int number)throws Exception {
		String sql = "from TracksMetadata where number =:name";
		Session session = sf.openSession();
		Query query = session.createQuery(sql);
		query.setInteger("name", number);
		TracksMetadata tm = (TracksMetadata)query.uniqueResult();
		session.flush();
		session.clear();
		session.close();
		return tm;
	}
	
	
}
