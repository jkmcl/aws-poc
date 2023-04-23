package jkml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.util.Base64;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@SpringBootTest
class ProvidersTests {

	@Autowired
	private AwsSecretsProvider secretsProvider;

	@Autowired
	private AwsKeyStoreProvider keyStoreProvider;

	@Autowired
	private AwsProperties properties;

	@MockBean
	private SecretsManagerClient secretsManagerClient;

	@Test
	void testSecretsProvider() {
		// Mock setup
		var secretName = "something";
		var expectedBytes = secretName.getBytes();
		var request = GetSecretValueRequest.builder().secretId(secretName).build();
		var base64 = Base64.getEncoder().encodeToString(expectedBytes);
		var response = GetSecretValueResponse.builder().secretString(base64).build();
		when(secretsManagerClient.getSecretValue(request)).thenReturn(response);

		// Test
		assertArrayEquals(expectedBytes, secretsProvider.getBase64DecodedSecret(secretName));
	}

	@Test
	void testGetKeyStore() throws Exception {
		// Mock setup
		var expected = "secret";
		var expectedBytes = expected.getBytes();
		var request1 = GetSecretValueRequest.builder().secretId(properties.getSecretName().getKeyStore()).build();
		var base64 = Base64.getEncoder().encodeToString(expectedBytes);
		var response1 = GetSecretValueResponse.builder().secretString(base64).build();
		when(secretsManagerClient.getSecretValue(request1)).thenReturn(response1);
		var request2 = GetSecretValueRequest.builder().secretId(properties.getSecretName().getKeyStorePassword()).build();
		var response2 = GetSecretValueResponse.builder().secretString(expected).build();
		when(secretsManagerClient.getSecretValue(request2)).thenReturn(response2);

		// Test
		assertArrayEquals(expectedBytes, Files.readAllBytes(keyStoreProvider.getKeyStore()));
		assertEquals(expected, keyStoreProvider.getKeyStorePassword());
	}

}
