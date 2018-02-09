package imaya.exper.svcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Splitter;

import java.util.Arrays;
import java.util.List;

@MessageEndpoint
public class MessageSplitter {

    private static final Log LOGGER = LogFactory.getLog(MessageAggregator.class);

    @Splitter(inputChannel = "channelOne", outputChannel = "queueChannel")
    public List<String> splitMessage(List<String>  val /*String val*/) {

        LOGGER.info("On " + Thread.currentThread().getName() + " starting splitting  for " + val);

        //return Arrays.asList(val.split(","));
        return val;
    }
}
