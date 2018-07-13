package edu.dartmouth.ccnl.ridmp.com;

public class QueryParsingException extends ParentEXC {
    public QueryParsingException() {
        super();
    }

    public QueryParsingException(String message) {
        super(message);
    }

    public QueryParsingException(Throwable throwable) {
        super(null, 0,throwable);
    }
}
