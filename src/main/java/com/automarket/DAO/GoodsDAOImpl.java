package com.automarket.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;

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
		// TODO Auto-generated method stub
		return null;
	}

}
