package com.automarket.persistence.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.automarket.entity.CommodityCirculation;

@Deprecated
public class CommodityCirculationDAOImpl implements CommodityCirculationDAO {
	
	static Logger log = LogManager.getLogger(CommodityCirculationDAOImpl.class);

	@Override
	public void addCirculation(CommodityCirculation commodityCirculation) {
		Session session = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.save(commodityCirculation);
			session.getTransaction().commit();
			log.info("Added new circulation: " + commodityCirculation);
		} catch (Exception e) {
			log.error("Error insert " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}	
	}

	@Override
	public void addCirculations(List<CommodityCirculation> circulations) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            for (CommodityCirculation circulation : circulations) {
                session.save(circulation);
                if (session.isDirty()) {
                    session.flush();
                    session.clear();
                }
            }
            session.getTransaction().commit();
            log.info("Added/updated circulations: " + circulations);
        } catch (Exception e) {
            log.error("Error insert " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
	}

	@Override
	public List<CommodityCirculation> commodityCirculations() {
		Session session = null;
		List<CommodityCirculation> circulations = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(CommodityCirculation.class);
			circulations.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Get all circulations: " + circulations);
		} catch (Exception e) {
			log.error("Error get " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return circulations;
	}
	
	@Override
	public List<CommodityCirculation> commodityCirculationsByDay(boolean b) {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
        byte i = (byte) (b?1:0);
		Session session = null;
		List<CommodityCirculation> circulations = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(CommodityCirculation.class);
			criteria.add(Restrictions.between("date", startDate.getTime(), endDate.getTime()));
            criteria.add(Restrictions.eq("sale", b));
			circulations.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Get all circulations today: " + circulations);
		} catch (Exception e) {
			log.error("Error get " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return circulations;
	}
	
	@Override
	public List<CommodityCirculation> commodityCirculationsByMonth() {
		Session session = null;
		List<CommodityCirculation> circulations = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(CommodityCirculation.class);
			circulations.addAll(criteria.list());
			session.getTransaction().commit();
			log.info("Get all circulations: " + circulations);
		} catch (Exception e) {
			log.error("Error get " + e);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return circulations;
	}

    @Override
    public List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store store, Goods goods, Boolean issale) {
        Session session = null;
        List<CommodityCirculation> circulations = new ArrayList<>();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(CommodityCirculation.class);
            criteria.add(Restrictions.between("date", fromDate, toDate));
            if (store != null) {
                criteria.add(Restrictions.eq("store",store));
            }
            if (goods != null) {
                criteria.add(Restrictions.eq("goods",goods));
            }
            criteria.add(Restrictions.eq("sale",issale));
            criteria.addOrder(Order.asc("goods.id"));
            circulations.addAll(criteria.list());
            session.getTransaction().commit();
            log.info("Get filter circulations: " + circulations);
        } catch (Exception e) {
            log.error("Error get " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return circulations;
    }

    @Override
    public List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store store, Goods goods) {
        Session session = null;
        List<CommodityCirculation> circulations = new ArrayList<>();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(CommodityCirculation.class);
            criteria.add(Restrictions.between("date", fromDate, toDate));
            if (store != null) {
                criteria.add(Restrictions.eq("store",store));
            }
            if (goods != null) {
                criteria.add(Restrictions.eq("goods",goods));
            }
            criteria.addOrder(Order.asc("goods.id"));
            circulations.addAll(criteria.list());
            session.getTransaction().commit();
            log.info("Get filter circulations: " + circulations);
        } catch (Exception e) {
            log.error("Error get " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return circulations;
    }

    @Override
    public void removeZeroCirculations() {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Query query = session.createQuery("delete from CommodityCirculation C where C.count=:count").setInteger("count", 0);
            query.executeUpdate();
            session.getTransaction().commit();
            log.info("Removed zeros");
        } catch (Exception e) {
            log.error(e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }
}
