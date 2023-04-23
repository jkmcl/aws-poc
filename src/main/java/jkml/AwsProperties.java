package jkml;

import java.security.KeyStore;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.aws")
public class AwsProperties {

	private String region;

	private final SecretName secretName = new SecretName();

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public SecretName getSecretName() {
		return secretName;
	}

	public static class SecretName {

		private String keyStore;

		private String keyStorePassword;

		private String keyStoreType = KeyStore.getDefaultType();

		public String getKeyStore() {
			return keyStore;
		}

		public void setKeyStore(String keyStore) {
			this.keyStore = keyStore;
		}

		public String getKeyStorePassword() {
			return keyStorePassword;
		}

		public void setKeyStorePassword(String keyStorePassword) {
			this.keyStorePassword = keyStorePassword;
		}

		public String getKeyStoreType() {
			return keyStoreType;
		}

		public void setKeyStoreType(String keyStoreType) {
			this.keyStoreType = keyStoreType;
		}

	}

}
