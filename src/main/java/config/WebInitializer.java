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

package config;

import com.yammer.metrics.reporting.AdminServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;

/**
 * Performs most of the work normally done by a web.xml file.
 */
public class WebInitializer implements WebApplicationInitializer,
        ServletContextListener {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private GenericWebApplicationContext rootWebApplicationContext;

    public WebInitializer(ApplicationContext applicationContext) {
        rootWebApplicationContext = new GenericWebApplicationContext();
        rootWebApplicationContext.setParent(applicationContext);
        rootWebApplicationContext.refresh();
    }

    @Override public void onStartup(ServletContext servletContext)
            throws ServletException {

        servletContext.setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                rootWebApplicationContext);

        addSpringDispatcherServlet(servletContext);
        addMetricsServlet(servletContext);
        addSpringSecurityFilter(servletContext);
    }

    private void addSpringDispatcherServlet(ServletContext sc) {
        AnnotationConfigWebApplicationContext mvcApplicationContext =
                new AnnotationConfigWebApplicationContext();
        mvcApplicationContext.register(MvcConfiguration.class);
        ServletRegistration.Dynamic dispatcherServlet =
                sc.addServlet(
                        "spring-dispatcher",
                        new DispatcherServlet(mvcApplicationContext));
        dispatcherServlet.setLoadOnStartup(1);
        dispatcherServlet.addMapping("/");
    }

    private void addMetricsServlet(ServletContext sc) {
        ServletRegistration.Dynamic metricsServlet = sc.addServlet("metrics", AdminServlet.class);
        metricsServlet.addMapping("/metrics/*");
    }

    private void addSpringSecurityFilter(ServletContext sc) {
        sc.addFilter("springSecurityFilterChain",
                new DelegatingFilterProxy())
                .addMappingForUrlPatterns(null, false, "/*");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            onStartup(servletContextEvent.getServletContext());
        }
        catch (ServletException e) {
            logger.error("Failed to initialized web application", e);
            System.exit(1);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
    }
}
