package jkml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@SpringBootTest
class AwsSecretsProviderTests {

	@Autowired
	private AwsSecretsProvider provider;

	@MockBean
	private SecretsManagerClient secretsManagerClient;

	@Test
	void testGetSecretString() {
		var name = "name1";
		var value = "value1";
		var request = GetSecretValueRequest.builder().secretId(name).build();
		var response = GetSecretValueResponse.builder().secretString(value).build();
		when(secretsManagerClient.getSecretValue(request)).thenReturn(response);

		assertEquals(value, provider.getSecretString(name));
	}

	@Test
	void testGetSecretBinary() {
		var name = "name2";
		var value = "value2".getBytes();
		var request = GetSecretValueRequest.builder().secretId(name).build();
		var response = GetSecretValueResponse.builder().secretBinary(SdkBytes.fromByteArray(value)).build();
		when(secretsManagerClient.getSecretValue(request)).thenReturn(response);

		assertArrayEquals(value, provider.getSecretBinary(name));
	}

}
