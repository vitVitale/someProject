package somecode.example;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import somecode.example.domain.Message;
import somecode.example.repo.MessageRepo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(ApplicationTests.class);

	private final String SERVER_URL = "http://localhost";
	private final String ENDPOINT = "/message";

	@Autowired
	protected TestRestTemplate restTemplate;
	@Autowired
	protected MessageRepo repository;
	@LocalServerPort
	protected int port;

	private String thingsEndpoint() {
		return SERVER_URL + ":" + port + ENDPOINT;
	}

	int put(final String something) {
		return restTemplate.postForEntity(thingsEndpoint(), something, Void.class).getStatusCodeValue();
	}

	Message getContents() {
		return restTemplate.getForEntity(thingsEndpoint(), Message.class).getBody();
	}

	void clean() {
		restTemplate.delete(thingsEndpoint());
	}
}
