package com.ea.campus.ms.payment.context;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

public class SecuredResourceSupport extends ResourceSupport {
	
	public void addIfSecure(Link link) {
		if (link != null) {
			super.add(link);
		}
	}

	@Deprecated
	public void add(Link link) {
		addIfSecure(link);
	}
}
