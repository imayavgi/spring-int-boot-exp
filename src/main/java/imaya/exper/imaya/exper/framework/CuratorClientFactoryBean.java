package imaya.exper.imaya.exper.framework;

import imaya.exper.svcs.MessageProcessingClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import javax.annotation.PreDestroy;

public class CuratorClientFactoryBean extends AbstractFactoryBean<CuratorFramework> {

    private static final Log LOGGER = LogFactory.getLog(AbstractFactoryBean.class);

    private String connectionString;

    private RetryPolicy connectionRetryPolicy;

    private CuratorFramework client;

    public CuratorClientFactoryBean(String connectionString, RetryPolicy policy) {
        this.connectionString = connectionString;
        this.connectionRetryPolicy = policy;
    }

    @Override
    public CuratorFramework createInstance() throws Exception {
        client =  CuratorFrameworkFactory.newClient(connectionString, connectionRetryPolicy);
        if(client != null) {
            client.start();
            LOGGER.info(" Created and started .. Check to see it is conncted and working");
            client.blockUntilConnected();
            client.checkExists().forPath("/");
            LOGGER.info("ZK CLIENT SUCCESSFULLY CREATED AND CONNECTED");
        } else {
            throw new IllegalStateException("Attempt to validate Curator client before creating the client.");
        }
        return client;
    }

    @PreDestroy
    public void destroy() throws Exception {
        try {
            logger.info("Closing Curator client");
            if (client != null) {
                client.close();
            }
        } catch (final Exception e) {
            logger.error("Error closing Curator client: ", e);
        }
    }

    @Override
    public Class<?> getObjectType() {
        return CuratorFramework.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
