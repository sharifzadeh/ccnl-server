package edu.dartmouth.ccnl.ridmp.com;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.QueryException;
import net.sf.hibernate.Session;
import net.sf.hibernate.exception.SQLGrammarException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class SearchComponentCommandExecutor implements HibernateCommandExecutor {
    private static final Logger logger = Logger.getLogger(SearchComponentCommandExecutor.class);

    private QueryProcessor queryProcessor = QueryProcessorFactory.createQueryProcessor(QueryProcessorFactory.SEARCH_PROVIDER);

    /**
     * Returns true if and only if result of command is a <code>java.util.List</code> of the {@link edu.dartmouth.ccnl.ridmp.com.SearchParameters}
     * class type.
     */
    public boolean canExecute(Session session, HibernateCommand hibernateCommand) throws HibernateException {
        if (hibernateCommand instanceof SearchableHibernateCommand) {
            SearchParameters searchParameters = PersistenceUtils.getSearchParameters();
            String searchClassName;
            if (searchParameters != null) {
                searchClassName = searchParameters.getClassName();

                if (StringUtils.isNotBlank(searchClassName) &&
                (StringUtils.isNotBlank(searchParameters.getFixedCriteria()) ||
                (StringUtils.isNotBlank(searchParameters.getUserCriteria())))) {
                    SearchableHibernateCommand command = (SearchableHibernateCommand) hibernateCommand;
                    Class resultClass = command.getResultClass(session);

                    if ((resultClass != null) &&
                        (searchClassName.equals(resultClass.getName()))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Shortcuts the command and executes the command according to <em>Search Component 3.x</em> criteria. If user query
     * is invalid it is ommited, and an error message is set.
     */
    public Object executeCommand(Session session, HibernateCommand hibernateCommand) throws HibernateException, HibernateEXC {
        try {
            SearchParameters searchParameters = PersistenceUtils.getSearchParameters();
            String className = searchParameters.getClassName();
            String fixedCriteria = searchParameters.getFixedCriteria();
            String userCriteria = searchParameters.getUserCriteria();

            ProcessResult fixedQuery = queryProcessor.process(className, fixedCriteria, null);

            ProcessResult userQuery = null;
            try {
                userQuery = queryProcessor.process(className, userCriteria, null);
            } catch (QueryParsingException e) {
                logger.error(e, e);
            }

            ProcessResult processResult = queryProcessor.join(fixedQuery, userQuery);
            try {
                List result = executeSearchQuery(session, className, processResult, searchParameters.getMaxResults());
                searchParameters.setUserCriteriaNotValid(false);
                return result;
            } catch (SQLGrammarException e) {
                searchParameters.setUserCriteriaNotValid(true);
                return executeSearchQuery(session, className, fixedQuery, searchParameters.getMaxResults());
            } catch (QueryException e) {
                searchParameters.setUserCriteriaNotValid(true);
                return executeSearchQuery(session, className, fixedQuery, searchParameters.getMaxResults());
            }
        } catch (QueryParsingException e) {
            logger.error(e, e);
            throw new HibernateEXC("error in parsing query", 0, e);
        }
    }

    private List executeSearchQuery(Session session, String className, ProcessResult processResult, int maxResult) throws HibernateException {
        String query = queryProcessor.createExecutableQuery(className, processResult.getQuery());

        HibernateCommand command = new ListFindByQuerySearchableHibernateCommand(
            query, processResult.getParameters().toArray(), maxResult);

        return (List) command.execute(session);
    }

    private static HibernateCommandExecutor instance = new SearchComponentCommandExecutor();

    public static HibernateCommandExecutor getInstance() {
        if (instance == null) {
            instance = new SearchComponentCommandExecutor();
        }
        return instance;
    }

    private SearchComponentCommandExecutor() {
    }
}
