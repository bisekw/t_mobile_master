package framework.core;

import framework.config.Settings;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverLogLevel;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import static framework.config.Settings.CHROME_VERSION;
import static framework.config.Settings.SCENARIO_NAME;
import static framework.core.DriverType.*;


public class FrameworkInitialize extends Base {

    public static void InitializeBrowser(DriverType driverType) throws MalformedURLException {

        RemoteWebDriver driver;
        switch (driverType) {
            case CHROME: {
                LoggingPreferences loggingPreferences = setLogs2All();
                if (Settings.IS_REMOTE_RUN) {
                    driver = new RemoteWebDriver(new URL(Settings.GRID_URL), prepareChromeOptions());
                    DriverContext.setRemoteWebDriverThreadLocal(CHROME, driver);
                } else {
                    WebDriverManager.chromedriver().setup();
                    driver = new ChromeDriver(prepareChromeOptions());
                    DriverContext.setRemoteWebDriverThreadLocal(CHROME, driver);
                }
                break;
            }
            case FIREFOX: {
                //Open the browser

                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (Settings.IS_REMOTE_RUN) {
                    driver = new RemoteWebDriver(new URL(Settings.GRID_URL), firefoxOptions);
                    DriverContext.setRemoteWebDriverThreadLocal(driver);
                } else {
                    driver = new FirefoxDriver();
                    DriverContext.setRemoteWebDriverThreadLocal(driver);
                }
                break;
            }
        }
    }

