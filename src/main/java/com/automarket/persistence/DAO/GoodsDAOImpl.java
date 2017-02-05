package com.automarket.persistence.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.automarket.entity.Goods;

public class GoodsDAOImpl implements GoodsDAO {
	
	static Logger log = LogManager.getLogger(GoodsDAOImpl.class);

	@Override
	public byte addGoods(Goods goods) {
		Session session = null;
        byte b = 1;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.saveOrUpdate(goods);
            session.flush();
            session.clear();
			session.getTransaction().commit();
			log.info("Added new goods: " + goods);
		} catch (Exception e) {
            b = 0;
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
        return b;
	}
	
	@Override
	public byte addGoodsList(List<Goods> goods) {
		Session session = null;
        byte result = 0;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			for (Goods good : goods) {
				session.saveOrUpdate(good);
				if (session.isDirty()) {
					session.flush();
					session.clear();
				}
			}
			session.getTransaction().commit();
			log.info("Added new goods: " + goods);
		} catch (Exception e) {
            result = 1;
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
        return result;
	}

	@Override
	public void remove(Goods goods) {
		Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.delete(goods);
            session.getTransaction().commit();
            log.info("Deleted goods: " + goods);
        } catch (Exception e) {
            log.error("Error insert " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	@Override
	public List<Goods> getAllGoods() {
		Session session = null;
		List<Goods> goods = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(Goods.class);
			goods = criteria.list();
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
			criteria.add(Restrictions.like("name", name).ignoreCase());
			goods = (Goods) criteria.list().get(0);
            session.getTransaction().commit();
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

    @Override
    public List<Goods> searchGoods(String text) {
        Session session = null;
        List<Goods> goods = new ArrayList<>();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Goods.class);
            criteria.add(Restrictions.like("name", "%" + text + "%").ignoreCase());
            goods = criteria.list();
            session.getTransaction().commit();
            log.info("Search goods: " + goods);
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
    public Integer getMaxIdentity() {
        Session session = null;
        Integer max = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Goods.class);
            criteria.setProjection(Projections.max("analog"));
            max = (Integer) criteria.list().get(0);
            session.getTransaction().commit();
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return max;
    }
}
