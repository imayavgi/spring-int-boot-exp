package imaya.exper.svcs;


import com.jamonapi.MonitorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.integration.IntegrationMessageHeaderAccessor;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.ReleaseStrategy;
import org.springframework.integration.store.MessageGroup;
import org.springframework.messaging.Message;

import java.util.Collection;
import java.util.List;


@MessageEndpoint
public class MessageAggregator {

    private static final Log LOGGER = LogFactory.getLog(MessageAggregator.class);

    @Aggregator(inputChannel = "channelThree", outputChannel = "channelFour")
    //public int aggregateMsg(Collection<Integer> vals) {
    public int aggregateMsg(Collection<Message<?>> vals) {

        LOGGER.info("On " + Thread.currentThread().getName() + " starting aggregateMsg for " + vals);
//        for (Message<?> msg : vals) {
//            LOGGER.info(msg.getHeaders());
//            LOGGER.info(msg.getPayload());
//        }
        /* return vals.stream().reduce(0, Integer::sum); */
        return vals.stream().mapToInt(msg -> (Integer) msg.getPayload()).sum();
    }

    @ReleaseStrategy
    public boolean canMessagesBeReleased(List<Message<?>> msgs) {
        LOGGER.info("On " + Thread.currentThread().getName() + " check release strat  for " + msgs.size());
        if  ( msgs.size() == ((Integer)msgs.get(0).getHeaders().get(IntegrationMessageHeaderAccessor.SEQUENCE_SIZE)).intValue() ) {
            LOGGER.info("On " + Thread.currentThread().getName() + " RELEASE TO AGGREGATOR");
            return true;
        }
        LOGGER.info("On " + Thread.currentThread().getName() + " DON'T RELEASE TO AGGREGATOR");
        return false;
    }

}
