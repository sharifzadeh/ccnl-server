package edu.dartmouth.ccnl.ridmp.com;

public class QueryProcessorFactory {
    public static final byte JDBC_PROVIDER = 0;
    public static final byte ENTITY_PROVIDER = 1;
    public static final byte SEARCH_PROVIDER = 2;
    public static final byte LOV_PROVIDER    = 3;
    
    public static QueryProcessor createQueryProcessor(byte type) {
        switch (type) {
            case JDBC_PROVIDER:
                return JDBCQueryProcessor.getInstance();

            case ENTITY_PROVIDER:
            case SEARCH_PROVIDER:
            case LOV_PROVIDER:
                return HibernateQueryProcessor.getInstance();
        }

        throw new IllegalArgumentException("QueryBuilderMetadataProvider dosn't support type : " + type);
    }
}