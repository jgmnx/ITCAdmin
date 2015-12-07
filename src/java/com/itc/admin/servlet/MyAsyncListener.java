package com.itc.admin.servlet;

import java.io.IOException;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebListener;

/**
 *
 * @author jgmnx
 */
@WebListener
public class MyAsyncListener implements AsyncListener {

    @Override
    public void onComplete(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener - onComplete");
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onTimeout(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener - onTimeout");
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onError(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener - onError");
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
        }
    }

    @Override
    public void onStartAsync(AsyncEvent event) throws IOException {
        System.out.println("MyAsyncListener - onStartAsync");
        if (event.getThrowable() != null) {
            event.getThrowable().printStackTrace();
        }
    }


}
