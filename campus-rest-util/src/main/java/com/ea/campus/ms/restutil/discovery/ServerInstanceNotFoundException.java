package com.ea.campus.ms.restutil.discovery;

public class ServerInstanceNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 575693134945432445L;
	private String serviceId;

	public ServerInstanceNotFoundException(String serviceId, String message) {
		super(message);
		this.serviceId = serviceId;
	}
	
	public ServerInstanceNotFoundException(String serviceId) {
		this(serviceId, "Server instance not found" + (serviceId != null ? " for serviceId '" + serviceId + "'." : "."));
	}

	public String getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
}
