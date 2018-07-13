package edu.dartmouth.ccnl.ridmp.com;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

public class DefaultCommandExecutor implements HibernateCommandExecutor {
    public boolean canExecute(Session session, HibernateCommand command) throws HibernateException {
        return true;
    }

    public Object executeCommand(Session session, HibernateCommand command) throws HibernateException {
        return command.execute(session);
    }

    private static HibernateCommandExecutor instance;

    public static HibernateCommandExecutor getInstance() {
        if (instance == null) {
            instance = new DefaultCommandExecutor();
        }
        return instance;
    }

    private DefaultCommandExecutor() {
    }
}