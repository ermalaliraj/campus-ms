package com.ea.rest.security.states;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(content = JsonInclude.Include.NON_NULL)
public abstract class ExpirableStateSupport {
	private Long exp;

	public ExpirableStateSupport() {
	}

	public ExpirableStateSupport(Long exp) {
		this.exp = exp;
	}

	public Long getExp() {
		return this.exp;
	}

	public void setExp(Long exp) {
		this.exp = exp;
	}

	@JsonIgnore
	public boolean isValid() {
		return this.exp.longValue() > System.currentTimeMillis() / 1000L;
	}
}
