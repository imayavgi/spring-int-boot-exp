package imaya.exper.svcs;


import com.jamonapi.MonitorFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;

import java.util.concurrent.TimeUnit;

@MessageEndpoint
public class MessageProcessor {

    private static final Log LOGGER = LogFactory.getLog(MessageProcessor.class);

    @Autowired
    private MessageProcessingClient mpClient;

    @ServiceActivator(inputChannel = "queueChannel", poller = @Poller(fixedDelay = "10", maxMessagesPerPoll = "1", taskExecutor = "pooledThreadExecutor"), outputChannel = "channelThree")
    public int processMsg(String inmsg) {

        LOGGER.info("On " + Thread.currentThread().getName() + " starting processMsg " + inmsg);
        try {
            //Thread.sleep(4000);
            mpClient.doWork(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("On " + Thread.currentThread().getName() + " completed processMsg for " + inmsg);

        return inmsg.length();
    }
}
