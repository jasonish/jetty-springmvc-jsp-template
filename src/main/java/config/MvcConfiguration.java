package config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * The SpringMVC application context.
 *
 * This is the annotation variation of configuring the SpringMVC application
 * context. An XML configuration is imported so XML based configuration can
 * still be used.
 *
 * Any @Controller classes will be picked up by component scanning. All other
 * components are ignored as they will be picked up by the root application
 * context.
 */
@EnableWebMvc
@Configuration
@ComponentScan(useDefaultFilters = false, basePackages = { "ca.unx.template" }, includeFilters = { @ComponentScan.Filter(Controller.class) })
@ImportResource("classpath:META-INF/spring/servlet-context.xml")
public class MvcConfiguration extends WebMvcConfigurerAdapter {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        /*
         * Server static resources from the src/main/resources/webapp/resources
         * directory. Perhaps rename resources to static.
         */
        registry.addResourceHandler("/resources/**").addResourceLocations(
                "/resources/");

        /*
         * Favicon mapping.
         */
        registry.addResourceHandler("/favicon.ico").addResourceLocations(
                "/resources/favicon.ico");

    }

    /**
     * Basic setup for JSP views.
     */
    @Bean
    public InternalResourceViewResolver configureInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
