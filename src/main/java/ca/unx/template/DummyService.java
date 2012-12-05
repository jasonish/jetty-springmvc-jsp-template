package ca.unx.template;

import org.springframework.stereotype.Service;

/**
 * A dummy service for testing Spring injection.
 */
@Service
public class DummyService {

    private static DummyService instance = null;

    public DummyService() {
        /*
         * This is here just to make sure we don't get instantiated 2x while
         * playing with Spring application contexts.
         */
        if (instance != null) {
            throw new RuntimeException(
                    "DummyServer has already been instantiated.");
        }
        instance = this;
    }

    public String getMessage() {
        return "I am a dummy service.";
    }

}
