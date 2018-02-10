/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package imaya.exper.svcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.integration.annotation.MessageEndpoint;
import org.springframework.integration.annotation.Poller;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
@Scope(value="thread", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class MessageProcessingClient {

    private static final Log LOGGER = LogFactory.getLog(MessageProcessingClient.class);

    private InterProcessMutex lock;

    @Autowired
    private CuratorFramework client;

    String lockPath = "/examples/locks";

    @Autowired
    private  LimitedResource resource;

    private  String clientName;

    @PostConstruct
    public void init() {

        //client = CuratorFrameworkFactory.newClient("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
        //client.start();
        LOGGER.info("On " + Thread.currentThread().getName() + " Zookeeper client assigned to worker ");

        clientName = "CLIENT_" + Thread.currentThread().getName();
        lock = new InterProcessMutex(client, lockPath);

    }

    public void doWork(long time, TimeUnit unit) throws Exception {
        LOGGER.info("On " + Thread.currentThread().getName() + " Zookeeper client doWork " + this);

        if (!lock.acquire(time, unit)) {
            throw new IllegalStateException(clientName + " could not acquire the lock");
        }
        try {
            LOGGER.info(clientName + " has the lock");
            resource.use();
        } finally {
            LOGGER.info(clientName + " releasing the lock");
            lock.release(); // always release the lock in a finally block
        }

    }
}
