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
import org.springframework.stereotype.Service;

/**
 * A dummy service for testing Spring injection.
 */
@Service
public class EchoService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    private static EchoService instance = null;

    public EchoService() {
        /**
         * This is here to detect if we get instantiated 2x.  While starting
         * with SpringMVC with Jetty embedded I found that I could get into
         * a case where the web application would recreate all the beans
         * already created outside of the web app context.  The component
         * scan filters should prevent this.
         */
        if (instance != null) {
            throw new RuntimeException(
                    "DummyServer has already been instantiated.");
        }
        instance = this;

        logger.info("EchoService initialized.");
    }

    public String echo(String msg) {
        return new String(msg);
    }

}
