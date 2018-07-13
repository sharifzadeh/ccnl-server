package edu.dartmouth.ccnl.ridmp.com;

import java.sql.Connection;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import net.sf.hibernate.CallbackException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.InstantiationException;
import net.sf.hibernate.JDBCException;
import net.sf.hibernate.MappingException;
import net.sf.hibernate.NonUniqueObjectException;
import net.sf.hibernate.NonUniqueResultException;
import net.sf.hibernate.ObjectDeletedException;
import net.sf.hibernate.ObjectNotFoundException;
import net.sf.hibernate.PersistentObjectException;
import net.sf.hibernate.PropertyAccessException;
import net.sf.hibernate.PropertyNotFoundException;
import net.sf.hibernate.PropertyValueException;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.StaleObjectStateException;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.TransactionException;
import net.sf.hibernate.TransientObjectException;
import net.sf.hibernate.UnresolvableObjectException;
import net.sf.hibernate.ValidationFailure;
import net.sf.hibernate.WrongClassException;
import net.sf.hibernate.cache.CacheException;
import net.sf.hibernate.cfg.Configuration;

import net.sf.hibernate.exception.ConstraintViolationException;
import net.sf.hibernate.exception.GenericJDBCException;
import net.sf.hibernate.exception.JDBCConnectionException;
import net.sf.hibernate.exception.LockAcquisitionException;
import net.sf.hibernate.exception.SQLGrammarException;
import net.sf.hibernate.id.IdentifierGenerationException;
import net.sf.hibernate.type.SerializationException;

import org.apache.log4j.Logger;

public final class PersistenceUtils {
    private static final String CONFIGURATION_FILE =
            "/edu/dartmouth/ccnl/ridmp/com/sessionFactory.cfg.xml";

    private static SessionFactory sessionFactory;
    private static final ThreadLocal sessionThreadLocal = new ThreadLocal();
    private static final ThreadLocal transactionThreadLocal = 
        new ThreadLocal();

    public static void setVoidTx() {
        sessionThreadLocal.set(null);
        transactionThreadLocal.set(null);
    }
    /**
     * if is set then closing the <code>Session</code> is done by <code>PersistenceManagerFilter</code>, otherwise the
     * <code>Session</code> is closed by {@link mfa.com.util.PersistenceManagerImpl}.
     */
    private static final ThreadLocal sessionByFilterThreadLocal = 
        new ThreadLocal();

    /**
     * if is set then used by <em>Search Component 3.x</em>
     * @since Search Component 3.x
     */
    private static final ThreadLocal searchParametersThreadLocal = 
        new ThreadLocal();

    private static final Logger logger = 
        Logger.getLogger(PersistenceUtils.class);

    /**
     * array containing all optional {@link HibernateCommandExecutor}s in order they are tried to run.
     */
    public static final HibernateCommandExecutor[] COMMAND_EXECUTORS = 
    { SearchComponentCommandExecutor.getInstance() };

