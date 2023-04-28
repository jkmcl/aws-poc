package jkml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;

public class AwsSecretsProvider {

	private final Logger log = LoggerFactory.getLogger(AwsSecretsProvider.class);

	private final SecretsManagerClient secretsManagerClient;

	public AwsSecretsProvider(SecretsManagerClient secretsManagerClient) {
		this.secretsManagerClient = secretsManagerClient;
	}

	public String getSecretString(String name) {
		log.info("Retrieving secret string by name: {}", name);
		var request = GetSecretValueRequest.builder().secretId(name).build();
		var response = secretsManagerClient.getSecretValue(request);
		return response.secretString();
	}

	public byte[] getSecretBinary(String name) {
		log.info("Retrieving secret binary by name: {}", name);
		var request = GetSecretValueRequest.builder().secretId(name).build();
		var response = secretsManagerClient.getSecretValue(request);
		return response.secretBinary().asByteArray();
	}

}
