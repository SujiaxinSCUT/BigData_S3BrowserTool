package com.sysyntool.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3BrowserClient {

	
	private AmazonS3 s3 = null;	
	
	public S3BrowserClient(Configuration config) {
		buildS3(config);
	}
	
	public void buildS3(Configuration config) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(config.getAccessKey(), config.getSecretKey());
		ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(false);
		EndpointConfiguration endpoint = new EndpointConfiguration(config.getServiceEndpoint(), config.getSigningRegion());
		s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();
	}
	
	public AmazonS3 getS3() {
		return s3;
	}

}
