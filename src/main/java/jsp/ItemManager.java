package jsp;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

class ItemManager {

    SessionFactory sessionFactory;

    ItemManager(SessionFactory factory) {
        sessionFactory = factory;
    }

    void delete(String name) throws HibernateException {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            Item i = (Item) session.load(Item.class, name);
            session.delete(i);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        }
        
    }

    void add(Item item) throws HibernateException {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(item);
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        }
    }

    List<Item> getItems() throws HibernateException {
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {
            return session.createQuery("from Item").list();
        } catch (HibernateException e) {
            tx.rollback();
            throw e;
        }
    }
}
