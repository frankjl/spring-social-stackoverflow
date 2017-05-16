package org.springframework.social.stackoverflow.api.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.social.stackoverflow.api.StackOverflowReputation;
import org.springframework.social.stackoverflow.api.StackOverflowUser;
import org.springframework.social.stackoverflow.api.UserOperations;
import org.springframework.web.client.RestTemplate;

public class UserTemplate extends AbstractStackOverflowOperations implements UserOperations {

	private final RestTemplate restTemplate;

	public UserTemplate(RestTemplate restTemplate, boolean isAuthorised) {
		super(isAuthorised);
		this.restTemplate = restTemplate;
	}

	@SuppressWarnings("unchecked")
	public StackOverflowUser getUser() {
		Map<String, ?> jsonWrapper = restTemplate.getForObject(buildUri("me"), Map.class);
		List<?> userList = (List<?>) jsonWrapper.get("items");
		Map<String, ?> userJson = (Map<String, ?>) userList.get(0);

		Long userId = Long.valueOf(String.valueOf(userJson.get("user_id")));
		String displayName = String.valueOf(userJson.get("display_name"));
		String profileImageUrl = String.valueOf(userJson.get("profile_image"));
		String profileUrl = String.valueOf(userJson.get("link"));
		String websiteUrl = String.valueOf(userJson.get("website_url"));
		Long accountId = Long.valueOf(String.valueOf(userJson.get("account_id")));
		Date accountCreationDate = toDate(String.valueOf(userJson.get("creation_date")));
		Date lastAccessedDate = toDate(String.valueOf(userJson.get("last_access_date")));
		Date lastModifiedDate = toDate(String.valueOf(userJson.get("last_modified_date")));

		Integer reputationScore = Integer.valueOf(String.valueOf(userJson.get("reputation")));
		Integer reputationChangeDay = Integer.valueOf(String.valueOf(userJson.get("reputation_change_day")));
		Integer reputationChangeWeek = Integer.valueOf(String.valueOf(userJson.get("reputation_change_week")));
		Integer reputationChangeMonth = Integer.valueOf(String.valueOf(userJson.get("reputation_change_month")));
		Integer reputationChangeQuarter = Integer.valueOf(String.valueOf(userJson.get("reputation_change_quarter")));
		Integer reputationChangeYear = Integer.valueOf(String.valueOf(userJson.get("reputation_change_year")));

		Map<String, ?> badgeJson = (Map<String, ?>) userJson.get("badge_counts");
		Integer goldBadgeCount = Integer.valueOf(String.valueOf(badgeJson.get("gold")));
		Integer silverBadgeCount = Integer.valueOf(String.valueOf(badgeJson.get("silver")));
		Integer bronzeBadgeCount = Integer.valueOf(String.valueOf(badgeJson.get("bronze")));
		StackOverflowReputation reputation = new StackOverflowReputation(reputationScore, reputationChangeDay, reputationChangeWeek,
				reputationChangeMonth, reputationChangeQuarter, reputationChangeYear);

		StackOverflowUser user = new StackOverflowUser(userId, accountCreationDate, displayName, profileImageUrl, reputation, lastAccessedDate,
				lastModifiedDate, profileUrl, websiteUrl, accountId, goldBadgeCount, silverBadgeCount, bronzeBadgeCount);

		return user;
	}

	/**
	 * Parse as Unix epoch time
	 * 
	 * @param dateString
	 * @return
	 */
	private Date toDate(String dateString) {
		return new Date(Long.valueOf(dateString) * 1000);
	}
}
