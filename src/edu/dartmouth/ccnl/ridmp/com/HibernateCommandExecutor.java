package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/**
 * A <code>HibernateCommandExecutor</code> executes a {@link edu.dartmouth.ccnl.ridmp.com.HibernateCommand}.
 * This interface decouples <em>Persistence Manager</em> from some other components including
 * <em>Search Component 3.x</em>.
 *
 * @author Amir  Sharifzadeh
 * @version 1.1.9
 * @date Aug 23, 2005
 */
public interface HibernateCommandExecutor {
    /**
     * decides whether the command executor can execute the {@link edu.dartmouth.ccnl.ridmp.com.HibernateCommand} or not.
     */
     boolean canExecute(Session session, HibernateCommand command) throws HibernateException, HibernateEXC;

     /**
      * executes the {@link edu.dartmouth.ccnl.ridmp.com.HibernateCommand} and returns the result.
      */
     Object executeCommand(Session session, HibernateCommand command) throws HibernateException, HibernateEXC;
}