package framework.steps;

import framework.config.Settings;
import framework.core.DriverType;
import framework.core.FrameworkInitialize;
import framework.toolkit.AdbCommandsRunner;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.SessionNotCreatedException;

import java.net.MalformedURLException;
import java.util.List;

@Slf4j
public class MasterSteps {

    private ScenarioContext scenarioContext;

    public MasterSteps(ScenarioContext scenarioContext){
        this.scenarioContext = scenarioContext;
    }
    @When("Użytkownik uruchamia przeglądarkę {}")
    public void runBrowser(DriverType driverType) throws MalformedURLException {
        Settings.DRIVER_TYPE = driverType;
        Settings.RUNNER_TYPE = "SELENIUM";
        log.info("Runner = " + Settings.RUNNER_TYPE);
        if (Settings.IS_REMOTE_RUN) {
            log.info("GRID_REMOTE");
            Settings.GRID_URL = Settings.GRID_REMOTE;
        } else {
            log.info("GRID_REMOTE");
            Settings.GRID_URL = Settings.GRID_LOCAL;
        }
        FrameworkInitialize.InitializeBrowser(driverType);
    }

    @When("Użytkownik przygotowuje konfigurację pod testy API")
    public void configRestAssured() {
        Settings.DRIVER_TYPE = DriverType.API;
        Settings.RUNNER_TYPE = "API_TESTS";
        log.info("Runner =" + Settings.RUNNER_TYPE);
    }


    @When("Użytkownik uruchamia aplikację {} na telefonie Android {}")
    public void runAppOnAndroidMobile(DriverType driverType, String udid) throws InterruptedException, SessionNotCreatedException, MalformedURLException {
        Settings.DRIVER_TYPE = DriverType.ANDROID_APP;
        Settings.RUNNER_TYPE = "APPIUM_ANDROID";

        if (Settings.IS_REMOTE_RUN) {
            Settings.GRID_URL = Settings.GRID_APPIUM_ANDROID;
        } else {
            Settings.GRID_URL = Settings.GRID_APPIUM_ANDROID;
        }

        try {
            FrameworkInitialize.InitializeMobile(driverType, udid);
        } catch (SessionNotCreatedException | MalformedURLException e) {
            AdbCommandsRunner adb = new AdbCommandsRunner();
            adb.rebootPhoneByUdid(udid);
            Thread.sleep(40000);
            for (int i = 0; i < 11; i++) {
                List<String> deviceList = adb.getAdbDeviceList();
                if (deviceList.contains(udid)) {
                    adb.runCommand(String.format("adb -s %s shell am forcr-stop com.android.settings", udid));
                    Thread.sleep(10000);
                    break;
                } else {
                    log.info("Device not found. I will try again, after 5 sek");
                    Thread.sleep(5000);
                }
                if (i == 10) {
                    log.info("Device not found!");
                    throw new SessionNotCreatedException("Session could not be created.");
                }
            }
            FrameworkInitialize.InitializeMobile(driverType, udid);
        }
    }

    @When("Użytkownik uruchamia aplikację {} na telefonie iOS {}")
    public void runAppOnIOSMobile(DriverType driverType, String udid) throws InterruptedException, SessionNotCreatedException, MalformedURLException {
        Settings.DRIVER_TYPE = DriverType.IOS_APP;
        Settings.RUNNER_TYPE = "IOS_ANDROID";
        Settings.APPIUM_DEVICE_NAME = "iPhone (Wojciech)";
        Settings.APPIUM_PLATFORM_VERSION ="16.5";
        if (Settings.IS_REMOTE_RUN) {
            Settings.GRID_URL = Settings.GRID_APPIUM_IOS;
        } else {
            Settings.GRID_URL = Settings.GRID_APPIUM_IOS;
        }

        try {
            FrameworkInitialize.InitializeMobile(driverType, udid);
        } catch (SessionNotCreatedException | MalformedURLException e) {
            log.info( e.getMessage());
            FrameworkInitialize.InitializeMobile(driverType, udid);
        }
    }
}
