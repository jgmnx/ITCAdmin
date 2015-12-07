package com.itc.admin.servlet;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author jgmnx
 */
@WebListener
public class MyAsyncThreadPool implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ThreadPoolExecutor threadPoolExec = new ThreadPoolExecutor(100, 200, 10000L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(100));
        sce.getServletContext().setAttribute("myAsyncThreadPool", threadPoolExec);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ((ThreadPoolExecutor) sce.getServletContext().getAttribute("myAsyncThreadPool")).shutdown();
    }

}
