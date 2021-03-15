/*
 * Copyright 2021 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.autoconfigure.securityoauth2;

import com.sun.org.apache.xerces.internal.parsers.SecurityConfiguration;
import io.jmix.core.CoreConfiguration;
import io.jmix.core.JmixOrder;
import io.jmix.core.session.SessionData;
import io.jmix.securityoauth2.SecurityOAuth2Configuration;
import io.jmix.securityoauth2.SecurityOAuth2Properties;
import io.jmix.securityoauth2.configurer.OAuth2ResourceServerConfigurer;
import io.jmix.securityoauth2.configurer.OAuth2AuthorizationServerConfigurer;
import io.jmix.securityoauth2.impl.RequestLocaleProvider;
import io.jmix.securityoauth2.impl.UniqueAuthenticationKeyGenerator;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@Import({CoreConfiguration.class, SecurityConfiguration.class, SecurityOAuth2Configuration.class})
@EnableResourceServer
@EnableAuthorizationServer
public class SecurityOAuth2AutoConfiguration {
    @Bean(name = "sec_TokenStore")
    @ConditionalOnMissingBean(TokenStore.class)
    protected TokenStore tokenStore() {
        InMemoryTokenStore tokenStore = new InMemoryTokenStore();
        tokenStore.setAuthenticationKeyGenerator(new UniqueAuthenticationKeyGenerator());
        return tokenStore;
    }

    @Configuration(proxyBeanMethods = false)
    @Order(JmixOrder.HIGHEST_PRECEDENCE + 100)
    public static class Oauth2AuthorizationServerConfiguration extends OAuth2AuthorizationServerConfigurer {
    }

    @Configuration(proxyBeanMethods = false)
    @Order(JmixOrder.HIGHEST_PRECEDENCE + 100)
    public static class Oauth2ResourceServerConfiguration extends OAuth2ResourceServerConfigurer {
    }
}