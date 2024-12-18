package framework.core;

import framework.config.Settings;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.remote.RemoteWebDriver;

@Slf4j
public class DriverContext {

    private static final ThreadLocal<RemoteWebDriver> remoteWebDriverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<AndroidDriver> remoteAndroidDriverThreadLocal = new ThreadLocal<>();
    private static final ThreadLocal<IOSDriver> remoteIOSDriverThreadLocal = new ThreadLocal<>();


    static void setRemoteWebDriverThreadLocal(RemoteWebDriver driverThreadLocal) {
        remoteWebDriverThreadLocal.set(driverThreadLocal);
    }

    public static RemoteWebDriver getRemoteWebDriver() {
        DriverType driverType = Settings.DRIVER_TYPE;

        switch (driverType) {
            case FIREFOX:
            case CHROME:
                return remoteWebDriverThreadLocal.get();
            case ANDROID_APP:
            case ANDROID_CHROME:
                return remoteAndroidDriverThreadLocal.get();
            case IOS_APP:
                return remoteIOSDriverThreadLocal.get();
            case API:
                log.info("For API Test driver not initialized");
                return null;
            case TRANSPORT_GZM_ANDROID:
                return remoteAndroidDriverThreadLocal.get();
            default:
                throw new IllegalArgumentException("Unsupported driver type: " + driverType);
        }
    }

    public static void setRemoteWebDriverThreadLocal(DriverType driverType, RemoteWebDriver driver) {
        switch (driverType) {
            case FIREFOX:
            case CHROME:
                remoteWebDriverThreadLocal.set(driver);
                break;
            case ANDROID_APP:
            case ANDROID_CHROME:
            case TRANSPORT_GZM_ANDROID:
                remoteAndroidDriverThreadLocal.set((AndroidDriver) driver);
                break;
            case IOS_APP:
                remoteIOSDriverThreadLocal.set((IOSDriver) driver);
                break;
            default:
                throw new IllegalArgumentException("Unsupported driver type: " + driverType);
        }
    }
}