    /**
     * initialize and build session factory and set it to static variable.
     * this method must be called during application startup.
     */
    public static void init() {
        if (sessionFactory == null) {
            try {
                PersistenceXMLParser parser = new PersistenceXMLParser();
                parser.parse(PersistenceUtils.class.getResourceAsStream(CONFIGURATION_FILE));

                Properties properties = parser.getProperties();
                if (properties != null) {
                    for (Iterator iterator = properties.keySet().iterator(); 
                         iterator.hasNext(); ) {
                        String key = (String)iterator.next();
                        String value = properties.getProperty(key);

                        System.setProperty(key, value);
                    }
                }

                Collection configFiles = parser.getConfigFiles();
                Configuration configuration = new Configuration();
                for (Iterator iterator = configFiles.iterator(); 
                     iterator.hasNext(); ) {
                    String file = (String)iterator.next();
                    configuration.configure(file);
                }

                sessionFactory = configuration.buildSessionFactory();
                System.out.println("build session factory");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Connection currentConenction() throws HibernateException {
        return currentSession().connection();
    }
    
    /**
     * close session factory
     * this method must be called during application shutdown
     */
    public static void destroy() {
        if (sessionFactory != null) {
            try {
                sessionFactory.close();
                sessionFactory = null;
            } catch (HibernateException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param b
     * @since 1.1.5
     */
    public static void setSessionByFilter(boolean b) {
        sessionByFilterThreadLocal.set(Boolean.valueOf(b));
    }

    /**
     * @return boolean value that indicate this session was opened with web filter or not
     * @since 1.1.5
     */
    public static boolean isSessionByFilter() {
        Boolean b = (Boolean)sessionByFilterThreadLocal.get();
        return b != null && b.booleanValue();
    }

    /**
     * this method open a session and set it to threadlocal. (if didnt it before)
     *
     * @return open session
     * @throws HibernateException
     */
    public static Session currentSession() throws HibernateException {
        Session session = (Session)sessionThreadLocal.get();
        if (session == null) {
            session = sessionFactory.openSession();
            logger.debug("Session created");
            sessionThreadLocal.set(session);
        }
//        if (session == null)
//            try {
//                System.out.println("session was null");
//                session = WFELocator.getHibernateSession();
//                System.out.println("session was created!");
//            } catch (ServiceLocatorException e) {
//                e.printStackTrace();
//            }
 
        if (session == null) 
            System.out.println("session in already null");
        
//        else
//            System.out.println("session is open");
            
        logger.debug("Session created");
        sessionThreadLocal.set(session);
        return session;
    }

    /**
     * this method close an opened session and reomve it from threadlocal
     */
    public static void closeSession() {
        Session session = (Session)sessionThreadLocal.get();

        sessionThreadLocal.set(null);
        transactionThreadLocal.set(null);

        if (session != null) {
            try {
                session.close();
            } catch (HibernateException e) {
                logger.error(e, e);
            }
            logger.debug("Session closed");
        }
    }

    /**
     * this method start a new transaction in current session and set it in threadlocal
     *
     * @return started transaction
     * @throws HibernateException
     */
    public static Transaction currentTransaction() throws HibernateException {
        Transaction transaction = (Transaction)transactionThreadLocal.get();
        if (transaction == null) {
            Session session = currentSession();
            transaction = session.beginTransaction();
            transactionThreadLocal.set(transaction);
        }
        return transaction;
    }

    /**
     * this method commit a transaction and remove it from thread local
     *
     * @throws HibernateException
     */
    public static void commitTransaction() throws HibernateException {
        Transaction transaction = (Transaction)transactionThreadLocal.get();
        if (transaction != null) {
            transaction.commit();
        }
        transactionThreadLocal.set(null);
    }

    /**
     * this method rellback a transaction and remove it from thread local
     *
     * @throws HibernateException
     */
    public static void rollbackTransaction() throws HibernateException {
        Transaction transaction = (Transaction)transactionThreadLocal.get();
        if (transaction != null) {
            transaction.rollback();
        }
        transactionThreadLocal.set(null);
    }

    /**
     * @since Search Component 3.x
     */
    public static void setSearchParameters(SearchParameters searchParameters) {
        searchParametersThreadLocal.set(searchParameters);
    }

    /**
     * @since Search Component 3.x
     */
    public static SearchParameters getSearchParameters() {
        return (SearchParameters)searchParametersThreadLocal.get();
    }

    /**
     * translate hibernate and database exception to HibernateEXC exception
     *
     * @param e exception that must be transelete.
     * @return a HibernateExc
     */
     
     public static HibernateEXC translateException(HibernateException e) {
         if (e == null) {
             return null;
         } else if (e instanceof DataAccessObjectException) {
             return new HibernateEXC(DataAccessObjectException.OBJECT_IN_WORKFLOW_DESC, 
                                     DataAccessObjectException.OBJECT_IN_WORKFLOW_CODE, 
                                     e);
         } else if (e instanceof CacheException) {
             return new HibernateEXC(null, HibernateEXC.CACHE_ERROR, e);
         } else if (e instanceof CallbackException) {
             return new HibernateEXC(null, 
                                     HibernateEXC.HIBERNATE_CALLBACK_ERROR, e);
         } else if (e instanceof IdentifierGenerationException) {
             return new HibernateEXC(null, HibernateEXC.ID_GENERATOR_ERROR, e);
         } else if (e instanceof InstantiationException) {
             return new HibernateEXC(null, HibernateEXC.INSTANTIATION_ERROR, e);
         } else if (e instanceof JDBCException) {
             if (e instanceof ConstraintViolationException) {
                 return new HibernateEXC(null, 
                                         HibernateEXC.CONSTRAINT_VIOLATION_ERROR, 
                                         e);
             } else if (e instanceof GenericJDBCException) {

                 if (e.getCause().getMessage().substring(0, 
                                                         9).equals(HibernateEXC.LOOP_ERROR))
                     return new HibernateEXC(null, HibernateEXC.LOOP_ERROR_ID, 
                                             e);
                 if (e.getCause().getMessage().substring(0, 
                                                         9).equals(HibernateEXC.QUERY_ERROR_CODE))
                     return new HibernateEXC(null, HibernateEXC.QUERY_ERROR_ID, 
                                             e);

                 return new HibernateEXC(null, 
                                         HibernateEXC.GENERIC_SQL_EXECEPTION, 
                                         e);
             } else if (e instanceof JDBCConnectionException) {
                 return new HibernateEXC(null, 
                                         HibernateEXC.JDBC_CONNECTION_ERROR, e);
             } else if (e instanceof LockAcquisitionException) {
                 return new HibernateEXC(null, HibernateEXC.LOCKING_ERROR, e);
             } else if (e instanceof SQLGrammarException) {
                 return new HibernateEXC(null, HibernateEXC.SQL_GRAMMER_ERROR, 
                                         e);
             } else {
                 return new HibernateEXC(null, HibernateEXC.JDBC_ERROR, e);
             }
         } else if (e instanceof MappingException) {
             if (e instanceof PropertyNotFoundException) {
                 return new HibernateEXC(null, 
                                         HibernateEXC.PROPERTY_NOT_FOUND_ERROR, 
                                         e);
             } else {
                 return new HibernateEXC(null, HibernateEXC.MAPPING_ERROR, e);
             }
         } else if (e instanceof NonUniqueObjectException) {
             return new HibernateEXC(null, HibernateEXC.NON_UNIQUE_OBJECT, e);
         } else if (e instanceof NonUniqueResultException) {
             return new HibernateEXC(null, HibernateEXC.NON_UNIQUE_RESULT, e);
         } else if (e instanceof PersistentObjectException) {
             return new HibernateEXC(null, HibernateEXC.PERSISTENT_OBJECT, e);
         } else if (e instanceof PropertyAccessException) {
             return new HibernateEXC(null, HibernateEXC.PROPERTY_ACCESS_ERROR, 
                                     e);
         } else if (e instanceof PropertyValueException) {
             return new HibernateEXC(null, HibernateEXC.PROPERTY_VALUE_ERROR, 
                                     e);
         } else if (e instanceof QueryException) {
             return new HibernateEXC(null, HibernateEXC.QUERY_ERROR, e);
         } else if (e instanceof SerializationException) {
             return new HibernateEXC(null, HibernateEXC.SERIALIZING_ERROR, e);
         } else if (e instanceof StaleObjectStateException) {
             return new HibernateEXC(null, HibernateEXC.STALE_OBJECT_STATE, e);
         } else if (e instanceof TransactionException) {
             return new HibernateEXC(null, HibernateEXC.TRANSIENT_OBJECT, e);
         } else if (e instanceof TransientObjectException) {
             return new HibernateEXC(null, HibernateEXC.TRANSACTION_ERROR, e);
         } else if (e instanceof UnresolvableObjectException) {
             if (e instanceof ObjectDeletedException) {
                 return new HibernateEXC(null, HibernateEXC.OBJECT_DELETED, e);
             } else if (e instanceof ObjectNotFoundException) {
                 return new HibernateEXC(null, HibernateEXC.OBJECT_NOT_FOUND, 
                                         e);
             } else {
                 return new HibernateEXC(null, HibernateEXC.UNRESORVABLE_OBJECT, 
                                         e);
             }
         } else if (e instanceof ValidationFailure) {
             return new HibernateEXC(null, HibernateEXC.VALIDATION_FAILURE, e);
         } else if (e instanceof WrongClassException) {
             return new HibernateEXC(null, HibernateEXC.WRONG_CLASS_ERROR, e);
         }

         return new HibernateEXC();
     }
     
     public static void main(String[] args) {
         init();
     }
}