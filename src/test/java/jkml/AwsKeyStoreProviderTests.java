package jkml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.nio.file.Files;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@SpringBootTest
class AwsKeyStoreProviderTests {

	@Autowired
	private AwsKeyStoreProvider provider;

	@Autowired
	private AwsProperties properties;

	@MockBean
	private SecretsManagerClient secretsManagerClient;

	@Test
	void testGetKeyStore() throws Exception {
		var expected = "expected".getBytes();
		var request = GetSecretValueRequest.builder().secretId(properties.getSecretName().getKeyStore()).build();
		var response = GetSecretValueResponse.builder().secretBinary(SdkBytes.fromByteArray(expected)).build();
		when(secretsManagerClient.getSecretValue(request)).thenReturn(response);

		assertArrayEquals(expected, Files.readAllBytes(provider.getKeyStore()));
	}

	@Test
	void testGetKeyStorePassword() throws Exception {
		var expected = "expected";
		var request = GetSecretValueRequest.builder().secretId(properties.getSecretName().getKeyStorePassword()).build();
		var response = GetSecretValueResponse.builder().secretString(expected).build();
		when(secretsManagerClient.getSecretValue(request)).thenReturn(response);

		assertEquals(expected, provider.getKeyStorePassword());
	}

}
