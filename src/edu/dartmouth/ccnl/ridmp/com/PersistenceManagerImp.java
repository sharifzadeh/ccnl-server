package edu.dartmouth.ccnl.ridmp.com;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Iterator;
import java.util.List;


import java.util.Map;
import java.util.Set;

import javax.sql.RowSet;

import edu.dartmouth.ccnl.ridmp.dto.RIDMPDataObject;

import net.sf.hibernate.Criteria;

import org.apache.log4j.Logger;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.LockMode;
import net.sf.hibernate.Query;
import net.sf.hibernate.Session;
import net.sf.hibernate.expression.Criterion;
import net.sf.hibernate.expression.Example;
import net.sf.hibernate.expression.Expression;
import net.sf.hibernate.expression.MatchMode;
import net.sf.hibernate.expression.Order;

import oracle.jdbc.rowset.OracleCachedRowSet;

import oracle.sql.BLOB;

public final class PersistenceManagerImp implements PersistenceManager {

    // static instance for singleton pattern
    private static PersistenceManager instance;
    private static final Logger logger = 
        Logger.getLogger(PersistenceManagerImp.class);

    /**
     * private constructor (singleton pattern)
     */
    private PersistenceManagerImp() {
    }

    /**
     * static method for getting an instance of PersistanceManager (singleton pattern)
     *
     * @return static instance of PersistanceManager
     */
    public static PersistenceManager getInstance() {
        if (instance == null) {
            instance = new PersistenceManagerImp();
        }
        return instance;
    }

    /**
     * execute an HibernateCommand. (Command design pattern.)
     * Operations performed:
     * <ul>
     * <li>current <code>Session</code> and <code>Transaction</code> which are assigned to current thread are retrieved.</li>
     * <li>tries to execute command with custom {@link HibernateCommandExecutor}s. If no proper one is found the fallback
     * one (@link DefaultCommandExecutor} is used.</li>
     * <li>commits current <code>Transaction</code> if no exception occures or rolls it back if an exception occures.</li>
     * <li>translates the exception to a proper one if one is thrown.</li>
     * <li>closes current <code>Session</code> if it is not marked to close by <code>PersistenceManagerFilter</code>.</li>
     * <ul>
     *
     * @param command that must be executed
     * @return result of execute command
     * @throws edu.dartmouth.ccnl.ridmp.com.HibernateEXC
     */
    private Object executeCommand(HibernateCommand command) throws HibernateEXC {
        try {
            Session session = PersistenceUtils.currentSession();
            
            PersistenceUtils.currentTransaction();

            Object result = null;

            boolean assigned = false;
            for (int i = 0; i < PersistenceUtils.COMMAND_EXECUTORS.length; 
                 i++) {
                HibernateCommandExecutor commandExecutor = 
                    PersistenceUtils.COMMAND_EXECUTORS[i];
                if (commandExecutor.canExecute(session, command)) {
                    result = commandExecutor.executeCommand(session, command);
                    assigned = true;
                    break;
                }
            }

            if (!assigned) {
                result = 
                        DefaultCommandExecutor.getInstance().executeCommand(session, 
                                                                            command);
            }

            PersistenceUtils.commitTransaction();

            //            LogAdaptor.doLog();
            return result;
        } catch (HibernateException e) {
            try {
                PersistenceUtils.rollbackTransaction();
                PersistenceUtils.closeSession();
            } catch (HibernateException he) {
                throw PersistenceUtils.translateException(e);
            }
            logger.error(e, e);
            throw PersistenceUtils.translateException(e);
        } finally {
            if (!PersistenceUtils.isSessionByFilter()) {
                PersistenceUtils.closeSession();
            }
        }
    }

    /* PersistencManager Implementation */

     public List findByQuery(final String query, final String alias, final Class clazz) throws HibernateEXC {
         return (List)executeCommand(new SearchableHibernateCommand() {
                     public Object execute(Session session) throws HibernateException {
                         return session.createSQLQuery(query, alias, clazz);
                     }

                     public Class getResultClass(Session session) {
                         return clazz;
                     }
                 });
     }

    public RIDMPDataObject saveDolatSendLetterTIF(RIDMPDataObject mfaDataObject) throws HibernateEXC {
        return null;
    }

