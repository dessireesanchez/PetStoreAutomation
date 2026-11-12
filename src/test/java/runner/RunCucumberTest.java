package runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@SuiteDisplayName("Pruebas de JPetStore")
@IncludeEngines("cucumber")
@SelectClasspathResource("Feature")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "steps,hook")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty,summary,"
        + "html:build/reports/cucumber/cucumber.html,"
        + "json:build/reports/cucumber/cucumber.json,"
        + "junit:build/reports/cucumber/cucumber.xml")
public class RunCucumberTest {
}
