package edu.dartmouth.ccnl.ridmp.com;

import java.io.Serializable;

public class Token implements Serializable {
    public static final char TOKEN_VARIABLE  = 'v';
    public static final char TOKEN_CONSTANT  = 'c';
    public static final char TOKEN_PROPERTY  = 'p';
    public static final char TOKEN_SYMBOL    = 's';

    public static final char SYMBOL_BINARY  = 'b';
    public static final char SYMBOL_STRING  = 's';
    public static final char OPERATOR_UNARY = 'u';
    public static final char SYMBOL_ETC     = 'e';

    public static final char DATA_INTEGER = 'n';
    public static final char DATA_FLOAT   = 'f';
    public static final char DATA_STRING  = 's';
    public static final char DATA_DATE    = 'd';

    public static final String TOKEN_SEPERATOR = "~";
    public static final String INTERNAL_TOKEN_SEPERATOR = ":";

    private String content;
    private char innerType;
    private char tokenType;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public char getInnerType() {
        return innerType;
    }

    public void setInnerType(char innerType) {
        this.innerType = innerType;
    }

    public char getTokenType() {
        return tokenType;
    }

    public void setTokenType(char tokenType) {
        this.tokenType = tokenType;
    }
}
