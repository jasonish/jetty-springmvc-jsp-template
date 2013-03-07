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

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.stereotype.Controller;

/**
 * The root application context.
 *
 * Beans can also be configured by XML in root-context.xml which is imported by
 * this context class.
 *
 * Component scanning is also done to pickup any components other than
 *
 * @Controllers. @Controllers will be picked up by the SpringMVC context.
 */
@Configuration
@ImportResource({"classpath:META-INF/spring/root-context.xml",
        "classpath:META-INF/spring/security.xml"})
@Import({config.JettyConfiguration.class})
@ComponentScan(basePackages = {"ca.unx.template"},
        excludeFilters = {@ComponentScan.Filter(Controller.class)})
public class RootConfiguration {
}
