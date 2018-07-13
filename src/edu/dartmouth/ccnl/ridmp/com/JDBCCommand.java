package edu.dartmouth.ccnl.ridmp.com;

import java.sql.Connection;
import java.sql.SQLException;

public interface JDBCCommand {
    Object execute(Connection connection) throws SQLException;
}