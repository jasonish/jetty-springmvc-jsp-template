package config;

import com.yammer.metrics.reporting.AdminServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

/**
 * Configure the embedded Jetty server and the SpringMVC dispatcher servlet.
 */
@Configuration
public class JettyConfiguration implements ApplicationContextAware {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;

    @Bean
    public WebAppContext jettyWebAppContext() throws IOException {
        WebAppContext ctx = new WebAppContext();
        ctx.setContextPath("/");
        ctx.setWar(new ClassPathResource("webapp").getURI().toString());

        /* We can add the Metrics servlet right away. */
        ctx.addServlet(AdminServlet.class, "/metrics/*");

        return ctx;
    }

    @Bean
    public ServletHolder dispatcherServlet() {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setParent(applicationContext);
        ctx.register(MvcConfiguration.class);
        DispatcherServlet servlet = new DispatcherServlet(ctx);
        ServletHolder holder = new ServletHolder("dispatcher-servlet", servlet);
        holder.setInitOrder(1);
        return holder;
    }

    @Bean
    public LifeCycle.Listener lifeCycleStartedListener() {
        return new AbstractLifeCycle.AbstractLifeCycleListener() {
            @Override
            public void lifeCycleStarted(LifeCycle event) {
                try {
                    ServletHolder dispatcherServlet = dispatcherServlet();
                    jettyWebAppContext().getServletHandler()
                            .addServletWithMapping(dispatcherServlet, "/");
                    dispatcherServlet.start();
                } catch (Exception e) {
                    logger.error(
                            "Failed to start Spring MVC dispatcher servlet", e);
                }
            }
        };
    }

    /**
     * Create your Jetty connectors here. This version creates the most basic
     * SelectChannelConnector.
     *
     * This method returns an array of connectors. This allows you to listen
     * with https on one port and plain http on another.
     */
    @Bean
    public Connector[] jettyConnectors() {
        SelectChannelConnector connector0 = new SelectChannelConnector();
        connector0.setPort(8080);

        return new Connector[]{connector0};
    }

    /**
     * Jetty Server bean.
     *
     * Instantiate the Jetty server.
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server jettyServer() throws IOException {
        Server server = new Server();
        server.setHandler(jettyWebAppContext());

        /* Add a life cycle listener so we can register the SpringMVC dispatcher
         * servlet after the web application context has been started. */
        server.addLifeCycleListener(lifeCycleStartedListener());
        server.setConnectors(jettyConnectors());
        return server;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }
}
