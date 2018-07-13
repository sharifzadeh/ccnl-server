package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

public interface SearchableHibernateCommand extends HibernateCommand{
    Class getResultClass(Session session) throws HibernateException;
}