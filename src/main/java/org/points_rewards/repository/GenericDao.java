package org.points_rewards.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * The type Generic dao.
 */
@Repository
@Transactional
public class GenericDao {

    //    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    public GenericDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private final Logger logger = LogManager.getLogger(this.getClass());


    /**
     * Save int.
     *
     * @param <T> the type parameter
     * @param o   the o
     * @return the int
     */
    public <T> int save(final T o) {

        return (Integer) sessionFactory.getCurrentSession().save(o);
    }

    /**
     * Save object.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void saveObject(final T o) {
        sessionFactory.getCurrentSession().save(o);
    }

    /**
     * Delete.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @param id   the id
     */
    public <T> void delete(final Class<T> type, Integer id) {
        T object = get(type, id);
        sessionFactory.getCurrentSession().delete(object);
    }

    /**
     * Delete object.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void deleteObject(final T o) {
        sessionFactory.getCurrentSession().delete(o);
    }

    /**
     * Get t.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @param id   the id
     * @return the t
     */
    public <T> T get(final Class<T> type, final Integer id) {
        return (T) sessionFactory.getCurrentSession().get(type, id);
    }

    /**
     * Save or update.
     *
     * @param <T> the type parameter
     * @param o   the o
     */
    public <T> void saveOrUpdate(final T o) {
        sessionFactory.getCurrentSession().saveOrUpdate(o);
    }


    /**
     * Gets all.
     *
     * @param <T>  the type parameter
     * @param type the type
     * @return the all
     */
    public <T> List<T> getAll(final Class<T> type) {
        final Session session = sessionFactory.getCurrentSession();
        final Criteria crit = session.createCriteria(type);
        return crit.list();
    }

}