    public static void InitializeMobile(DriverType driverType, String udid) throws MalformedURLException {

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, Settings.APPIUM_DEVICE_NAME);
        desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, Settings.APPIUM_PLATFORM_VERSION);
        String prefix = "appium:";
        switch (driverType) {
            case IOS_APP: {
                IOSDriver driver;
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.IOS);
             //   desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "16.4.1");
                desiredCapabilities.setCapability(prefix + "wdaLaunchTimeout", 120);
                desiredCapabilities.setCapability(prefix + "wdaLaunchTimeout", 120);
                desiredCapabilities.setCapability(prefix + "showXcodeLog", true);
                desiredCapabilities.setCapability(prefix + "bundleId", Settings.APPIUM_APP_BUNDLE_ID);
                desiredCapabilities.setCapability(prefix + "automationName", "XCUITest");
                desiredCapabilities.setCapability(prefix + "useNewWDA", false);
                desiredCapabilities.setCapability(prefix + "waitForQuiescence", false);
                desiredCapabilities.setCapability(prefix + "shouldUseCompactResponses", true);
                desiredCapabilities.setCapability(prefix + "autoAcceptAlerts", true);
                desiredCapabilities.setCapability(prefix + "showIOSLog", false);
                desiredCapabilities.setCapability(prefix + "autoWebviewTimeout", 5000);
                desiredCapabilities.setCapability(prefix + "newCommandTimeout", 120);
                desiredCapabilities.setCapability(prefix + "xcodeOrgId", ""); //TBD
                desiredCapabilities.setCapability(prefix + "xcodeSigningId", "iPhone Developer");
                desiredCapabilities.setCapability(prefix + "updateWDABundleId", "io.appium.WebDriverAgentRunner");
                driver = new IOSDriver(new URL(Settings.GRID_APPIUM_IOS), desiredCapabilities);
                DriverContext.setRemoteWebDriverThreadLocal(IOS_APP, driver);
                break;
            }
            case ANDROID_APP: {
                AndroidDriver driver;
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
                desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, Settings.APPIUM_APP_PACKAGE);
                desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Settings.APPIUM_APP_ACTIVITY);
                desiredCapabilities.setCapability(prefix + "autoGrantPermissions", true);
                desiredCapabilities.setCapability(prefix + "unicodeKeyboard", true);
                desiredCapabilities.setCapability(prefix + "resetKeyboard", true);
                desiredCapabilities.setCapability(prefix + "disableWindowAnimation", true);
                desiredCapabilities.setCapability(prefix + "automationName", "uiautomator2");
                desiredCapabilities.setCapability(prefix + "autoDimissAlerts", true);
                desiredCapabilities.setCapability(prefix + "newCommandTimeout", 120);
                driver = new AndroidDriver(new URL(Settings.GRID_APPIUM_ANDROID), desiredCapabilities);
                DriverContext.setRemoteWebDriverThreadLocal(ANDROID_APP, driver);
                break;
            }
            case ANDROID_CHROME: {
                AndroidDriver driver;
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
                desiredCapabilities.setCapability(CapabilityType.BROWSER_VERSION, "11");
                desiredCapabilities.setCapability(prefix + "autoGrantPermissions", true);
                desiredCapabilities.setCapability(prefix + "unicodeKeyboard", true);
                desiredCapabilities.setCapability(prefix + "resetKeyboard", true);
                desiredCapabilities.setCapability(prefix + "disableWindowAnimation", true);
                desiredCapabilities.setCapability(prefix + "automationName", "uiautomator2");
                desiredCapabilities.setCapability(prefix + "autoDimissAlerts", true);
                desiredCapabilities.setCapability(prefix + "newCommandTimeout", 120);
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.setAcceptInsecureCerts(true);
                chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour.ACCEPT);
                desiredCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
                chromeOptions.merge(desiredCapabilities);
                driver = new AndroidDriver(new URL(Settings.GRID_APPIUM_ANDROID), desiredCapabilities);
                DriverContext.setRemoteWebDriverThreadLocal(ANDROID_CHROME, driver);
                break;
            }
            case TRANSPORT_GZM_ANDROID: {
                AndroidDriver driver;
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, udid);
                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, Platform.ANDROID);
                desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, Settings.APPIUM_APP_PACKAGE);
                desiredCapabilities.setCapability(AndroidMobileCapabilityType.APP_ACTIVITY, Settings.APPIUM_APP_ACTIVITY);
                desiredCapabilities.setCapability(prefix + "autoGrantPermissions", true);
                desiredCapabilities.setCapability(prefix + "unicodeKeyboard", true);
                desiredCapabilities.setCapability(prefix + "resetKeyboard", true);
                desiredCapabilities.setCapability(prefix + "disableWindowAnimation", true);
                desiredCapabilities.setCapability(prefix + "automationName", "uiautomator2");
                desiredCapabilities.setCapability(prefix + "printPageSourceOnFindFailure", true);
                desiredCapabilities.setCapability(prefix + "ensureWebviewsHavePages", true);
                desiredCapabilities.setCapability(prefix + "nativeWebScreenshot", true);

                desiredCapabilities.setCapability(prefix + "newCommandTimeout", 120);
                driver = new AndroidDriver(new URL(Settings.GRID_APPIUM_ANDROID), desiredCapabilities);
                DriverContext.setRemoteWebDriverThreadLocal(ANDROID_APP, driver);
                break;
            }
        }
    }

    private static LoggingPreferences setLogs2All() {
        LoggingPreferences loggingPreferences = new LoggingPreferences();
        loggingPreferences.enable(LogType.BROWSER, Level.ALL);
        loggingPreferences.enable(LogType.PERFORMANCE, Level.ALL);
        loggingPreferences.enable(LogType.CLIENT, Level.ALL);
        loggingPreferences.enable(LogType.SERVER, Level.ALL);
        loggingPreferences.enable(LogType.DRIVER, Level.ALL);
        loggingPreferences.enable(LogType.PROFILER, Level.ALL);
        return loggingPreferences;
    }

    private static ChromeOptions prepareChromeOptions() {
        DesiredCapabilities chromCapabilities = new DesiredCapabilities();
        chromCapabilities.setCapability("scenarioName", SCENARIO_NAME);
        chromCapabilities.setPlatform(Platform.ANY);
        chromCapabilities.setCapability(CapabilityType.BROWSER_NAME, "Chrome");
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            chromCapabilities.setCapability(getCurrentTime() + " StartedBy", System.getProperty("user.name"));
        }
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--lang=pl");
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--disable-setuid-sandbox");
        chromeOptions.addArguments("disable-infobars");
        chromeOptions.addArguments("disable-notifications");
        chromeOptions.addArguments("disable-popup-blocking");
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("safebrowsing-disable-download-protection");
        chromeOptions.addArguments("safebrowsing-disable-extension-blacklist");
        chromeOptions.setAcceptInsecureCerts(true);
        chromeOptions.setLogLevel(ChromeDriverLogLevel.ALL);

        chromeOptions.setCapability("browserVersion", CHROME_VERSION);
        chromeOptions.setCapability("selenoid:options", new HashMap<String, Object>() {{
            put("LANG","pl_PL.UTF-8");
            put("LANGUAGE","pl:en");
            put("LC_ALL","pl_PL.UTF-8");
            put("name", getCurrentTime()+" "+SCENARIO_NAME);
            put("sessionTimeout", "15m");
            put("env", new ArrayList<String>() {{
                add("TZ=Europe/Warsaw");
            }});

            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
                put("environment","PROD");
                put("scenario start time",getCurrentTime());
            }});
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);
        }});
        chromCapabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        chromeOptions.merge(chromCapabilities);
        return chromeOptions;
    }


    public static String getCurrentTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
}
