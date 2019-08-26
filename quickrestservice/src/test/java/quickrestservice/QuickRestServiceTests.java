package quickrestservice;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import quickrestservice.config.Configs;
import quickrestservice.models.RestServiceOperation;
import quickrestservice.models.RestServiceRequest;

class QuickRestServiceTests {

	@Test
	void test_config() {
		
		assertNotNull(Configs._PROP);
		assertNotNull(Configs.BASE_PATH);
		assertNotNull(Configs.PORT);
		 
	}
	
	
	@Test
	void test_id() {
		
		RestServiceRequest request = new RestServiceRequest();
		
		String body = "test request 123";
		
		request.setBody(body);
		
		RestServiceOperation operation = new RestServiceOperation();
		
		operation.setRequest(request);
		
		assertEquals(operation.getId(),DigestUtils.sha256Hex(body.replaceAll("\r\n", "").replaceAll("\\s+", "")));
		 
	}

}
