package org.springframework.boot.autoconfigure.social;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * An extension of {@link SocialProperties} that includes the secret key field
 * for interacting with StackExchange APIs.
 * 
 * @author <a href="mailto:frank@crosschx.com">Frank J. Lamantia</a>
 *
 */
@ConfigurationProperties(prefix = "spring.social.stackoverflow")
public class StackOverflowProperties extends SocialProperties {

	private String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
