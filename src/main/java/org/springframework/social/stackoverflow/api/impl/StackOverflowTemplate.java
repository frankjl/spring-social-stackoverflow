package org.springframework.social.stackoverflow.api.impl;

import java.io.IOException;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;
import org.springframework.social.stackoverflow.api.StackOverflow;
import org.springframework.social.stackoverflow.api.UserOperations;
import org.springframework.social.support.HttpRequestDecorator;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

public class StackOverflowTemplate extends AbstractOAuth2ApiBinding implements StackOverflow {

	private UserOperations userOperations;
	private final String key;
	private final String accessToken;

	/**
	 * Constructs a StackOverflowTemplate using an accessToken (SO uses OAuth2)
	 * 
	 * @param accessToken
	 *            - token provided on authorization
	 * @param key
	 *            - the applications API key
	 */
	public StackOverflowTemplate(String accessToken, String key) {
		super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
		super.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()));
		this.userOperations = new UserTemplate(getRestTemplate(), isAuthorized());
		this.key = key;
		this.accessToken = accessToken;
	}

	@Override
	protected void configureRestTemplate(RestTemplate restTemplate) {
		// TODO: figure out why these interceptors are being called twice.
		restTemplate.getInterceptors().clear();
		restTemplate.getInterceptors().add(new OAuthAccessTokenAndKeyHttpRequestInterceptor());
	}

	public UserOperations userOperations() {
		return userOperations;
	}

	public RestOperations restOperations() {
		return getRestTemplate();
	}

	private class OAuthAccessTokenAndKeyHttpRequestInterceptor implements ClientHttpRequestInterceptor {

		public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
			HttpRequestDecorator decorator = null;
			if (request instanceof HttpRequestDecorator) {
				decorator = (HttpRequestDecorator) request;
			} else {
				decorator = new HttpRequestDecorator(request);
			}

			// TODO: not do this check
			if (!decorator.getURI().toString().contains("key")) {
				decorator.addParameter("key", StackOverflowTemplate.this.key);
			}
			if (!decorator.getURI().toString().contains("access_token")) {
				decorator.addParameter("access_token", StackOverflowTemplate.this.accessToken);
			}
			return execution.execute(decorator, body);
		}
	}
}
