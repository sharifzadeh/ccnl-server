package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;

/**
 * @author Amir  Sharifzadeh
 * @date Aug 23, 2005
 */
 public class ListFindByQuerySearchableHibernateCommand implements SearchableHibernateCommand {
     private final String hql;
     private final Object[] parameters;
     private final int maxResults;

     public ListFindByQuerySearchableHibernateCommand(String hql, Object[] parameters, int maxResults) {
         this.hql = hql;
         this.parameters = parameters;
         this.maxResults = maxResults;
     }

     public Object execute(Session session) throws HibernateException {
         Query query = session.createQuery(hql);
         if (parameters != null) {
             for (int i = 0; i < parameters.length; i++) {
                 Object parameter = parameters[i];
                 query.setParameter(i, parameter);
             }
         }

         if (maxResults > 0) {
             query.setMaxResults(maxResults);
         }

         return query.list();
     }

     public Class getResultClass(Session session) throws HibernateException {
         return HibernateUtils.getHqlResultType(hql, session);
     }
 }