package somecode.example;

import cucumber.api.java.ru.Дано;
import cucumber.api.java.ru.Когда;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import somecode.example.domain.Message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;


@Ignore
public class CucumberStepDefinitions  extends ApplicationTests {
    private final Logger LOG = LoggerFactory.getLogger(CucumberStepDefinitions.class);

    @Дано("^удаляем записи из БД$")
    public void deleteAllNotes() {
        repository.deleteAll();
        repository.flush();
    }

    @Когда("^отправляем сообщение: (.*?)$")
    public void postMessageCheck(String msg) {
        Message message = new Message();
        message.setText(msg);

        ResponseEntity<Message> response =
                restTemplate.postForEntity("/message", message, Message.class);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getId(), notNullValue());
        assertThat(response.getBody().getText(), is(msg));
    }
}
