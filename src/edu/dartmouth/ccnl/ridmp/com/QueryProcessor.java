package edu.dartmouth.ccnl.ridmp.com;

import java.util.Map;

public interface QueryProcessor {
    ProcessResult process(String source, String whereClause, Map parameters) throws QueryParsingException;

    ProcessResult join(ProcessResult first, ProcessResult second);

    void validate(String source, String whereClause) throws QueryParsingException;

    String createExecutableQuery(String source, String processedQuery);

    ProcessResult createExecutableQuery(String source, String qbResult, Map parameters) throws QueryParsingException;
}
