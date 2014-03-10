package com.automarket.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public class CounterDAOImpl implements CounterDAO {
	
	static Logger log = LogManager.getLogger(CounterDAOImpl.class);

	@Override
	public void addCounter(Counter counter) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.save(counter);
			session.getTransaction().commit();
			log.info("Added new counter: " + counter);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}	
	}

	@Override
	public void removeCounter(int id) {
			
	}

	@Override
	public void updateCounter(Counter counter) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.update(counter);
			session.getTransaction().commit();
			log.info("Added new counter: " + counter);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}	
	}

	@Override
	public Counter getCounterById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Counter> getCountersList() {
		Session session = null;
		List<Counter> counters = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Counter.class);
			counters.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Get all counters: " + counters);
		} catch (Exception e) {
			log.error("Error get " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return counters;
	}
	
	@Override
	public int sale(Goods goods, Store store, int count) {
		Session session = null;
		Counter counter = new Counter();
		int result = 0;
		String hqlUpdate = "update Counter c set c.count = :newCount where c.goods.id = :goods and c.store.id = :store";
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Counter.class)
			        .add(Restrictions.eq("goods.id", goods.getId()))
			        .add(Restrictions.eq("store.id", store.getId()));
			result = -1;
			counter = (Counter) criteria.list().get(0);
			session.getTransaction().commit();
			result = 0;
			log.info("Get counters: " + counter);
			if (count <= counter.getCount()) {
				session.beginTransaction();
				counter.setCount(counter.getCount() - count);
				result = session.createQuery(hqlUpdate).setInteger("newCount", counter.getCount())
											.setLong("goods", goods.getId())
											.setInteger("store", store.getId()).executeUpdate();
				session.getTransaction().commit();
				log.info("Sale: " + counter);
			} else {
				result = -1;
			}
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return result;
	}
}
