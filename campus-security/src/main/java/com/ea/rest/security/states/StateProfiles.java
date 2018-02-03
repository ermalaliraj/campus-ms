package com.ea.rest.security.states;

import java.util.List;

import com.ea.rest.security.SecurityState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@SecurityState(headerName = "x-application-state-profiles")
public class StateProfiles {
	public static final String APPLICATION_STATE_HEADER_PROFILES = "x-application-state-profiles";
	private List<String> profiles;

	public StateProfiles() {
	}

	public StateProfiles(@JsonProperty("profiles") List<String> profiles) {
		this.profiles = profiles;
	}

	public List<String> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}

	public String toString() {
		return "StateProfiles [profiles=" + this.profiles + "]";
	}
}
