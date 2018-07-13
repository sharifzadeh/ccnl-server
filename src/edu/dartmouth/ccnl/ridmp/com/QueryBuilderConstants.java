package edu.dartmouth.ccnl.ridmp.com;

public interface QueryBuilderConstants {
    String BLANK = " ";

    String SYM_SOP_BET = "between";
    String SYM_SOP_ORD = "order";
    String SYM_SOP_NOT = "not";
    String SYM_SOP_OP = "open";
    String SYM_SOP_CL = "close";
    String SYM_SOP_SEP = "seperator";

    String SYM_BOP_EQ = "eq";
    String SYM_BOP_NEQ = "not.eq";
    String SYM_BOP_GT = "gt";
    String SYM_BOP_GE = "ge";
    String SYM_BOP_LT = "lt";
    String SYM_BOP_LE = "le";
    String SYM_BOP_ADD = "add";
    String SYM_BOP_SUB = "sub";
    String SYM_BOP_MUL = "mul";
    String SYM_BOP_DIV = "div";

    String SYM_BOP_STR_CNT = "cnt";
    String SYM_BOP_STR_NCNT = "not.cnt";
    String SYM_BOP_STR_STW = "stw";
    String SYM_BOP_STR_NSTW = "not.stw";
    String SYM_BOP_STR_ENW = "enw";
    String SYM_BOP_STR_NENW = "not.enw";
    String SYM_BOP_STR_CONCAT = "concat";

    String SYM_UOP_NULL = "is.null";
    String SYM_UOP_NNULL = "is.not.null";
    String SYM_UOP_DESC = "desc";
    String SYM_UOP_ASC = "asc";

    String SYM_JUN_AND = "and";
    String SYM_JUN_OR = "or";
}
