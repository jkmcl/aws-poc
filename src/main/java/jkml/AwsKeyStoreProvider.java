package jkml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AwsKeyStoreProvider {

	private final Logger log = LoggerFactory.getLogger(AwsKeyStoreProvider.class);

	private final AwsSecretsProvider secretsProvider;

	private final AwsProperties properties;

	public AwsKeyStoreProvider(AwsSecretsProvider awsSecretsProvider, AwsProperties properties) {
		this.properties = properties;
		this.secretsProvider = awsSecretsProvider;
	}

	public Path getKeyStore() throws IOException {
		var bytes = secretsProvider.getSecretBinary(properties.getSecretName().getKeyStore());
		var path = Files.createTempFile(null, null);
		log.info("Writing key store to temporary file: {}", path);
		Files.write(path, bytes);
		return path;
	}

	public String getKeyStorePassword() {
		return secretsProvider.getSecretString(properties.getSecretName().getKeyStorePassword());
	}

}
