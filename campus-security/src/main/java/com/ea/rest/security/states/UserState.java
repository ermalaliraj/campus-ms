package com.ea.rest.security.states;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import com.ea.rest.security.SecurityState;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Maps;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SecurityState(headerName = "x-application-state")
public class UserState {
	private String userId;
	private Map<Long, AccountInfo> accountInfoMap = Maps.newHashMap();

	public UserState() {
	}

	@JsonCreator
	public UserState(@JsonProperty("userId") String userId) {
		this.userId = userId;
	}

	@JsonIgnore
	public Collection<Long> getCustomerIds() {
		return this.accountInfoMap.keySet();
	}

	public void setAccountInfoMap(Map<Long, AccountInfo> accountInfoMap) {
		this.accountInfoMap.clear();
		this.accountInfoMap.putAll(accountInfoMap);
	}

	public Map<Long, AccountInfo> getAccountInfoMap() {
		return Collections.unmodifiableMap(this.accountInfoMap);
	}

	@JsonIgnore
	public AccountInfo getAccountInfo(Long accountId) {
		return (AccountInfo) this.accountInfoMap.get(accountId);
	}

	@JsonIgnore
	public boolean containsCustomer(Long customerId) {
		return this.accountInfoMap.containsKey(customerId);
	}

	@JsonIgnore
	public void add(AccountInfo accountInfo) {
		this.accountInfoMap.put(accountInfo.getId(), accountInfo);
	}

	public String toString() {
		return "UserState [userId=" + userId + ", accountInfoMap=" + this.accountInfoMap + "]";
	}
}
