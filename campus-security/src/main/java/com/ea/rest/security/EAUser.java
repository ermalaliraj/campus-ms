package com.ea.rest.security;

import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class EAUser extends User {

	private static final long serialVersionUID = -5162019511770248058L;
	private Map<String, Object> grantedStates;

	public EAUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Map<String, Object> grantedStates) {
		super(username, password, authorities);
		this.grantedStates = grantedStates;
	}

	public Map<String, Object> getGrantedStates() {
		return this.grantedStates;
	}

	public boolean hasGrantedState(Class<?> clazz) {
		return this.grantedStates.containsKey(clazz.getSimpleName());
	}

	public void addGrantedState(Object state) {
		if (state != null) {
			this.grantedStates.put(state.getClass().getSimpleName(), state);
		}
	}

	public void addGrantedStates(Collection<Object> states) {
		for (Object state : states) {
			addGrantedState(state);
		}
	}
}
