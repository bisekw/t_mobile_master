package framework.steps;

import framework.config.Settings;
import framework.core.DriverContext;
import framework.core.WebElementHelper;
import framework.toolkit.Cucumber;
import io.cucumber.java.*;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class CucumberTestInit {

    @Before
    public void Init(Scenario scenario)
    {
        log.info("Before from MASTER");
        Settings.SCENARIO_NAME = scenario.getName();
        Settings.CUCUMBER_SCENARIO_TAGS = scenario.getSourceTagNames();
        log.info("Runing Scenario: "+Settings.SCENARIO_NAME);
        Cucumber.SCENARIO_RUNNING = scenario;
    }


    @After
    public void AfterScenario(Scenario scenario)
    {
        if(scenario.isFailed())
        {
          byte[] screenshotBytes = ((TakesScreenshot) DriverContext.getRemoteWebDriver()).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshotBytes, "image/png", "Screenshot");
         String pageSource = Objects.requireNonNull(DriverContext.getRemoteWebDriver()).getPageSource();
        scenario.attach(pageSource.getBytes(StandardCharsets.UTF_8),"plain/text", "pageSource");
        }
        log.info("AfterScenario from MASTER");
        if(DriverContext.getRemoteWebDriver()!=null)
        {
            WebElementHelper.QuitDriver();
        }
    }

    @BeforeStep
    public void CucumberBeforeStep(Scenario scenario){
        log.info("BeforeStep from MASTER");
    }

    @AfterStep
    public void CucumberAfterStep(Scenario scenario)
    {
        log.info("AfterStep from MASTER");
        log.info("Scenario is failed "+scenario.isFailed());
        if(scenario.isFailed()){
            WebElementHelper.SavePageSource();
        }
    }
}