    public RIDMPDataObject findByPrimaryKey(final Class clazz,
                                          final Serializable pk) throws HibernateEXC {
        return (RIDMPDataObject)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        WorkFlowDataAccessObject workFlowDataAccessObject = 
                            new WorkFlowDataAccessObject();
                        Object obj = null;
                        if (workFlowDataAccessObject.isObjectInWorkflow(clazz, 
                                                                        pk))
                            obj = 
session.createCriteria(clazz).add(Expression.eq(OBJECT_ID, pk)).uniqueResult();
                        return obj;
                    }
                });
    }

    public RIDMPDataObject loadByPrimaryKey(final Class clazz,
                                          final Serializable pk, final LockMode mode) throws HibernateEXC {
        return (RIDMPDataObject)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        WorkFlowDataAccessObject workFlowDataAccessObject = 
                            new WorkFlowDataAccessObject();
                        Object obj = null;
                        if (workFlowDataAccessObject.isObjectInWorkflow(clazz, pk))
                            obj = session.load(clazz, pk, mode);
                        return obj;
                    }
                });
    }

    public RIDMPDataObject findByPrimaryKey(final RIDMPDataObject object) throws HibernateEXC {
        return (RIDMPDataObject)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Class clazz = object.getClass();
                        return session.createCriteria(clazz).add(Expression.eq(OBJECT_ID, 
                                                                               object)).uniqueResult();
                    }
                });
    }
    
    public List findByPrimaryKeys(final Class clazz, 
                                  final List pks) throws HibernateEXC {
        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        return session.createCriteria(clazz).add(Expression.in(OBJECT_ID, 
                                                                               pks)).list();
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }

    public List findAll(final Class clazz) throws HibernateEXC {
        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        return session.createCriteria(clazz).list();
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }

    public List findAllOrdrBy(final Class clazz, final String orderBy, 
                              final boolean ascending) throws HibernateEXC {

        return findByIndexOrderBy(clazz, 0, 0, orderBy, ascending);
    }

    public List findByIndex(final Class clazz, final int start, 
                            int count) throws HibernateEXC {
        return findByIndexOrderBy(clazz, start, count, null, true);
    }

    public List findByIndexOrderBy(final Class clazz, final int start, 
                                   final int count, final String orderBy, 
                                   final boolean ascending) throws HibernateEXC {

        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Criteria criteria = 
                            session.createCriteria(clazz).setFirstResult(start);

                        if (count != 0) {
                            criteria.setMaxResults(count);
                        }

                        if (orderBy != null) {
                            criteria.addOrder(ascending ? Order.asc(orderBy) : 
                                              Order.desc(orderBy));
                        }

                        return criteria.list();
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }

    public RIDMPDataObject findUniqueByCondition(final Class clazz,
                                               final Criterion criterion) throws HibernateEXC {

        return (RIDMPDataObject)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Criteria criteria = session.createCriteria(clazz);

                        if (criterion != null) {
                            criteria.add(criterion);
                        }

                        return criteria.uniqueResult();
                    }
                });
    }

    public List findByCondition(final Class clazz, 
                                final Criterion criterion) throws HibernateEXC {
        return findByConditionOrderBy(clazz, criterion, 0, 0, null, false);
    }

    public List findByConditionOrderBy(Class clazz, Criterion criterion, 
                                       String orderBy, 
                                       boolean ascending) throws HibernateEXC {

        return findByConditionOrderBy(clazz, criterion, 0, 0, orderBy, 
                                      ascending);
    }

    public List findByCondition(final Class clazz, final Criterion criterion, 
                                final int start, 
                                final int count) throws HibernateEXC {

        return findByConditionOrderBy(clazz, criterion, start, count, null, 
                                      false);
    }

    public List findByConditionOrderBy(final Class clazz, 
                                       final Criterion criterion, 
                                       final int start, final int count, 
                                       final String orderBy, 
                                       final boolean ascending) throws HibernateEXC {

        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Criteria criteria = 
                            session.createCriteria(clazz).setFirstResult(start);

                        if (criterion != null) {
                            criteria.add(criterion);
                        }

                        if (count > 0) {
                            criteria.setMaxResults(count);
                        }

                        if (orderBy != null) {
                            Order order = 
                                ascending ? Order.asc(orderBy) : Order.desc(orderBy);
                            criteria.addOrder(order);
                        }

                        return criteria.list();
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }

    public List findByConditions(final Class clazz, 
                                 final List criteriaList) throws HibernateEXC {
        return findByConditions(clazz, criteriaList, 0, 0);
    }

    public List findByConditionsOrderBy(Class clazz, List criteriaList, 
                                        String orderBy, 
                                        boolean ascending) throws HibernateEXC {

        return findByConditionsOrderBy(clazz, criteriaList, 0, 0, orderBy, 
                                       ascending);
    }

    public List findByConditions(final Class clazz, final List criteriaList, 
                                 final int start, 
                                 final int count) throws HibernateEXC {

        return findByConditionsOrderBy(clazz, criteriaList, start, count, null, 
                                       false);
    }

    public List findByConditionsOrderBy(final Class clazz, 
                                        final List criteriaList, 
                                        final int start, final int count, 
                                        final String orderBy, 
                                        final boolean ascending) throws HibernateEXC {

        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {

                        Criteria criteria = 
                            session.createCriteria(clazz).setFirstResult(start);
                        for (Iterator itr = criteriaList.iterator(); 
                             itr.hasNext(); ) {
                            Criterion criterion = (Criterion)itr.next();
                            if (criterion != null) {
                                criteria.add(criterion);
                            }
                        }

                        if (count > 0) {
                            criteria.setMaxResults(count);
                        }

                        if (orderBy != null) {
                            Order order = 
                                ascending ? Order.asc(orderBy) : Order.desc(orderBy);
                            criteria.addOrder(order);
                        }

                        return criteria.list();
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }

    public List findByExample(final RIDMPDataObject object) throws HibernateEXC {
        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        return session.createCriteria(object.getClass()).add(Example.create(object).enableLike(MatchMode.ANYWHERE)).list();
                    }

                    public Class getResultClass(Session session) {
                        return object.getClass();
                    }
                });
    }

    public RIDMPDataObject findUniqueByQuery(final String hql,
                                           final Object[] parameters) throws HibernateEXC {
        return (RIDMPDataObject)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Query query = session.createQuery(hql);
                        if (parameters != null) {
                            for (int i = 0; i < parameters.length; i++) {
                                Object parameter = parameters[i];
                                query.setParameter(i, parameter);
                            }
                        }

                        return query.uniqueResult();
                    }
                });
    }

    public List findByQuery(final String hql, 
                            final Object[] parameters) throws HibernateEXC {
        return findByQuery(hql, parameters, 0);
    }

    public List findByQuery(final String hql, final Object[] parameters, 
                            final int maxResults) throws HibernateEXC {

        return (List)executeCommand(new ListFindByQuerySearchableHibernateCommand(hql, 
                                                                                  parameters, 
                                                                                  maxResults));
    }

    public List findByQuery(String hql, Map parameters) throws HibernateEXC {
        return findByQuery(hql, parameters, 0);
    }

    public List findByQuery(final String hql, final Map parameters, 
                            final int maxResults) throws HibernateEXC {

        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Query query = session.createQuery(hql);
                        if (parameters != null && parameters.size() != 0) {
                            Set entrySet = parameters.entrySet();
                            for (Iterator iterator = entrySet.iterator(); 
                                 iterator.hasNext(); ) {
                                Map.Entry entry = (Map.Entry)iterator.next();
                                query.setParameter((String)entry.getKey(), 
                                                   entry.getValue());
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
                });
    }

    public int count(final Class clazz) throws HibernateEXC {
        Integer result = (Integer)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        return session.createQuery("select count(object) from " + 
                                                   clazz.getName() + 
                                                   " as object").uniqueResult();
                    }
                });

        return result.intValue();
    }

    public void save(final RIDMPDataObject mfaDataObject) throws HibernateEXC {
        executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        session.save(mfaDataObject);
                        session.flush();
                        session.refresh(mfaDataObject);
                        return null;
                    }
                });
    }

    public Serializable saveAndReturnPrimaryKey(final RIDMPDataObject mfaDataObject) throws HibernateEXC {

        return (Serializable)executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Object key = session.save(mfaDataObject);
                        session.flush();
                        session.refresh(mfaDataObject);
                        return key;
                    }
                });
    }

    public void update(final RIDMPDataObject mfaDataObject) throws HibernateEXC {
        executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        session.update(mfaDataObject);
                        session.flush();
                        session.refresh(mfaDataObject);
                        return null;
                    }
                });
    }

    public void saveOrUpdate(final RIDMPDataObject mfaDataObject) throws HibernateEXC {
        executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        session.saveOrUpdate(mfaDataObject);
                        session.flush();
                        session.refresh(mfaDataObject);
                        return null;
                    }
                });
    }

    public void delete(final Class clazz, 
                       final Serializable pk) throws HibernateEXC {
        executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        session.delete("from " + clazz.getName() + 
                                       " as object where object.id = " + pk);
                        session.flush();
                        return null;
                    }
                });
    }

    public void delete(final RIDMPDataObject mfaDataObject) throws HibernateEXC {
        executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Class clazz = mfaDataObject.getClass();
                        Serializable pk = 
                            session.getSessionFactory().getClassMetadata(clazz).getIdentifier(mfaDataObject);

                        session.delete("from " + clazz.getName() + 
                                       " as object where object.id = " + pk);
                        session.flush();

                        /**
                 *  changed in 1.0.1
                 */
                        //session.delete(mfaDataObject);

                        return null;
                    }
                });
    }

    public Object execute(final JDBCCommand command) throws HibernateEXC {
        return executeCommand(new HibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Connection connection;
                        try {
                            connection = session.connection();
                            return command.execute(connection);
                        } catch (SQLException e) {
                            throw session.getSessionFactory().getSQLExceptionConverter().convert(e, 
                                                                                                 null);
                        }
                    }
                });
    }

    public RowSet executeJDBC(final String query, 
                              final Object[] parameters) throws HibernateEXC {
        return (RowSet)execute(new JDBCCommand() {
                    public Object execute(Connection connection) throws SQLException {
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;

                        try {
                            preparedStatement = 
                                    connection.prepareStatement(query);

                            if (parameters != null) {
                                for (int i = 0; i < parameters.length; i++) {
                                    Object parameter = parameters[i];
                                    preparedStatement.setObject(i + 1, 
                                                                parameter);
                                }
                            }

                            logger.info("query: " + query);

                            resultSet = preparedStatement.executeQuery();

                            OracleCachedRowSet oracleCachedRowSet = 
                                new OracleCachedRowSet();
                            oracleCachedRowSet.populate(resultSet);

                            return oracleCachedRowSet;
                        } finally {
                            if (resultSet != null) {
                                resultSet.close();
                            }

                            if (preparedStatement != null) {
                                preparedStatement.close();
                            }
                        }
                    }
                });
    }

    public ResultSet executeJDBCCommand(final String query, 
                                        final Object[] parameters) throws HibernateEXC {
        return (RowSet)execute(new JDBCCommand() {
                    public Object execute(Connection connection) throws SQLException {
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;

                        try {
                            
                            
                            preparedStatement = connection.prepareStatement("select * from viewdepartmentshow where DEP_CODE like'%" + 81 + "%'");
                            resultSet = preparedStatement.executeQuery("select * from viewdepartmentshow where DEP_CODE like'%" + 81 + "%'");
//                            resultSet = stmt.executeQuery("select * from viewdepartmentshow where DEP_CODE like'%" + 81 + "%'");
                            
//                            resultSet.first();
                            while(resultSet.next()) {
                                System.out.println(resultSet.getString("DEP_CODE"));
                            }
                            
                            preparedStatement = 
                                    connection.prepareStatement(query);

                            if (parameters != null) {
                                for (int i = 0; i < parameters.length; i++) {
                                    Object parameter = parameters[i];
                                    preparedStatement.setObject(i + 1, 
                                                                parameter);
                                }
                            }

                            logger.info("query: " + query);

                            resultSet = preparedStatement.executeQuery();

                            return resultSet;
                        } finally {
                            if (resultSet != null) {
                                resultSet.close();
                            }

                            if (preparedStatement != null) {
                                preparedStatement.close();
                            }
                        }
                    }
                });
    }
    
    public List findByLike(final Class clazz, final Object obj) throws HibernateEXC {
        return (List)executeCommand(new SearchableHibernateCommand() {
                    public Object execute(Session session) throws HibernateException {
                        Criterion criteria = Example.create(obj).enableLike(MatchMode.EXACT);
                        List list = session.createCriteria(clazz).add(criteria).list();
                        return list;
                    }

                    public Class getResultClass(Session session) {
                        return clazz;
                    }
                });
    }
    
  
}
