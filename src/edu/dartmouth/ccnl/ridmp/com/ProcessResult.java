package edu.dartmouth.ccnl.ridmp.com;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

/**
 * Date: Jun 12, 2005
 *
 * @author Amir  Sharifzadeh
 */
public class ProcessResult implements Serializable {
    private String query;
    private List parameters;

    public ProcessResult(String query, List parameters) {
        this.query = query;
        this.parameters = parameters == null ? new ArrayList() : parameters;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public List getParameters() {
        return parameters;
    }
}
