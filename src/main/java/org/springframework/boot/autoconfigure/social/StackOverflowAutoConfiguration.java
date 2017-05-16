/*
 * Copyright 2012-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.social;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.web.GenericConnectionStatusView;
import org.springframework.social.stackoverflow.api.StackOverflow;
import org.springframework.social.stackoverflow.connect.StackOverflowConnectionFactory;

/**
 * Auto-configuration for StackExchange APIs
 * 
 * @author <a href="mailto:frank@crosschx.com">Frank J. Lamantia</a>
 *
 */
@Configuration
@ConditionalOnClass({ SocialConfigurerAdapter.class, StackOverflowConnectionFactory.class })
@ConditionalOnProperty(prefix = "spring.social.stackoverflow", name = "app-id")
@AutoConfigureBefore(SocialWebAutoConfiguration.class)
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class StackOverflowAutoConfiguration {

	@Configuration
	@EnableSocial
	@EnableConfigurationProperties(StackOverflowProperties.class)
	@ConditionalOnWebApplication
	protected static class StackOverflowConfigurerAdapter extends SocialAutoConfigurerAdapter {

		private final StackOverflowProperties properties;

		protected StackOverflowConfigurerAdapter(StackOverflowProperties properties) {
			this.properties = properties;
		}

		@Bean
		@ConditionalOnMissingBean(StackOverflow.class)
		@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
		public StackOverflow stackOverflow(ConnectionRepository repository) {
			Connection<StackOverflow> connection = repository.findPrimaryConnection(StackOverflow.class);
			return connection != null ? connection.getApi() : null;
		}

		@Bean(name = { "connect/stackoverflowConnect", "connect/stackoverflowConnected" })
		@ConditionalOnProperty(prefix = "spring.social", name = "auto-connection-views")
		public GenericConnectionStatusView stackOverflowViews() {
			return new GenericConnectionStatusView("stackoverflow", "StackOverflow");
		}

		@Override
		protected ConnectionFactory<?> createConnectionFactory() {
			return new StackOverflowConnectionFactory(this.properties.getAppId(), this.properties.getAppSecret(), this.properties.getKey());
		}
	}
}
