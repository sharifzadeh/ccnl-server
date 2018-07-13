package edu.dartmouth.ccnl.ridmp.com.util;

import edu.dartmouth.ccnl.ridmp.com.PersistenceUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by ccnl on 3/12/2015.
 */
public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        PersistenceUtils.init();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        PersistenceUtils.destroy();
    }
}
