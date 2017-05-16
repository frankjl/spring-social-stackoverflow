package org.springframework.social.stackoverflow.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import org.springframework.web.client.RestTemplate;

public class StackOverflowAPITests {

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void testGetData() throws Exception {
		RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactorySelector.getRequestFactory());
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>(2);
		FormHttpMessageConverter messageConverter = new FormHttpMessageConverter() {
			public boolean canRead(Class<?> clazz, MediaType mediaType) {
				return true;
			}
		};
		converters.add(messageConverter);
		converters.add(new GsonHttpMessageConverter());

		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build()));
		restTemplate.setMessageConverters(converters);
		Map forObject = restTemplate.getForObject(
				"https://api.stackexchange.com/2.0/me?site=stackoverflow&key=YZb1GY6iVqDZG*rqawAXiQ((&access_token=gFAYsydaw0jzcqdJnfJgdw))",
				HashMap.class);
		System.out.println(forObject);

	}
}
