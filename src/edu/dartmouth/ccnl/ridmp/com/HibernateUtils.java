package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.type.EntityType;
import net.sf.hibernate.type.Type;

/**
 * @author Amir  Sharifzadeh
 * @date Aug 23, 2005
 */
public class HibernateUtils {
    public static Class getHqlResultType(String hql, Session session) throws HibernateException {
        Type[] returnTypes = session.createQuery(hql).getReturnTypes();
        if ((returnTypes.length == 1) &&
            (returnTypes[0] instanceof EntityType)) {
            EntityType entityType = (EntityType) returnTypes[0];
            return entityType.getAssociatedClass();
        }

        // it is unknown
        return Object.class;
    }

}
