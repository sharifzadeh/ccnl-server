package edu.dartmouth.ccnl.ridmp.com;

public class HibernateQueryProcessor extends AbstractQueryProcessor {
    private static final String HQL_OBJECT_ALIAS = "theObject";

    protected void executeQuery(String source, ProcessResult processResult) throws QueryParsingException {
        String query = "select count(" + HQL_OBJECT_ALIAS + ")" + createExecutableQuery(source, processResult.getQuery());

        try {
            persistenceManager.findByQuery(query, processResult.getParameters().toArray());
        } catch (HibernateEXC hibernateEXC) {
            throw new QueryParsingException(hibernateEXC);
        }
    }

    protected String getExecutableQueryPattern() {
        return "from {0} as " + HQL_OBJECT_ALIAS + " where {1}";
    }

    protected String getExecutableQueryPatternNoWhere() {
        return "from {0} as " + HQL_OBJECT_ALIAS + " {1}";
    }

    protected String getPropertyQL(Token token) {
        return HQL_OBJECT_ALIAS + "." + token.getContent();
    }

    // singletone instance;
    private static QueryProcessor instance;

    public static QueryProcessor getInstance() {
        if (instance == null) {
            instance = new HibernateQueryProcessor();
        }

        return instance;
    }

    private HibernateQueryProcessor() {
    }
}
