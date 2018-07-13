package edu.dartmouth.ccnl.ridmp.com;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.lang.StringUtils;

class QueryScanner implements Iterator, QueryBuilderConstants {
    private static final String TOKEN_SEPERATOR = Token.TOKEN_SEPERATOR;
    private static final String INTERNAL_TOKEN_SEPERATOR = Token.INTERNAL_TOKEN_SEPERATOR;

    private String[] tokens;
    private int index;

    public QueryScanner(String query) {

        String optimze = optimizeQuery(query);

        if (StringUtils.isEmpty(optimze)) {
            tokens = null;
            index = -1;
        } else {
            tokens = optimze.split(TOKEN_SEPERATOR);
            index = 0;
        }
    }

    private String optimizeQuery(String query) {
        if (StringUtils.isEmpty(query)) {
            query = "";

        } else {
            query = query.trim();

            if (query.endsWith(TOKEN_SEPERATOR)) {
                query = query.substring(0, query.length() - 1);
            }

            if (query.startsWith(TOKEN_SEPERATOR)) {
                query = query.substring(1);
            }
        }
        return query;
    }

    public void remove() {
    }

    public boolean hasNext() {
        return (index != -1) && (index < tokens.length);
    }

    public Object next() {
        if ((index == -1) || (index >= tokens.length)) {
            throw new NoSuchElementException();
        }

        String tmpData = tokens[index++];
        String[] tmpSplit = tmpData.split(INTERNAL_TOKEN_SEPERATOR);

        if (tmpSplit.length != 3) {
            throw new IllegalArgumentException("invalid token syntax");
        }

        Token token = new Token();

        token.setTokenType(tmpSplit[0].charAt(0));
        token.setInnerType(tmpSplit[1].charAt(0));
        token.setContent(tmpSplit[2]);

        return token;
    }
}
