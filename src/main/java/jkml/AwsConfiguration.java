package jkml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;

@Configuration
@EnableConfigurationProperties(AwsProperties.class)
class AwsConfiguration {

	private final Logger log = LoggerFactory.getLogger(AwsConfiguration.class);

	private final AwsProperties properties;

	private final Region region;

	AwsConfiguration(AwsProperties properties) {
		this.properties = properties;
		region = Region.of(properties.getRegion());
		log.info("AWS Region: {}", region);
	}

	@Bean
	SecretsManagerClient secretsManagerClient() {
		log.info("Creating {}", SecretsManagerClient.class.getSimpleName());
		return SecretsManagerClient.builder()
				.region(region)
				.credentialsProvider(DefaultCredentialsProvider.create())
				.build();
	}

	@Bean
	AwsSecretsProvider awsSecretsProvider(SecretsManagerClient secretsManagerClient) {
		log.info("Creating {}", AwsSecretsProvider.class.getSimpleName());
		return new AwsSecretsProvider(secretsManagerClient);
	}

	@Bean
	AwsKeyStoreProvider awsKeyStoreProvider(AwsSecretsProvider awsSecretsProvider) {
		return new AwsKeyStoreProvider(awsSecretsProvider, properties);
	}

}
