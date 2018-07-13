package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
/**
 * @author: Amir H. Sharifzadeh
 * @Dolat Service
 */
public interface HibernateCommand {
    Object execute(Session session) throws HibernateException;
}