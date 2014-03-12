package com.automarket.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.automarket.entity.Goods;

public class GoodsDAOImpl implements GoodsDAO {
	
	static Logger log = LogManager.getLogger(GoodsDAOImpl.class);

	@Override
	public void addGoods(Goods goods) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.save(goods);
			session.getTransaction().commit();
			log.info("Added new goods: " + goods);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}
	
	@Override
	public void addGoodsList(List<Goods> goods) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			for (Goods good : goods) {
				session.save(good);
				if (session.isDirty()) {
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
			log.info("Added new goods: " + goods);
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
		// TODO Auto-generated method stub

	}

	@Override
	public List<Goods> getAllGoods() {
		Session session = null;
		List<Goods> goods = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Goods.class);
			goods.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Added new goods: " + goods);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return goods;
	}

	@Override
	public Goods getGoodsByName(String name) {
		Session session = null;
		Goods goods = new Goods();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Goods.class);
			criteria.add(Restrictions.or(Restrictions.like("name", name).ignoreCase()));
			goods = (Goods) criteria.list().get(0);
			log.info("" + goods);
		} catch (Exception e) {
			log.error("Error get by name: " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return goods;
	}

}
