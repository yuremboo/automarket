package com.automarket.persistence.DAO;

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
			log.info("Updated counter: " + counter);
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
    public Counter getCounterByGoodsStore(Goods goods, Store store) {
        Session session = null;
        Counter counter = new Counter();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Counter.class);
            criteria.add(Restrictions.eq("store.id", store.getId()));
            criteria.add(Restrictions.eq("goods.id", goods.getId()));
            counter = (Counter) criteria.list().get(0);
            session.getTransaction().commit();
            log.info("Get all counters by Goods Store: " + counter);
        } catch (Exception e) {
            log.error("Error get " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return counter;
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
    public List<Counter> getCountersListByStore(Store store) {
        Session session = null;
        List<Counter> counters = new ArrayList<>();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Counter.class);
            criteria.add(Restrictions.eq("store.id", store.getId()));
            counters.addAll(criteria.list());
            session.getTransaction().commit();
            log.info("Get all counters by store: " + counters);
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

    @Override
    public void addOrUpdateCounter(Counter counter) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            session.saveOrUpdate(counter);
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
    public void addOrUpdateCounterList(List<Counter> counterList) {
        Session session = null;
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            for (Counter counter : counterList) {
                session.saveOrUpdate(counter);
                if (session.isDirty()) {
                    session.flush();
                    session.clear();
                }
            }
            session.getTransaction().commit();
            log.info("Added/updated counters: " + counterList);
        } catch (Exception e) {
            log.error("Error insert " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
    }

    @Override
    public List<Counter> searchCountersByGoods(String s) {
        Session session = null;
        List<Counter> counters = new ArrayList<>();
        try {
            session = getSessionFactory().openSession();
            session.beginTransaction();
            Criteria criteria = session.createCriteria(Counter.class);
            criteria.createAlias("goods", "g");
            criteria.add(Restrictions.like("g.name", "%" + s + "%").ignoreCase());
            counters.addAll(criteria.list());
            session.getTransaction().commit();
            log.info("Search all counters: " + counters);
        } catch (Exception e) {
            log.error("Error search " + e);
        } finally {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }
        return counters;
    }
}
