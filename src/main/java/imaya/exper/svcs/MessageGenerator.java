package imaya.exper.svcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MessageGenerator {

    private static final Log LOGGER = LogFactory.getLog(MessageGenerator.class);

    public List<String> getEventNames() {

        LOGGER.info("On " + Thread.currentThread().getName() + " getEventNames");
        return Arrays.asList("First Event","Second Event","Third Event", "Fourth Event", "Fifth Event");
    }
}
