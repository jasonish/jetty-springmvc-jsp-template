package ca.unx.template.web;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ca.unx.template.DummyService;

/**
 * Handles requests for the application home page.
 */
@Controller
public class DummyController {

    private static final Logger logger = LoggerFactory
            .getLogger(DummyController.class);

    /*
     * Autowire in the dummy service from the root application context.
     */
    @Autowired
    private DummyService dummyService = null;

    /**
     * Simple controller for "/" that returns a JSP view.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Locale locale, Model model) {
        logger.info("Welcome home! the client locale is " + locale.toString());

        Date date = new Date();
        DateFormat dateFormat =
                DateFormat.getDateTimeInstance(DateFormat.LONG,
                        DateFormat.LONG, locale);

        String formattedDate = dateFormat.format(date);

        model.addAttribute("serverTime", formattedDate);

        model.addAttribute("dummyService", dummyService);

        /*
         * When using embedded Jetty there can be issues with JSP tag libraries.
         * We'll put some stuff here to test the c:forEach tag.
         */
        model.addAttribute("someItems", new String[] { "one", "two", "three" });

        return "home";
    }

}
