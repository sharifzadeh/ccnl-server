package edu.dartmouth.ccnl.ridmp.com;

public class HibernateEXC extends ParentEXC {
    public static final int HIBERNATE_FINALIZATION_ERROR = 1000;
    public static final int HIBERNATE_RETRIEVAL_ERROR = 1001;
    public static final int HIBERNATE_STORAGE_ERROR = 1002;
    public static final int HIBERNATE_INITIALIZATION_ERROR = 1003;


    /**
     * @reason : Handling the loop which will be occured in many-to-one
     *           relations.
     */
     public static final String LOOP_ERROR = "ORA-04092";
     public static final int    LOOP_ERROR_ID = 5600;

    /**
     * @reason : Handling the errors of query.
     */
     public static final String QUERY_ERROR_CODE = "ORA-00920";
     public static final int    QUERY_ERROR_ID = 5601;

    /*
     * added by Amir  Sharifzadeh - start
     * do not remove, sued by mfa.com.util.hibernate.PersistenceUtils
     */
    // evironment error - non resolvable
    public static final int CACHE_ERROR = 5500;
    public static final int ID_GENERATOR_ERROR = 5510;          // should not happen
    public static final int JDBC_CONNECTION_ERROR = 5520;

    // need change in code and re-compile
    public static final int MAPPING_ERROR = 5400;
    public static final int PROPERTY_NOT_FOUND_ERROR = 5401;
    public static final int SERIALIZING_ERROR = 5410;           // if a binary data field is not serializable
    public static final int INSTANTIATION_ERROR = 5420;         // no default constructor, or exception in default constructor
    public static final int PROPERTY_ACCESS_ERROR = 5430;       // getter or setter is not public

    // should not happen - feature not used
    public static final int HIBERNATE_CALLBACK_ERROR = 5300;
    public static final int VALIDATION_FAILURE = 5310;
    public static final int STALE_OBJECT_STATE = 5320;          // optimistic lockibng
    public static final int SQL_GRAMMER_ERROR = 5330;           // no direct JDBC

    // program logic bug
    public static final int NON_UNIQUE_OBJECT = 5200;           // more than one objects of the same type with same pk in a session
    public static final int NON_UNIQUE_RESULT = 5210;           // a query which must return unique, returns a list
    public static final int UNRESORVABLE_OBJECT = 5230;
    public static final int OBJECT_DELETED = 5231;              // requested object is deleted in current session
    public static final int OBJECT_NOT_FOUND = 5232;            // requested object is not found
    public static final int PROPERTY_VALUE_ERROR = 5240;        // such as not null
    public static final int CONSTRAINT_VIOLATION_ERROR = 5250;  // unique constraint, relation, ...

    // hibernate usage bug
    public static final int PERSISTENT_OBJECT = 5100;           // object must be persistent
    public static final int TRANSIENT_OBJECT = 5110;            // object must be transient
    public static final int QUERY_ERROR = 5120;                 // invalid HQL query
    public static final int LOCKING_ERROR = 5130;               // item locked on database
    public static final int WRONG_CLASS_ERROR = 5140;           // error in hierarchical query

    // unknown state
    public static final int JDBC_ERROR = 5000;                  // unknown JDBC error - should not happen
    public static final int GENERIC_SQL_EXECEPTION = 5001;      // unknown JDBC error
    public static final int TRANSACTION_ERROR = 5010;           // error in transaction
    /*
     * added by Amir  Sharifzadeh - end
     */
    
        

    public HibernateEXC() {
    }

    public HibernateEXC(int errNum) {
        this.errorNumber = errNum;
    }

    public HibernateEXC(String errMsg, int errNum) {
        this.errorMessage = errMsg;
        this.errorNumber = errNum;
    }

    public HibernateEXC(String errMsg, int errNum, Throwable errThrow) {
        super(errMsg);
        this.errorMessage = errMsg;
        this.errorNumber = errNum;
        this.errorThrowable = errThrow;
    }
}