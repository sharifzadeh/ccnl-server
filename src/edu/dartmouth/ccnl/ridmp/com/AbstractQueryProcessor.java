package edu.dartmouth.ccnl.ridmp.com;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractQueryProcessor implements QueryProcessor, QueryBuilderConstants {
    public static final String SYM_SOP_BET_BOTH = "between";
    public static final String SYM_SOP_ORD_BOTH = "order by";
    public static final String SYM_SOP_NOT_BOTH = "not";
    public static final String SYM_SOP_OP_BOTH = "(";
    public static final String SYM_SOP_CL_BOTH = ")";
    public static final String SYM_SOP_SEP_BOTH = ",";

    public static final String SYM_BOP_EQ_BOTH = "=";
    public static final String SYM_BOP_NEQ_BOTH = "!=";
    public static final String SYM_BOP_GT_BOTH = ">";
    public static final String SYM_BOP_GE_BOTH = ">=";
    public static final String SYM_BOP_LT_BOTH = "<";
    public static final String SYM_BOP_LE_BOTH = "<=";
    public static final String SYM_BOP_ADD_BOTH = "+";
    public static final String SYM_BOP_SUB_BOTH = "-";
    public static final String SYM_BOP_MUL_BOTH = "*";
    public static final String SYM_BOP_DIV_BOTH = "/";

    public static final String SYM_BOP_STR_CNT_BOTH = "like";
    public static final String SYM_BOP_STR_NCNT_BOTH = "not like";
    public static final String SYM_BOP_STR_STW_BOTH = "like";
    public static final String SYM_BOP_STR_NSTW_BOTH = "not like";
    public static final String SYM_BOP_STR_ENW_BOTH = "like";
    public static final String SYM_BOP_STR_NENW_BOTH = "not like";

    public static final String SYM_UOP_NULL_BOTH = "is null";
    public static final String SYM_UOP_NNULL_BOTH = "is not null";
    public static final String SYM_UOP_DESC_BOTH = "desc";
    public static final String SYM_UOP_ASC_BOTH = "asc";

    public static final String SYM_JUN_AND_BOTH = "and";
    public static final String SYM_JUN_OR_BOTH = "or";

    public static final Map SYMBOLES_MAPPING =
        ConstantUtils.getConstantsMap(QueryBuilderConstants.class, JDBCQueryProcessor.class, "SYM_", "_BOTH");

    public static final String PREPARED_PARAMETER_SIGN = "?";

    protected final PersistenceManager persistenceManager;

    protected AbstractQueryProcessor() {
        persistenceManager = PersistenceManagerImp.getInstance();
    }

    public ProcessResult process(String source, String whereClause, Map parameters) throws QueryParsingException {
        QueryScanner scanner = new QueryScanner(whereClause);

        Token lastSymbol = null;
        List list = new ArrayList();
        StringBuffer result = new StringBuffer();

        try {
            while (scanner.hasNext()) {
                Token token = (Token) scanner.next();
                result.append(BLANK);

                switch (token.getTokenType()) {
                    case Token.TOKEN_SYMBOL:
                        result.append(getXQLSymbol(token.getContent()));
                        lastSymbol = token;
                        break;

                    case Token.TOKEN_VARIABLE:
                        result.append(getXQLVariable(token, parameters, list, lastSymbol));
                        lastSymbol = null;
                        break;

                    case Token.TOKEN_CONSTANT:
                        result.append(getXQLValue(token, list, lastSymbol));
                        lastSymbol = null;
                        break;

                    case Token.TOKEN_PROPERTY:
                        result.append(getPropertyQL(token));
                        lastSymbol = null;
                        break;
                }
            }
        } catch (RuntimeException e) {
            throw new QueryParsingException(e);
        }

        return new ProcessResult(result.toString(), list);
    }

    public ProcessResult join(ProcessResult first, ProcessResult second) {
        if ((first == null) || (StringUtils.isEmpty(first.getQuery()))) {
            return second;
        }

        if ((second == null) || (StringUtils.isEmpty(second.getQuery()))) {
            return first;
        }

        String fwhere = first.getQuery().trim();
        int fidx = StringUtils.indexOf(fwhere, SYM_SOP_ORD_BOTH);

        String swhere = second.getQuery().trim();
        int sidx = StringUtils.indexOf(swhere, SYM_SOP_ORD_BOTH);

        if (sidx != -1) {
            if (fidx != -1) {
                fwhere = StringUtils.substring(fwhere, 0, fidx);
            }
        } else if (fidx != -1) {
            swhere += BLANK + StringUtils.substring(fwhere, fidx);
            fwhere = StringUtils.substring(fwhere, 0, fidx);
        }

        StringBuffer finalWhere = new StringBuffer();
        if (StringUtils.isNotEmpty(fwhere)) {
            finalWhere.append(SYM_SOP_OP_BOTH).append(fwhere).append(SYM_SOP_CL_BOTH);
        }

        if ((sidx == 0) || (fidx == 0)) {
            finalWhere.append(BLANK);
        } else {
            finalWhere.append(BLANK).
                append(SYM_JUN_AND_BOTH).
                append(BLANK);
        }
        finalWhere.append(swhere);

        List joinList = new ArrayList(first.getParameters());
        joinList.addAll(second.getParameters());

        return new ProcessResult(finalWhere.toString(), joinList);
    }

    public void validate(String source, String whereClause) throws QueryParsingException {
        ProcessResult processResult = process(source, whereClause, null);

        executeQuery(source, processResult);
    }

    protected abstract void executeQuery(String source, ProcessResult processResult) throws QueryParsingException;

    public String createExecutableQuery(String source, String processedQuery) {
        String pattern = getExecutableQueryPattern();

        if (processedQuery != null) {
            processedQuery = processedQuery.trim();
            if (StringUtils.isBlank(processedQuery) || processedQuery.startsWith(SYM_SOP_ORD_BOTH)) {
                pattern = getExecutableQueryPatternNoWhere();
            }
        }

        return MessageFormat.format(pattern, new Object[]{source, processedQuery});
    }
   
    protected abstract String getExecutableQueryPattern();

    protected abstract String getExecutableQueryPatternNoWhere();

    public ProcessResult createExecutableQuery(String source, String qbResult, Map parameters) throws QueryParsingException {
        return process(source, qbResult, parameters);
    }

    protected final Float getFloatToken(String token) throws QueryParsingException {
        try {
            return new Float(token);
        } catch (NumberFormatException e) {
            throw new QueryParsingException(e);
        }
    }

    protected final Integer getIntegerToken(String token) throws QueryParsingException {
        try {
            return new Integer(token);
        } catch (NumberFormatException e) {
            throw new QueryParsingException(e);
        }
    }

    protected final String getStringToken(String content, Token lastSymbol) {
        if ((lastSymbol != null) && (lastSymbol.getInnerType() == Token.SYMBOL_STRING)) {

            String operator = lastSymbol.getContent();
            if ((SYM_BOP_STR_CNT.equals(operator)) || (SYM_BOP_STR_NCNT.equals(operator))) {
                content = "%" + content + "%";
            } else if ((SYM_BOP_STR_STW.equals(operator)) || (SYM_BOP_STR_NSTW.equals(operator))) {
                content = content + "%";
            } else if ((SYM_BOP_STR_ENW.equals(operator)) || (SYM_BOP_STR_NENW.equals(operator))) {
                content = "%" + content;
            }
        }
        return content;
    }

    // process internal methods {
    protected String getXQLSymbol(String symbol) throws QueryParsingException {
        String result = (String) SYMBOLES_MAPPING.get(symbol);
        if (result == null) {
            throw new QueryParsingException();
        }
        return result;
    }

    protected String getXQLVariable(Token token, Map map, List params, Token lastSymbol) throws QueryParsingException {
        String tokenContent = token.getContent();
        boolean process = map != null;
        if (process) {
            tokenContent = (String) map.get(token.getContent());
        }

        Object value;
        switch (token.getInnerType()) {
            case Token.DATA_DATE:
                value = process ? ReportUtils.getDateToken(tokenContent) : new java.sql.Date(System.currentTimeMillis());
                break;

            case Token.DATA_FLOAT:
                value = process ? getFloatToken(tokenContent) : new Float(0);
                break;

            case Token.DATA_INTEGER:
                value = process ? getIntegerToken(tokenContent) : new Integer(0);
                break;

            case Token.DATA_STRING:
                value = getStringToken(tokenContent, lastSymbol);
                break;

            default:
                throw new QueryParsingException();
        }

        params.add(value);

        return PREPARED_PARAMETER_SIGN;
    }

    protected String getXQLValue(Token token, List params, Token lastSymbol) throws QueryParsingException {

        switch (token.getInnerType()) {
            case Token.DATA_DATE:
                params.add(ReportUtils.getDateToken(token.getContent()));
                break;

            case Token.DATA_FLOAT:
                params.add(getFloatToken(token.getContent()));
                break;

            case Token.DATA_INTEGER:
                params.add(getIntegerToken(token.getContent()));
                break;

            case Token.DATA_STRING:
                params.add(getStringToken(token.getContent(), lastSymbol));
                break;

            default:
                throw new QueryParsingException();
        }

        return PREPARED_PARAMETER_SIGN;
    }

    protected abstract String getPropertyQL(Token token);
    // } process internal methods
}
