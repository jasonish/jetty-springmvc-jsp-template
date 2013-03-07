/*
 * The MIT License (MIT)
 * Copyright (C) 2012 Jason Ish
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the “Software”), to deal in the Software without
 * restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ca.unx.template;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.WebApplicationContext;

import config.RootConfiguration;

/**
 * Application entry point.
 *
 * This class implements and bootstraps the Spring application context. It also
 * listens for application events to ensure the SpringMVC application context is
 * successfully loaded.
 */
public class Main {

    /**
     * Flag that will be set to true when the web application context
     * (SpringMVC) is refreshed.
     */
    static boolean webApplicationContextInitialized = false;

    public static void main(String[] args) throws Exception {

        final Logger logger = LoggerFactory.getLogger("main");

        try {
            AnnotationConfigApplicationContext applicationContext =
                    new AnnotationConfigApplicationContext();

            /*
             * One problem with SpringMVC is it creates its own application
             * context, and so it can end up failing but our application will
             * keep running.
             * 
             * To detect the case where the SpringMVC's web application context
             * fails we'll listen for ContextRefreshEvents and set a flag when
             * we see one.
             */
            applicationContext
                    .addApplicationListener(
                            new ApplicationListener<ContextRefreshedEvent>() {
                                @Override
                                public void onApplicationEvent(
                                        ContextRefreshedEvent event) {
                                    if (event
                                            .getApplicationContext() instanceof WebApplicationContext) {
                                        webApplicationContextInitialized = true;
                                    }
                                }
                            });

            applicationContext.registerShutdownHook();
            applicationContext.register(RootConfiguration.class);
            applicationContext.refresh();

            if (!webApplicationContextInitialized) {
                logger.error(
                        "Web application context not initialized. Exiting.");
                System.exit(1);
            }

            logger.info("Running.");
        } catch (Exception e) {
            logger.error("Error starting application", e);
            System.exit(1);
        }
    }
}
