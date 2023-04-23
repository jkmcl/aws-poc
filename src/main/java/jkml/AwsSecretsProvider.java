package jkml;

import java.util.Base64;

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

	public String getSecret(String name) {
		log.info("Retrieving secret: {}", name);
		var request = GetSecretValueRequest.builder().secretId(name).build();
		var response = secretsManagerClient.getSecretValue(request);
		return response.secretString();
	}

	public byte[] getBase64DecodedSecret(String name) {
		var base64 = getSecret(name);
		log.info("Decoding secret: {}", name);
		return Base64.getMimeDecoder().decode(base64);
	}

}
