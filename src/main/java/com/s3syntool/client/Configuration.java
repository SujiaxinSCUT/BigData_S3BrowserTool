package com.s3syntool.client;

import java.io.Serializable;

public class Configuration implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  String accessKey;
	private  String secretKey;
	private  String serviceEndpoint;
	private  String signingRegion;
	public Configuration(String accessKey, String secretKey, String serviceEndpoint, String signingRegion) {
		super();
		this.accessKey = accessKey;
		this.secretKey = secretKey;
		this.serviceEndpoint = serviceEndpoint;
		this.signingRegion = signingRegion;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getServiceEndpoint() {
		return serviceEndpoint;
	}
	public void setServiceEndpoint(String serviceEndpoint) {
		this.serviceEndpoint = serviceEndpoint;
	}
	public String getSigningRegion() {
		return signingRegion;
	}
	public void setSigningRegion(String signingRegion) {
		this.signingRegion = signingRegion;
	}
	@Override
	public String toString() {
		return "Configuration [accessKey=" + accessKey + ", secretKey=" + secretKey + ", serviceEndpoint="
				+ serviceEndpoint + ", signingRegion=" + signingRegion + "]";
	}
	
	
}
