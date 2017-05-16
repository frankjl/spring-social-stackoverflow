package org.springframework.social.stackoverflow.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.stackoverflow.api.StackOverflow;
import org.springframework.social.stackoverflow.api.impl.StackOverflowTemplate;

/**
 * StackOverflow Service Provider implementation (based on OAuth2)
 * 
 * @author robert.hinds
 * 
 */
public class StackOverflowServiceProvider extends AbstractOAuth2ServiceProvider<StackOverflow> {

	private String key;
	
	public StackOverflowServiceProvider(String clientId, String clientSecret, String key) {
		super(new StackOverflowOAuth2Template(clientId, clientSecret, "https://stackexchange.com/oauth", "https://stackexchange.com/oauth/access_token"));
		this.key = key;
	}

	@Override
	public StackOverflow getApi(String accessToken) {
		return new StackOverflowTemplate(accessToken, key);
	}

}
