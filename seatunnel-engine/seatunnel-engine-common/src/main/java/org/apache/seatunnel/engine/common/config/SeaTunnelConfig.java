/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.engine.common.config;

import org.apache.seatunnel.engine.common.Constant;

import com.hazelcast.config.Config;
import com.hazelcast.logging.ILogger;
import com.hazelcast.logging.Logger;

import java.io.File;

public class SeaTunnelConfig {

    private static final ILogger LOGGER = Logger.getLogger(SeaTunnelConfig.class);

    private final EngineConfig engineConfig = new EngineConfig();

    static {
        String value = seatunnelHome();
        LOGGER.info("seatunnel.home is " + value);
        System.setProperty(SeaTunnelProperties.SEATUNNEL_HOME.getName(), value);
    }

    private Config hazelcastConfig;

    public SeaTunnelConfig() {
        hazelcastConfig = new Config();
        hazelcastConfig.getNetworkConfig().getJoin().getMulticastConfig()
            .setMulticastPort(Constant.DEFAULT_SEATUNNEL_MULTICAST_PORT);
        hazelcastConfig.setClusterName(Constant.DEFAULT_SEATUNNEL_CLUSTER_NAME);
        hazelcastConfig.getHotRestartPersistenceConfig()
            .setBaseDir(new File(seatunnelHome(), "recovery").getAbsoluteFile());
    }

    /**
     * Returns the absolute path for seatunnel.home based from the system property
     * {@link SeaTunnelProperties#SEATUNNEL_HOME}
     */
    private static String seatunnelHome() {
        return new File(System.getProperty(SeaTunnelProperties.SEATUNNEL_HOME.getName(),
            SeaTunnelProperties.SEATUNNEL_HOME.getDefaultValue())).getAbsolutePath();
    }

    public Config getHazelcastConfig() {
        return hazelcastConfig;
    }

    public void setHazelcastConfig(Config hazelcastConfig) {
        this.hazelcastConfig = hazelcastConfig;
    }

    public EngineConfig getEngineConfig() {
        return engineConfig;
    }
}
