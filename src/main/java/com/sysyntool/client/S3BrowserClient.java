package com.sysyntool.client;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

public class S3BrowserClient {

	
	private AmazonS3 s3 = null;	
	
	public S3BrowserClient(String accessKey,String secretKey,String serviceEndpoint,String signingRegion) {
		buildS3(accessKey, secretKey, serviceEndpoint, signingRegion);
	}
	
	public void buildS3(String accessKey,String secretKey,String serviceEndpoint,String signingRegion) {
		BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(false);
		EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
		s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();
	}
	
	public AmazonS3 getS3() {
		return s3;
	}

}
