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
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Simulates some external resource that can only be access by one process at a time
 */

@Service
public class FakeLimitedResource implements LimitedResource {

    private static final Log LOGGER = LogFactory.getLog(FakeLimitedResource.class);

    private final AtomicBoolean inUse = new AtomicBoolean(false);

    public void use() throws InterruptedException {
        // in a real application this would be accessing/manipulating a shared resource

        if (!inUse.compareAndSet(false, true)) {
            throw new IllegalStateException("Needs to be used by one client at a time");
        }

        try {
            int randomNum = ThreadLocalRandom.current().nextInt(3, 4 + 1);
            LOGGER.info(" DOING WORK THAT WILL TAKE " + 3 * randomNum  +"  sec");
            Thread.sleep((long) (3000 * randomNum));
        } finally {
            inUse.set(false);
        }
    }
}
