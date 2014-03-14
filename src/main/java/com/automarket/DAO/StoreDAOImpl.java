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

public class StoreDAOImpl implements StoreDAO {
	
	static Logger log = LogManager.getLogger(StoreDAOImpl.class);

	@Override
	public void addStore(Store store) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(store);
			session.getTransaction().commit();
			log.info("Added new store: " + store);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@Override
	public void remove(int id) {
		
	}

	@Override
	public List<Store> getAllStores() {
		Session session = null;
		List<Store> stores = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Store.class);
			stores.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Get all stores: " + stores);
		} catch (Exception e) {
			log.error("Error get all stores " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return stores;
	}

	@Override
	public Store getStoreByName(String name) {
		Session session = null;
		Store store = new Store();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Store.class);
			criteria.add(Restrictions.or(Restrictions.like("name", name).ignoreCase()));
			store = (Store) criteria.list().get(0);
			log.info("Get store " + store);
		} catch (Exception e) {
			log.error("Error get by name: " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return store;
	}
	
	@Override
	public Store getDefault() {
		Session session = null;
		Store store = new Store();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Store.class)
			        .add(Restrictions.eq("defaultStore", true));
			store = (Store) criteria.list().get(0);
			session.getTransaction().commit();
			log.info("Get default: " + store);
		} catch (Exception e) {
			log.error(e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return store;
	}

    @Override
    public void changeDefault(Store oldDefault, Store newDefault) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.update(oldDefault);
            session.update(newDefault);
            session.getTransaction().commit();
            log.info("Changed default store");
        } catch (Exception e) {
            log.error("Error insert " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

}
