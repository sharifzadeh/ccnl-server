package edu.dartmouth.ccnl.ridmp.com;

import org.apache.commons.lang.StringUtils;

public class JDBCQueryProcessor extends AbstractQueryProcessor {
    protected void executeQuery(String source, ProcessResult processResult) throws QueryParsingException {
        String query = createExecutableQuery(source, processResult.getQuery());
        query = query.replaceAll(" \\*  ", " count(*) ");

        try {
            persistenceManager.executeJDBC(query, processResult.getParameters().toArray());
        } catch (HibernateEXC hibernateEXC) {
            throw new QueryParsingException(hibernateEXC);
        }
    }

    protected String getExecutableQueryPattern() {
        return "select * from {0} where {1}";
    }

    protected String getExecutableQueryPatternNoWhere() {
        return "select * from {0} {1}";
    }

    protected String getPropertyQL(Token token) {
        return token.getContent();
    }

    public void applyGroupingOrderClause(ProcessResult processResult, String groupingOrderClause) {
        if (processResult != null) {
            if (StringUtils.isNotBlank(groupingOrderClause)) {
                String whereClause = processResult.getQuery();

                if (whereClause == null) {
                    whereClause = "";

                    int idx = StringUtils.lastIndexOf(whereClause, SYM_SOP_ORD_BOTH);
                    if (idx != -1) {
                        int endIndex = idx + SYM_SOP_ORD_BOTH.length() + 1;
                        String firstPart = whereClause.substring(0, endIndex);
                        String orderByClause = whereClause.substring(endIndex);
                        whereClause = firstPart + groupingOrderClause + orderByClause;
                    } else {
                        whereClause = (whereClause + " " + SYM_SOP_ORD_BOTH + " " + groupingOrderClause).trim();
                        if (whereClause.endsWith(",")) {
                            whereClause = whereClause.substring(0, whereClause.length() - 1);
                        }
                    }
                    processResult.setQuery(whereClause);
                }
            }
        }
    }

    // singletone instance;
    private static QueryProcessor instance;

    public static QueryProcessor getInstance() {
        if (instance == null) {
            instance = new JDBCQueryProcessor();
        }

        return instance;
    }

    private JDBCQueryProcessor() {
    }
}
