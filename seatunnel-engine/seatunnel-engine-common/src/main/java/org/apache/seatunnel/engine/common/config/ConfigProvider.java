/*
 * Copyright (c) 2008-2021, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.seatunnel.engine.common.config;

import static com.hazelcast.internal.config.DeclarativeConfigUtil.SYSPROP_CLIENT_CONFIG;
import static com.hazelcast.internal.config.DeclarativeConfigUtil.SYSPROP_MEMBER_CONFIG;
import static com.hazelcast.internal.config.DeclarativeConfigUtil.validateSuffixInSystemProperty;

import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.YamlClientConfigBuilder;
import com.hazelcast.client.config.impl.YamlClientConfigLocator;
import com.hazelcast.config.Config;
import com.hazelcast.config.YamlConfigBuilder;
import com.hazelcast.internal.config.YamlConfigLocator;
import lombok.NonNull;

import java.util.Properties;

/**
 * Locates and loads SeaTunnel or SeaTunnel Client configurations from various locations.
 *
 * @see YamlSeaTunnelConfigLocator
 */
public final class ConfigProvider {

    private ConfigProvider() {
    }

    public static SeaTunnelConfig locateAndGetSeaTunnelConfig() {
        return locateAndGetSeaTunnelConfig(null);
    }

    @NonNull
    public static SeaTunnelConfig locateAndGetSeaTunnelConfig(Properties properties) {

        YamlSeaTunnelConfigLocator yamlConfigLocator = new YamlSeaTunnelConfigLocator();
        SeaTunnelConfig config;

        if (yamlConfigLocator.locateFromSystemProperty()) {
            // 1. Try loading YAML config if provided in system property
            config = new YamlSeaTunnelConfigBuilder(yamlConfigLocator).setProperties(properties).build();

        } else if (yamlConfigLocator.locateInWorkDirOrOnClasspath()) {
            // 2. Try loading YAML config from the working directory or from the classpath
            config = new YamlSeaTunnelConfigBuilder(yamlConfigLocator).setProperties(properties).build();
        } else {
            // 3. Loading the default YAML configuration file
            yamlConfigLocator.locateDefault();
            config = new YamlSeaTunnelConfigBuilder(yamlConfigLocator).setProperties(properties).build();
        }
        return config;

    }

    @NonNull
    public static ClientConfig locateAndGetClientConfig() {
        validateSuffixInSystemProperty(SYSPROP_CLIENT_CONFIG);

        ClientConfig config;
        YamlClientConfigLocator yamlConfigLocator = new YamlClientConfigLocator();

        if (yamlConfigLocator.locateFromSystemProperty()) {
            // 1. Try loading config if provided in system property and it is an YAML file
            config = new YamlClientConfigBuilder(yamlConfigLocator.getIn()).build();
        } else if (yamlConfigLocator.locateInWorkDirOrOnClasspath()) {
            // 2. Try loading YAML config from the working directory or from the classpath
            config = new YamlClientConfigBuilder(yamlConfigLocator.getIn()).build();
        } else {
            // 3. Loading the default YAML configuration file
            yamlConfigLocator.locateDefault();
            config = new YamlClientConfigBuilder(yamlConfigLocator.getIn()).build();
        }
        return config;
    }

    @NonNull
    public static Config locateAndGetMemberConfig(Properties properties) {
        validateSuffixInSystemProperty(SYSPROP_MEMBER_CONFIG);

        Config config;
        YamlConfigLocator yamlConfigLocator = new YamlConfigLocator();

        if (yamlConfigLocator.locateFromSystemProperty()) {
            // 1. Try loading config if provided in system property and it is an YAML file
            config = new YamlConfigBuilder(yamlConfigLocator.getIn()).setProperties(properties).build();
        } else if (yamlConfigLocator.locateInWorkDirOrOnClasspath()) {
            // 2. Try loading YAML config from the working directory or from the classpath
            config = new YamlConfigBuilder(yamlConfigLocator.getIn()).setProperties(properties).build();
        } else {
            // 3. Loading the default YAML configuration file
            yamlConfigLocator.locateDefault();
            config = new YamlConfigBuilder(yamlConfigLocator.getIn()).setProperties(properties).build();
        }
        return config;
    }
}
