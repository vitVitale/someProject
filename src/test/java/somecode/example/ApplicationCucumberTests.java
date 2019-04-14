package somecode.example;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/resources/pilot.feature",
        plugin = {"pretty", "html:target/cucumber"},
        tags = {"@Pilot"})

public class ApplicationCucumberTests {
}
