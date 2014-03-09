package com.automarket.DAO;

import static com.automarket.utils.HibernateUtil.getSessionFactory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.automarket.entity.CommodityCirculation;

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
	public void addCirculations(ArrayList<CommodityCirculation> circulations) {
		// TODO Auto-generated method stub
		
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
	public List<CommodityCirculation> commodityCirculationsByDay() {
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		Session session = null;
		List<CommodityCirculation> circulations = new ArrayList<>();
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			Criteria criteria = session.createCriteria(CommodityCirculation.class);
			criteria.add(Restrictions.between("date", startDate.getTime(), endDate.getTime()));
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

	

}
